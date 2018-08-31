package rabbitMQ;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;

import initProject.Parameter;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class SendToServer {

	/*
	 * static String readFile(String path, Charset encoding) throws IOException { //
	 * byte[] encoded = Files.readAllBytes(Paths.get(path)); // return new
	 * String(encoded, encoding);
	 * 
	 * String content = new String(Files.readAllBytes(Paths.get(path))); return
	 * content; }
	 */

	private static final String EXCHANGE_NAME = "topic_logs";

	public static void send(String routingKey, String message) {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			if (Parameter.DebugMode) {
				factory.setHost("localhost");
				//factory.setHost("192.168.23.130");
				//factory.setUsername("OpenCareLab");
				//factory.setPassword("@pencarelab");
				//factory.setPort(5672);
			} else {
				factory.setHost("13.209.7.39");
				factory.setUsername("OpenCareLab");
				factory.setPassword("@pencarelab");
				factory.setPort(5672);
			}

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
			System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

}