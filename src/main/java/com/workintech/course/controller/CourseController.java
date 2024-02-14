package com.workintech.course.controller;

import com.workintech.course.entity.ApiResponse;
import com.workintech.course.entity.Course;
import com.workintech.course.entity.CourseGpa;
import com.workintech.course.exceptions.ApiException;
import com.workintech.course.validations.CourseValidation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private List<Course> courses;
    private final CourseGpa lowCourseGpa;
    private final CourseGpa mediumCourseGpa;
    private final CourseGpa highCourseGpa;
    @Autowired
    public CourseController(@Qualifier("lowCourseGpa") CourseGpa lowCourseGpa,
                            @Qualifier("mediumCourseGpa") CourseGpa mediumCourseGpa,
                            @Qualifier("highCourseGpa") CourseGpa highCourseGpa){
         this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }
    @PostConstruct
    public void init(){
      courses=new ArrayList<>()  ;
    }

    @GetMapping
    public List<Course> getCourseList(){
        return courses;
    }

    @GetMapping("/{name}")
    public Course getCourseByName(@PathVariable String name){
        CourseValidation.checkName(name);
        return courses.stream()
                .filter(course->course.getName().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(()->new ApiException(name+" not found", HttpStatus.NOT_FOUND));
        }
    @PostMapping
    public ResponseEntity<ApiResponse> addCourseToCourseList(@RequestBody Course course){
        CourseValidation.checkName(course.getName());
        CourseValidation.checkCredit(course.getCredit());
        CourseValidation.checkIsNameExist(courses,course.getName());
        courses.add(course);
        Integer totalGpa=getTotalGpa(course);
        ApiResponse apiResponse=new ApiResponse(course,totalGpa);
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);

    }
    private Integer getTotalGpa(Course course){
        if(course.getCredit()<=2){
           return course.getGrade().getCoefficient()*course.getCredit()* lowCourseGpa.getGpa();
        }else if(course.getCredit()==3){
            return course.getGrade().getCoefficient()*course.getCredit()* mediumCourseGpa.getGpa();
        }else{
            return course.getGrade().getCoefficient()*course.getCredit()* highCourseGpa.getGpa();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Integer id,@RequestBody Course course){
        CourseValidation.checkId(id);
        CourseValidation.checkName(course.getName());
        CourseValidation.checkCredit(course.getCredit());

        Course existingCourse=courses.stream()
                .filter(c-> Objects.equals(c.getId(), id))
                .findAny()
                .orElseThrow(()->new ApiException(id+"-> this id not found",HttpStatus.NOT_FOUND));

        int indexOfExistingCourse=courses.indexOf(existingCourse);
        course.setId(id);
        courses.set(indexOfExistingCourse,course);
        Integer totalGpa=getTotalGpa(course);
        ApiResponse apiResponse=new ApiResponse(courses.get(indexOfExistingCourse),totalGpa);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Course deleteCourse(@PathVariable Integer id){
        CourseValidation.checkId(id);
        Course deletedCourse=courses.stream()
                .filter(c->c.getId().equals(id))
                .findAny()
                .orElseThrow(()->new ApiException(id+"-> this id not found",HttpStatus.NOT_FOUND));

        courses.remove(deletedCourse);
        return deletedCourse;

    }


}
