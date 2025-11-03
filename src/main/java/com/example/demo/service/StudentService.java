package com.example.demo.service;


import com.example.demo.model.Student;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class StudentService {
    private final Map<Integer, Student> store = new ConcurrentHashMap<>();

    public Student save(Student s) {
        store.put(s.getId(), s);
        return s;
    }
    public Student get(Integer id) {
        return store.get(id);
    }
}