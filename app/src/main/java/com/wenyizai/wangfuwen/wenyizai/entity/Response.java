package com.wenyizai.wangfuwen.wenyizai.entity;

import java.io.Serializable;

/**
 * Created by wangfuwen on 2017/4/18.
 */

public class Response implements Serializable {
public static final long serialVersionUID = 1L;

private boolean error;
    private int errortype;
    private String errorMessage;
    private String result;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getErrortype() {
        return errortype;
    }

    public void setErrortype(int errortype) {
        this.errortype = errortype;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

