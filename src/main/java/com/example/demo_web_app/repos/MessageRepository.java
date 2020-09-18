package com.example.demo_web_app.repos;

import com.example.demo_web_app.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message,Integer> {
        Iterable<Message> findByTag(String tag);
}
