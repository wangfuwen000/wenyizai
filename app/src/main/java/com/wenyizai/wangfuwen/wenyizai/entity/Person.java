package com.wenyizai.wangfuwen.wenyizai.entity;

import java.io.Serializable;

/**
 * Created by user on 15/9/8.
 */
public class Person implements Serializable{
    public static final long seriaVersionUID = 1L;

    public int age;
    public String name;
    public  int _id;
    public  String info;

    public Person(){

    }

    public Person(String name,int age,String info){
        this.name = name;
        this.age = age;
        this.info = info;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
