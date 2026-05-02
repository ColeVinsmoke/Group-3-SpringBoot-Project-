package com.example.registration.service;

import com.example.registration.entity.Student;
import com.example.registration.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // register a new student
    public Student registerStudent(Student student) {
        // Check if student already exists by email
        Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
        if (existingStudent.isPresent()) {
            throw new RuntimeException("Student with this email already exists!");
        }
        // Save new student
        return studentRepository.save(student);
    }

    // login 
    public Student loginStudent(String email, String password) {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isEmpty()) {
            throw new RuntimeException("Student not found!");
        }
        if (!student.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid password!");
        }
        return student.get();
    }

    // view all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // view a student by ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
    }

    // update student details
    public Student updateStudent(Long id, Student updatedStudent) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        student.setPassword(updatedStudent.getPassword());

        return studentRepository.save(student);
    }

    //  delete a student
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        studentRepository.delete(student);
    }
}
