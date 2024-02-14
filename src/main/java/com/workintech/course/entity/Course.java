package com.workintech.course.entity;

import lombok.Data;

@Data
public class Course {
    private Integer id;
    private String name;
    private int credit;
    private Grade grade;
}
