package com.attendance.controller;

import com.attendance.entity.*;
import com.attendance.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:5173")
public class AttendanceController {
    private final AttendanceRepository attendance;
    private final StudentRepository students;
    private final SubjectRepository subjects;

    public AttendanceController(AttendanceRepository attendance,
                                 StudentRepository students,
                                 SubjectRepository subjects) {
        this.attendance = attendance;
        this.students = students;
        this.subjects = subjects;
    }

    @PostMapping
    public ResponseEntity<?> mark(@RequestBody Map<String, String> body) {
        Long studentId = Long.valueOf(body.get("studentId"));
        Long subjectId = Long.valueOf(body.get("subjectId"));
        LocalDate date = LocalDate.parse(body.get("date"));
        Status status = Status.valueOf(body.get("status"));

        Student student = students.findById(studentId).orElseThrow();
        Subject subject = subjects.findById(subjectId).orElseThrow();

        Attendance a = attendance
                .findByStudentIdAndSubjectIdAndDate(studentId, subjectId, date)
                .orElse(new Attendance(student, subject, date, status));

        a.setStatus(status);
        return ResponseEntity.ok(attendance.save(a));
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> studentHistory(@PathVariable Long studentId) {
        return attendance.findByStudentIdOrderByDateDesc(studentId);
    }

    @GetMapping("/summary/{studentId}")
    public Map<String, Object> summary(@PathVariable Long studentId) {
        long total = attendance.countByStudentId(studentId);
        long present = attendance.countByStudentIdAndStatus(studentId, Status.PRESENT);
        double percentage = total == 0 ? 0 : (present * 100.0 / total);

        return Map.of(
                "total", total,
                "present", present,
                "absent", total - present,
                "percentage", Math.round(percentage * 100.0) / 100.0
        );
    }
}
