package com.example.sads.honeycontrol.service.response;

import com.example.sads.honeycontrol.models.result;

import java.util.ArrayList;

/**
 * Created by sads on 26/03/17.
 */
public class ResponseLogin {
    private ArrayList<result> result;
    private boolean responde;

    public boolean isResponde() {
        return responde;
    }

    public void setResponde(boolean responde) {
        this.responde = responde;
    }

    public ArrayList<result> getResult() {
        return result;
    }

    public void setResult(ArrayList<result> result) {
        this.result = result;
    }
}
