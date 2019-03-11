package com.wenyizai.wangfuwen.wenyizai.entity;

import java.io.Serializable;

/**
 * Created by wangfuwen on 2016/12/17.
 */

public class Fruit implements Serializable{
    public static final long serialVersionUID = 1L;


    private String name;
    private int imageId;

    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
