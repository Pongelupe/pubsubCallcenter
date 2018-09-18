package callcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Atendente {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		final Channel channel1 = connection.createChannel();
		final Channel channel2 = connection.createChannel();

		channel1.basicQos(1);
		channel2.basicQos(1);
		
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		int atendente = 0;
		String linha = d.readLine();
		atendente = Integer.parseInt(linha);
		
		switch(atendente){
			case 1:
				channel1.queueDeclare("nova linha", true, false, false, null);
				break;
			case 2:
				channel1.queueDeclare("nova linha", true, false, false, null);
				channel2.queueDeclare("reparo na linha", true, false, false, null);
				break;
			case 3:
				channel1.queueDeclare("reparo na linha", true, false, false, null);
				break;
			case 4:
				channel1.queueDeclare("cancelamento de linha", true, false, false, null);
				channel2.queueDeclare("reparo na linha", true, false, false, null);
				break;
		}
		
		if(atendente==2||atendente==4){
			final Consumer consumer1 = new DefaultConsumer(channel1) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");

					System.out.println(" [x] Received '" + message + "'");
					channel1.basicAck(envelope.getDeliveryTag(), false);
				}
			};
			
			final Consumer consumer2 = new DefaultConsumer(channel2) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");

					System.out.println(" [x] Received '" + message + "'");
					channel2.basicAck(envelope.getDeliveryTag(), false);
				}
			};

			channel1.basicConsume("", false, consumer1);
			channel2.basicConsume("", false, consumer2);
		}
		else if(atendente==1||atendente==3){
			final Consumer consumer1 = new DefaultConsumer(channel1) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");

					System.out.println(" [x] Received '" + message + "'");
					channel1.basicAck(envelope.getDeliveryTag(), false);
				}
			};
			
			channel1.basicConsume("", false, consumer1);
		}

	}

}
