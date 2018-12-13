package com.rabbitmq.core;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

/**
 * 生产者
 * @author jinlei
 *
 */
public class Send {

	//队列名称  
    private final static String QUEUE_NAME = "queue";  
    
    
    public static void main(String[] args) throws Exception{
		
    	/**创建连接池连接RibbitMQ**/
    	ConnectionFactory factory=new ConnectionFactory();
    	/**设置ip**/
    	factory.setHost("127.0.0.1");
    	/**建立一个连接**/
    	Connection connection=factory.newConnection();
    	/**创建一个频道**/
    	Channel channel=connection.createChannel();
    	/**指定一个队列
    	 * 1.队列名称
    	 * 2.true则为一个持久队列（队列在服务器重新启动后仍然有效）
    	 * 3.是否仅限于此连接
    	 * 4.当不再使用时，服务器将删除它
    	 * 5.队列的其他属性(构造参数)
    	 * **/
    	channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    	/**发送的消息 **/
    	String sd="hello world!";
    	String message=new String(sd.getBytes(),"UTF-8");        
    	/**往队列里面发送信息
    	 * 1.将消息发布到的交换器
    	 * 2.队列名称
    	 * 3.消息路由头的其他属性
    	 * 4.消息的字节流
    	 * **/
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("Sent '" + message + "'");  
        //关闭频道和连接  
        channel.close();  
        connection.close(); 
	}
    
}
