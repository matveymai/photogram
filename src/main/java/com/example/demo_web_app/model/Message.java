package com.example.demo_web_app.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private String tag;

    public Message() {}

    public Message(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }
}
