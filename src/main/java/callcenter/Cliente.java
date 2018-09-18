package callcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Cliente {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    int usuario = 0;
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		String message,linha1 = null;
		linha1 = d.readLine();
		usuario = Integer.parseInt(linha1);
		message = d.readLine();
		
		switch(usuario){
		
			case 1:
				channel.queueDeclare("nova linha", true, false, false, null);
				channel.basicPublish( "", "nova linha", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			case 2:
				channel.queueDeclare("reparo na linha", true, false, false, null);
				channel.basicPublish( "", "reparo na linha", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			case 3:
				channel.queueDeclare("cancelamento de linha", true, false, false, null);
				channel.basicPublish( "", "cancelamento de linha", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			case 4:
				channel.queueDeclare("nova linha", true, false, false, null);
				channel.basicPublish( "", "nova linha", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			case 5:
				channel.queueDeclare("cancelamento de linha", true, false, false, null);
				channel.basicPublish( "", "cancelamento de linha", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				System.out.println("User"+usuario+" mandou a mensagem: "+message);
				break;
			
		}
		
		channel.close();
		connection.close();

	}

}
