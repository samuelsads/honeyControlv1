package com.example.sads.honeycontrol.service.response;

/**
 * Created by sads on 15/04/17.
 */
public class ResponseInsertProduct {
    private String success;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


}
