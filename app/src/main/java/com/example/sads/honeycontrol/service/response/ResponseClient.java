package com.example.sads.honeycontrol.service.response;

import com.example.sads.honeycontrol.models.Client;
import com.example.sads.honeycontrol.models.Products;

import java.util.ArrayList;

/**
 * Created by sads on 31/03/17.
 */
public class ResponseClient {
    private ArrayList<Client> respuesta;
    private boolean success;

    public ArrayList<Client> getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(ArrayList<Client> respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
