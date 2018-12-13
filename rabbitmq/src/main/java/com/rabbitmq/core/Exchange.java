package com.rabbitmq.core;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 使用转发器发送信息
 * @author jinlei
 *
 */
public class Exchange {

    private final static String EXCHANGE_NAME = "logs";
    
    public static void main(String[] args) throws IOException {
            
            ConnectionFactory factory=new ConnectionFactory();
            
            factory.setHost("127.0.0.1");
            
            Connection connection=factory.newConnection();
            
            Channel channel=connection.createChannel();
            
            //创建一个非持久的、唯一的、自动删除的队列且队列名称且由服务器随机产生的队列名称
            String queueName = channel.queueDeclare().getQueue();
            
            System.out.println("随机队列名称："+queueName);
            
            //创建转发器
            //转发器类型：Direct:direct类型比较清晰明了，就是在发送消息至转换器时，会指定一个路由key，处理路由键
            //消费者消费的时候也会指定一个路由key，这样你发送的消息指定的是什么routingKey，那么转发器就会把消息转给相应队列对应的消费者进行处理。
            //Topic:将消息中的路由键和已经绑定了路由键的队列进行模式匹配，如果匹配，则将该消息发送到该队列，否则不发，处理路由键
            //Headers:根据发送的消息内容中的headers属性进行匹配,不处理路由键
            //Fanout:fanout转发器把所有它接收到的消息，广播到此转发器的EXCHANGE_NAME参数的所有队列。fanout发布/订阅,不处理路由键
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            
            //参数1：队列名称 ；参数2：转发器名称
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            
            String message = "helloworld--";
            
            channel.basicPublish( EXCHANGE_NAME,"", null, message.getBytes());
            
            System.out.println(" Sent '" + message + "'");

            channel.close();
            
            connection.close();

    }
}
