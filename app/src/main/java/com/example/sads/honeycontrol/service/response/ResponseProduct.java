package com.example.sads.honeycontrol.service.response;

import com.example.sads.honeycontrol.models.Products;

import java.util.ArrayList;

/**
 * Created by sads on 14/04/17.
 */
public class ResponseProduct {
    private ArrayList<Products> respuesta;
    private boolean success;

    public ArrayList<Products> getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(ArrayList<Products> respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
