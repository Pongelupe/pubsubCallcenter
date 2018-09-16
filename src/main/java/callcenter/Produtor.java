package callcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Produtor {
	
	private static final String EXCHANGE_NAME = "sac";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME,"direct");
		
		int usuario = 0;
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		String message,linha1 = null;
		linha1 = d.readLine();
		usuario = Integer.parseInt(linha1);
		message = d.readLine();
		
		switch(usuario){
		
			case 1:
				channel.basicPublish(EXCHANGE_NAME, "nova", null, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			case 2:
				channel.basicPublish(EXCHANGE_NAME, "reparo na linha", null, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			case 3:
				channel.basicPublish(EXCHANGE_NAME, "cancelamento de linha", null, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			case 4:
				channel.basicPublish(EXCHANGE_NAME, "nova linha", null, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			case 5:
				channel.basicPublish(EXCHANGE_NAME, "cancelamento de linha", null, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			
		}
		
		channel.close();
		connection.close();
	}

}
