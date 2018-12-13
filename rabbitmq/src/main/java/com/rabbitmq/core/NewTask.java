package com.rabbitmq.core;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Channel;

/**
 * 工作队列
 * @author jinlei
 *
 */
public class NewTask {

	    //队列名称
		private final static String QUEUE_NAME = "workqueue";

		public static void main(String[] args) throws IOException {
			
			
			ConnectionFactory factory=new ConnectionFactory();
			
			factory.setHost("127.0.0.1");
			
			Connection connection=factory.newConnection();
			
			Channel channel=connection.createChannel();
			//确认RabbitMQ永远不会丢失我们的队列
//			boolean durable = true;
//			channel.queueDeclare("task_queue", durable, false, false, null);
			
			channel.queueDeclare(QUEUE_NAME, false, false,false,null);
			

			/**发送10条信息**/
			for(int i=0;i<5;i++) {
				
				String dots = "";
				for (int j = 6; j >= i; j--)
				{
					dots += ".";
				}
				String message = "helloworld" + dots+dots.length();
				//我们需要标识我们的信息为持久化的。通过设置MessageProperties（implements BasicProperties）值为PERSISTENT_TEXT_PLAIN。
				channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				System.out.println("Sent '" + message + "'");
			
			}
			
			channel.close();
			
			connection.close();
		}
}
