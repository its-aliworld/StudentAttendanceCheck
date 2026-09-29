package com.attendance.controller;

import com.attendance.entity.Subject;
import com.attendance.repository.SubjectRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "http://localhost:5173")
public class SubjectController {
    private final SubjectRepository repository;

    public SubjectController(SubjectRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Subject> all() {
        return repository.findAll();
    }

    @PostMapping
    public Subject create(@RequestBody Subject subject) {
        return repository.save(subject);
    }
}
