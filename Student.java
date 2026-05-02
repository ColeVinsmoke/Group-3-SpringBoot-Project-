package com.example.studentCorseReg.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    @NotBlank
    @Size(min =3, max =20, message="Enter minimum of 3 and maximum o 20")
    private String fullName;
    @Column(unique=true)
    private String email;
    @NotBlank(message = "password  is required")
    private String password;



    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fulltName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", level=" + password +

                '}';
    }
}
