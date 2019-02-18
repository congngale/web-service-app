package net.web.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.web.service.models.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(WebServiceApplication.class);

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
        logger.info("Got TcpConnectionEvent: source =" + event.getSource() +
                ", id =" + event.getConnectionId());

        WebServiceApplication.connectionId = event.getConnectionId();
    }

    // Endpoint example
    @MessageEndpoint
    public static class HandleMessage {

        // Server
        @Transformer(inputChannel="fromTcp", outputChannel="handle")
        public Client input(byte[] bytes) throws Exception {
            //convert to string
            String input = new String(bytes);

            logger.info("TCP server received data = " + input);

            //convert to client
            return new ObjectMapper().readValue(input, Client.class);
        }

        // Server
        @ServiceActivator(inputChannel="handle", outputChannel="toTcp")
        public void output(Client client) {
            //set connected client
            WebServiceApplication.clientId = client.id;
        }
    }
}
