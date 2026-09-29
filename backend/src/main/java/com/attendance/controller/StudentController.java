package com.attendance.controller;

import com.attendance.entity.Student;
import com.attendance.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {
    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Student> all() {
        return repository.findAll();
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return repository.save(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
