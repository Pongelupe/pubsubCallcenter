package callcenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
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
		final Random random = new Random();
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.basicQos(1);

		channel.queueDeclare("nova linha", true, false, false, null);
		channel.queueDeclare("reparo na linha", true, false, false, null);
		channel.queueDeclare("cancelamento de linha", true, false, false, null);

		BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
		int atendente = 0;
		String linha = d.readLine();
		atendente = Integer.parseInt(linha);
		
		System.out.println("O atendente ate"+atendente+" esta disponivel");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				
				AMQP.BasicProperties replyProps = new AMQP.BasicProperties
		                  .Builder()
		                  .correlationId(properties.getCorrelationId())
		                  .build();

				String resposta = null;
				
				String message = new String(body, "UTF-8");
				boolean resolveu = random.nextBoolean();

				try {
					System.out.println("Existe um novo pedido na fila. Processando requisicao...");
					Thread.sleep(10000);
					if (resolveu == true) {
						resposta = "O pedido foi processado com sucesso";
					} else {
						resposta = "O pedido foi processado mas nao foi possivel efetua-lo";
					}

				} catch (InterruptedException _ignored) {
					Thread.currentThread().interrupt();
				}

				System.out.println("O pedido veio com o seguinte comentario: '" + message + "'");
				channel.basicPublish( "", properties.getReplyTo(), replyProps, resposta.getBytes("UTF-8"));
				System.out.println(resposta);
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		
		switch(atendente){
			case 1:
				channel.basicConsume("nova linha", false, consumer);
				break;
			case 2:
				channel.basicConsume("nova linha", false, consumer);
				channel.basicConsume("reparo na linha", false, consumer);
				break;
			case 3:
				channel.basicConsume("reparo na linha", false, consumer);
				break;
			case 4:
				channel.basicConsume("reparo na linha", false, consumer);
				channel.basicConsume("cancelamento de linha", false, consumer);
				break;
		}

	}

}
