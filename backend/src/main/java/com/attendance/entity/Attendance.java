package com.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance",
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id", "date"}))
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    @ManyToOne(optional = false)
    private Subject subject;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Attendance() {}

    public Attendance(Student student, Subject subject, LocalDate date, Status status) {
        this.student = student;
        this.subject = subject;
        this.date = date;
        this.status = status;
    }

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public Subject getSubject() { return subject; }
    public LocalDate getDate() { return date; }
    public Status getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setStudent(Student student) { this.student = student; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStatus(Status status) { this.status = status; }
}
