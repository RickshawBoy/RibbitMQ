package com.rabbitmq.common;

public enum Templete {

    QUEUE_NAME("hello");//队列名称
    
    private String name;
    
    
    Templete(String name){
        this.name=name;
    };


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
