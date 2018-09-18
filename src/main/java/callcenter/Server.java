package callcenter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Server {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel1 = connection.createChannel(1);
	    Channel channel2 = connection.createChannel(2);
	    Channel channel3 = connection.createChannel(3);
	    Channel channel4 = connection.createChannel(4);
	    
	    channel1.queueDeclare("nova linha", true, false, false, null);
	    channel2.queueDeclare("reparo na linha", true, false, false, null);
	    channel3.queueDeclare("cancelamento da linha", true, false, false, null);
	    channel4.queueDeclare("atendidos", true, false, false, null);
	    

	}

}
