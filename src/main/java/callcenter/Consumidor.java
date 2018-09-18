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

public class Consumidor {
	
	private static final String EXCHANGE_NAME = "sac";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		int atendente = 0;
		String linha = d.readLine();
		atendente = Integer.parseInt(linha);
		
		String nomeFila = channel.queueDeclare().getQueue();
		System.out.println(nomeFila);
		
		switch(atendente){
			case 1:
				channel.queueBind(nomeFila, EXCHANGE_NAME, "nova linha");
				break;
			case 2:
				channel.queueBind(nomeFila, EXCHANGE_NAME, "nova linha");
				channel.queueBind(nomeFila, EXCHANGE_NAME, "reparo na linha");
				break;
			case 3:
				channel.queueBind(nomeFila, EXCHANGE_NAME, "reparo na linha");
				break;
			case 4:
				channel.queueBind(nomeFila, EXCHANGE_NAME, "reparo na linha");
				channel.queueBind(nomeFila, EXCHANGE_NAME, "cancelamento de linha");
				break;
		}
		
		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
				String message = new String(body, "UTF-8");
				System.out.println(message);
			}
		};
		
		channel.basicConsume(nomeFila, true, consumer);
		
		
		
	}

}
