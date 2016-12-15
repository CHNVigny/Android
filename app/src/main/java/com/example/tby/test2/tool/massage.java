package com.example.tby.test2.tool;

import java.io.Serializable;

/**
 * Created by tby on 2016/12/10.
 */

public class massage implements Serializable {

    String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}