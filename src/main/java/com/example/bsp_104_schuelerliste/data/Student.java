package com.example.bsp_104_schuelerliste.data;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class Student {
    private String className;
    private String lastName;
    private String firstName;
    private char sex;
    private LocalDate birthdate;

    private int catNr;

    public Student(String className, String lastName, String firstName, char sex, LocalDate date) {
        this.className = className;
        this.lastName = lastName;
        this.firstName = firstName;
        this.sex = sex;
        this.birthdate = date;
    }

    public static Student fromString(String line) {
        String[] splitted = line.split(";");

        return new Student(splitted[0], splitted[1], splitted[2], splitted[3].toUpperCase().toCharArray()[0], LocalDate.parse(splitted[4], DateTimeFormatter.ofPattern("y-M-d")));
    }
}
