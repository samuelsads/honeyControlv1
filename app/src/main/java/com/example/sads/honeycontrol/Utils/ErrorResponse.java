package com.example.sads.honeycontrol.Utils;

/**
 * Created by sads on 2/05/17.
 */
public class ErrorResponse {

    public ErrorResponse() {
    }

    public static String errorConection(String error){
        switch (error){
            case "1":
                return "Error conect service";
            case "2":
                return "Error post";
            case "3":
                return "Error autentication";
            case "4":
                return "Error query";
        }
        return "";
    }
}
