package com.example.helloworld;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Message {

    @Id
    private int id;
    private String text;

    // Default constructor
    public Message() {
    }

    // Constructor with parameters (optional)
    public Message(int id, String text) {
        this.id = id;
        this.text = text;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
