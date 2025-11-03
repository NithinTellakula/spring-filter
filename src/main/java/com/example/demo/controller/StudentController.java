package com.example.demo.controller;


import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping("/save")
    public ResponseEntity<?> saveStudent(@RequestBody Student student) {
        logger.info("Saving student id={}", student.getId());
        Student saved = studentService.save(student);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Integer id) {
        Student s = studentService.get(id);
        if (s == null) {
            return ResponseEntity.status(404).body("Student not found");
        }
        return ResponseEntity.ok(s);
    }
}