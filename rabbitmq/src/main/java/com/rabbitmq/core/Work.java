package com.rabbitmq.core;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * 接收工作队列
 * @author jinlei
 * 默认的，RabbitMQ会一个一个的发送信息给下一个消费者(consumer)，而不考虑每个任务的时长等等，
 * 且是一次性分配，并非一个一个分配。平均的每个消费者将会获得相等数量的消息。这样分发消息的方式叫做round-robin。
 */

public class Work {

	 //队列名称
	 private final static String QUEUE_NAME = "workqueue";
	 
	 public static void main(String[] args) throws IOException, InterruptedException {
		
		//区分不同工作进程的输出
		int hashCode = Work.class.hashCode();
		 
		ConnectionFactory factory=new ConnectionFactory();
		
		factory.setHost("localhost");
		
		Connection connection=factory.newConnection();
		
		Channel channel=connection.createChannel(); 
		
		//声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		System.out.println(hashCode
				+ " [*] Waiting for messages. To exit press CTRL+C");
		
		QueueingConsumer consumer=new QueueingConsumer(channel);
//		boolean ack = false ; //打开应答机制
//		channel.basicConsume(QUEUE_NAME, ack, consumer);
		
		
		channel.basicConsume(QUEUE_NAME, true, consumer);
		//公平转发（Fair dispatch）,设置最大服务转发消息数量
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);
		while (true)
		{
			Delivery delivery = consumer.nextDelivery();

			String message = new String(delivery.getBody());
 
			System.out.println(hashCode + " Received '" + message + "'");
			
			doWork(message);
			
			System.out.println(hashCode + " Done");
			
			//另外需要在每次处理完成一个消息后，手动发送一次应答。配合是否已经打开了应答机制
			//channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}

	}
	 
	 /**
		 * 每个点耗时1s
		 * @param task
		 * @throws InterruptedException
		 */
		private static void doWork(String task) throws InterruptedException
		{
			for (char ch : task.toCharArray())
			{
				if (ch == '.')
					Thread.sleep(1000);
			}
		}
	 
}
