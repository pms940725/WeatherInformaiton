package rabbitMQ;

import com.rabbitmq.client.*;

import initProject.AnalyzeRequest;
import initProject.Parameter;

import java.io.IOException;

public class ReceiveFromServer {
	private static final String EXCHANGE_NAME = "topic_logs";

	public static void receive() {
		try {
			ConnectionFactory factory = new ConnectionFactory();

			if (Parameter.DebugMode) {
				factory.setHost("localhost");
				//factory.setHost("192.168.23.130");
				//factory.setUsername("OpenCareLab");
				//factory.setPassword("@pencarelab");
				//factory.setPort(5672);
			}
			else {
				factory.setHost("13.209.7.39");
				factory.setUsername("OpenCareLab");
				factory.setPassword("@pencarelab");
				factory.setPort(5672);
			}
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
			String queueName = channel.queueDeclare().getQueue();

			/*
			 * for (String bindingKey : topicArray) { channel.queueBind(queueName,
			 * EXCHANGE_NAME, bindingKey); }
			 */
			String bindingKey = Parameter.Container_Name + ".#";
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);

			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			Consumer consumer = new DefaultConsumer(channel) {
				
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

					// 06. RabbitMQ를 통해 응답이 왔을 경우 해당 정보를 Parsing 하여 동작
					AnalyzeRequest.analyzeRequest(envelope.getRoutingKey(), message);
				}
			};
			channel.basicConsume(queueName, true, consumer);
		} catch (Exception e) {
			System.out.println("Error in Subscribing");
			e.printStackTrace();
		}
	}

}
