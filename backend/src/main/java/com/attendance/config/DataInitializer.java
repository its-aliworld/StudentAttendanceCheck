package com.attendance.config;

import com.attendance.entity.*;
import com.attendance.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seed(UserRepository users,
                           StudentRepository students,
                           SubjectRepository subjects) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (users.findByEmail("admin@example.com").isEmpty()) {
                users.save(new User(
                    "Admin Teacher",
                    "admin@example.com",
                    encoder.encode("admin123"),
                    Role.TEACHER
                ));
            }

            if (users.findByEmail("student@example.com").isEmpty()) {
                users.save(new User(
                    "Demo Student",
                    "student@example.com",
                    encoder.encode("student123"),
                    Role.STUDENT
                ));
            }

            if (students.findByEmail("student@example.com").isEmpty()) {
                students.save(new Student(
                    "STU001",
                    "Demo Student",
                    "student@example.com",
                    "B.Tech CSE"
                ));
            }

            if (subjects.count() == 0) {
                subjects.save(new Subject("Data Structures", "CS201"));
                subjects.save(new Subject("Database Management", "CS202"));
                subjects.save(new Subject("Artificial Intelligence", "AI301"));
            }
        };
    }
}
