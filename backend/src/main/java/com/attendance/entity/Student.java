package com.attendance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String rollNumber;

    private String name;
    private String email;
    private String course;

    public Student() {}

    public Student(String rollNumber, String name, String email, String course) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public Long getId() { return id; }
    public String getRollNumber() { return rollNumber; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }

    public void setId(Long id) { this.id = id; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setCourse(String course) { this.course = course; }
}
