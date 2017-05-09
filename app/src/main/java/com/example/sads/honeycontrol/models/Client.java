package com.example.sads.honeycontrol.models;

/**
 * Created by sads on 31/03/17.
 */
public class Client {
    private int id;
    private String name;
    private String father_surname;
    private String mother_surname;

    public Client(){

    }
    public Client(int id, String name, String father_surname, String mother_surname) {
        this.id = id;
        this.name = name;
        this.father_surname =father_surname;
        this.mother_surname = mother_surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather_surname() {
        return father_surname;
    }

    public void setFather_surname(String father_surname) {
        this.father_surname = father_surname;
    }

    public String getMother_surname() {
        return mother_surname;
    }

    public void setMother_surname(String mother_surname) {
        this.mother_surname = mother_surname;
    }
}
