package com.zjl.tech.rabbitmq.spring;

import java.io.Serializable;

public class A implements Serializable{

    private String name;

    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
