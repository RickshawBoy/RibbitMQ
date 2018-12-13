package com.rabbitmq.core;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * 消费者
 * @author jinlei
 *
 */
public class Receive {

	private  final  static String QUEUE_NAME = "queue";
	
	public static void main(String[] args) throws Exception{
		
		//打开连接和创建频道，与发送端一样  
        ConnectionFactory factory = new ConnectionFactory();
        //设置MabbitMQ所在主机ip或者主机名  
        factory.setHost("127.0.0.1");  
        Connection connection = factory.newConnection();  
        //创建一个频道
        Channel channel = connection.createChannel();  
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。  
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
        System.out.println("Waiting for messages. To exit press CTRL+C");  

                         
//        Consumer consumer = new DefaultConsumer(channel){
//            //重写DefaultConsumer中handleDelivery方法，在方法中获取消息
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, 
//                    AMQP.BasicProperties properties, byte[] body) throws IOException{
//                String message = new String(body);
//                System.out.println("Received '" + message + "'");
//            }
//        }; 
        
        //指定消费队列  
//        channel.basicConsume(QUEUE_NAME, true, consumer);
        
        //创建队列消费者  
        /***
         * 1.队列名称
         * 2.如果服务器应该考虑消息，则为true 承认一次交付
         * 3.消费者对象的接口
         */
        QueueingConsumer consumers = new QueueingConsumer(channel); 
        
        channel.basicConsume(QUEUE_NAME, true, consumers);
        while (true)  
        {  
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）  
            Delivery delivery = consumers.nextDelivery();  
            String message = new String(delivery.getBody());  
            System.out.println("Received '" + message + "'");
        }
	}
}
