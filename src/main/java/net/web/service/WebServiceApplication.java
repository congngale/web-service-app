package net.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.web.service.models.ClientData;
import net.web.service.repositories.ClientDataRepository;
import net.web.service.repositories.ClientRepository;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.io.IOException;
import java.util.UUID;

@SpringBootApplication
public class WebServiceApplication {

	@Autowired
	private ClientRepository repository;

	@Autowired
	private ClientDataRepository dataRepository;

	@Autowired
	private ServerConfiguration.ServerGateway serverGateway;

	private ObjectMapper mapper =  new ObjectMapper();

	private boolean clientState = false;

	public static int threshold;

	public static String connectionId;

	public static void main(String[] args) {
		SpringApplication.run(WebServiceApplication.class, args);
//		Socket socket = SocketFactory.getDefault().createSocket("localhost", 2019);
//		socket.getOutputStream().write("foo\r\n".getBytes());
//		socket.close();
//		Thread.sleep(1000);
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inbound() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName("abgwsudg");
		options.setPassword("Fcz6bH0Q5XIL".toCharArray());
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(options);

		MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter("tcp://m16.cloudmqtt.com:17245",
						"web-service-app" + UUID.randomUUID().toString(), factory, "net/serverGateway/#");
		adapter.setCompletionTimeout(60000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				System.out.println(message.getPayload());
				try {
					//convert to client data
					ClientData data = mapper.readValue(message.getPayload().toString(), ClientData.class);

					//save data
					dataRepository.insert(data);

					//check connection id
					if (connectionId != null && !connectionId.isEmpty()) {
						//check client
						if (threshold < data.data && !clientState) {
							//set state
							clientState = true;

							//take action
							serverGateway.send("ON", connectionId);
						} else if (clientState) {
							//set state
							clientState = false;

							//take action
							serverGateway.send("OFF", connectionId);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}
}