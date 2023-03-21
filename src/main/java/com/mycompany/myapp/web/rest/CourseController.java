package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.CourseService;
import com.mycompany.myapp.service.dto.CourseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourseController {

    private CourseService courseService;

    //   public CourseController(CourseService courseService){
    //       this.courseService = courseService;
    //   }

    @DeleteMapping(path = "/student/course/{courseName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dropCourse(@PathVariable String courseName) {
        //call courseService to do real logic
        String userName = getUserName();
        courseService.dropCourse(userName, courseName);
    }

    private String getUserName() {
        return SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> {
                throw new UsernameNotFoundException("Username not found");
            });
    }

    @GetMapping(path = "/allCourses")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CourseDTO> listAllCourse() {
        return courseService.listAllCourses();
    }

    @PostMapping(path = "/student/course/{courseName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void enrollCourse(@PathVariable String courseName) {
        String userName = getUserName();
        courseService.enrollCourse(userName, courseName);
    }

    @GetMapping(path = "/student/courses")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CourseDTO> getEnrolledCourses() {
        String userName = getUserName();
        return courseService.getEnrolledCourse(userName);
    }
}
