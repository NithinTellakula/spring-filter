package com.example.demo.service;

import com.example.demo.model.Student;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudentService {
    private final Map<Integer, Student> store = new ConcurrentHashMap<>();

    public Student save(Student s) {
        if (s == null || s.getId() == null) {
            throw new IllegalArgumentException("Student or ID cannot be null");
        }
        store.put(s.getId(), s);
        return s;
    }

    public Student get(Integer id) {
        return store.get(id);
    }
}
