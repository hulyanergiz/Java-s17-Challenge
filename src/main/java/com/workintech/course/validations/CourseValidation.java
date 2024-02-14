package com.workintech.course.validations;

import com.workintech.course.entity.Course;
import com.workintech.course.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

public class CourseValidation {
    public static void checkName(String name){
        if(name==null||name.isEmpty()){
            throw new ApiException("Name cannot be null or empty.", HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkCredit(Integer credit){
        if(credit==null||credit<0||credit>4){
            throw new ApiException("credit cannot be null or not between 0-4.",HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkIsNameExist(List<Course> courses,String name){
        Optional<Course> optionalCourse= courses.stream()
                .filter(course->course.getName().equalsIgnoreCase(name))
                .findAny();

        if(optionalCourse.isPresent()){
            throw new ApiException(name+" is already exist in Course List.",HttpStatus.BAD_REQUEST);
        }
    }
    public static void checkId(Integer id){
        if(id==null||id<0){
            throw new ApiException("id cannot be null or less than zero",HttpStatus.BAD_REQUEST);
        }
    }
}
