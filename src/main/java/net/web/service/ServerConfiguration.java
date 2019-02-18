package net.web.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;


@Configuration()
@EnableIntegration
@IntegrationComponentScan
public class ServerConfiguration implements ApplicationListener<TcpConnectionEvent> {

    private final int port = 2019;//SocketUtils.findAvailableServerSocket(5000);

    @MessagingGateway(defaultRequestChannel="toTcp")
    public interface ServerGateway {
        void send(@Payload String data, @Header(IpHeaders.CONNECTION_ID) String connectionId);
    }

    @Bean
    public AbstractServerConnectionFactory serverFactory() {
        return new TcpNetServerConnectionFactory(port);
    }

    @Bean
    public MessageChannel toTcp() {
        System.out.println("creating toTcp DirectChannel");
        DirectChannel dc = new DirectChannel();
        dc.setBeanName("toTcp");

        return dc;
    }

    @Bean
    public MessageChannel fromTcp() {
        return new DirectChannel();
    }

    // Inbound channel adapter. This receives the data from the client
    @Bean
    public TcpReceivingChannelAdapter inboundAdapter(AbstractServerConnectionFactory connectionFactory) {
        TcpReceivingChannelAdapter inbound = new TcpReceivingChannelAdapter();

        inbound.setConnectionFactory(connectionFactory);
        inbound.setOutputChannel(fromTcp());

        return inbound;
    }

    // Outbound channel adapter. This sends the data to the client
    @Bean
    @ServiceActivator(inputChannel="toTcp")
    public TcpSendingMessageHandler outboundAdapter(AbstractServerConnectionFactory connectionFactory) {
        TcpSendingMessageHandler outbound = new TcpSendingMessageHandler();
        outbound.setConnectionFactory(connectionFactory);
        return outbound;
    }

    @Override
    public void onApplicationEvent(TcpConnectionEvent event) {
        System.out.println("Got TcpConnectionEvent: source =" + event.getSource() +
                ", id =" + event.getConnectionId());

        WebServiceApplication.connectionId = event.getConnectionId();
    }

    // Endpoint example
    @MessageEndpoint
    public static class HandleMessage {

        // Server
        @Transformer(inputChannel="fromTcp", outputChannel="handle")
        public String input(byte[] bytes) {
            return new String(bytes);
        }

        // Server
        @ServiceActivator(inputChannel="handle", outputChannel="toTcp")
        public String output(String in) {
            return in;
        }
    }
}
