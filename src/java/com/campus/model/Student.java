package com.campus.model;

public class Student {
    public String id, firstName, lastName, gender, dept, faculty, status, photoPath, studentType;
    public int age, year;
    public String admissionDate, birthDate;
    public String fName;
    public String lName;

    public Student(String id, String fName, String lName, int age, String gender, 
                   String dept, String faculty, int year, String status, 
                   String admDate, String birthDate, String photoPath, String studentType) {
        this.id = id;
        this.firstName = fName;
        this.lastName = lName;
        this.age = age;
        this.gender = gender;
        this.dept = dept;
        this.faculty = faculty;
        this.year = year;
        this.status = status;
        this.admissionDate = admDate;
        this.birthDate = birthDate;
        this.photoPath = photoPath;
        this.studentType = studentType;
    }
}