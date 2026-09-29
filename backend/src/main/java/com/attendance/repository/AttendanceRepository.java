package com.attendance.repository;

import com.attendance.entity.Attendance;
import com.attendance.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentIdOrderByDateDesc(Long studentId);
    List<Attendance> findByDateAndSubjectId(LocalDate date, Long subjectId);
    Optional<Attendance> findByStudentIdAndSubjectIdAndDate(Long studentId, Long subjectId, LocalDate date);
    long countByStudentIdAndStatus(Long studentId, Status status);
    long countByStudentId(Long studentId);
}
