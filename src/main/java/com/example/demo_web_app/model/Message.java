package com.example.demo_web_app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@SuppressWarnings("ALL")
@Data
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please fill the message")
    @Length(max = 2048,message = "Message too long (more than 2kB)")
    private String text;
    @NotBlank(message = "Please fill the tag")
    @Length(max = 255,message = "Message too long (more than 255 letters)")
    private String tag;

    private String filename;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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
