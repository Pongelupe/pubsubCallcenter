package callcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Cliente {

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare("nova linha", true, false, false, null);
	    channel.queueDeclare("reparo na linha", true, false, false, null);
	    channel.queueDeclare("cancelamento de linha", true, false, false, null);

	    int usuario = 0;
		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		String message,linha1 = null;
		linha1 = d.readLine();
		usuario = Integer.parseInt(linha1);
		message = d.readLine();
		
		final String corrId = UUID.randomUUID().toString();

	    String replyQueueName = channel.queueDeclare().getQueue();
	    AMQP.BasicProperties props = new AMQP.BasicProperties
	            .Builder()
	            .correlationId(corrId)
	            .replyTo(replyQueueName)
	            .build();
		
		switch(usuario){
		
			case 1:
				channel.basicPublish( "", "nova linha", props, message.getBytes());
				System.out.println("User"+usuario+" fez um pedido de nova linha com o seguinte comentario: "+message);
				break;
			case 2:
				channel.basicPublish( "", "reparo na linha", props, message.getBytes());
				System.out.println("User"+usuario+" fez um pedido de reparo na linha com o seguinte comentario: "+message);
				break;
			case 3:
				channel.basicPublish( "", "cancelamento de linha", props, message.getBytes());
				System.out.println("User"+usuario+" fez um pedido de cancelamento de linha com o seguinte comentario: "+message);
				break;
			case 4:
				channel.basicPublish( "", "nova linha", props, message.getBytes());
				System.out.println("User"+usuario+" fez um pedido de nova linha com o seguinte comentario: "+message);
				break;
			case 5:
				channel.basicPublish( "", "cancelamento de linha", props, message.getBytes());
				System.out.println("User"+usuario+" fez um pedido de cancelamento de linha com o seguinte comentario: "+message);
				break;
			
		}
		
		final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

	    String ctag = channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	        if (properties.getCorrelationId().equals(corrId)) {
	          response.offer(new String(body, "UTF-8"));
	        }
	      }
	    });

	    String result = response.take();
	    System.out.println(result);
	    channel.basicCancel(ctag);
	    if(result!=null){
	    	channel.close();
			connection.close();
	    }

	}

}
