package com.example.demo_web_app.model;

import lombok.Data;

import javax.persistence.*;

@SuppressWarnings("ALL")
@Data
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Message() {}

    public Message(String text, String tag, User user) {
        this.text = text;
        this.tag = tag;
        this.author = user;
    }

    public String getAuthorName(){
        return author.getUsername();
    }
}
