package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.service.dto.CourseDTO;
import org.springframework.stereotype.Service;

@Service
public class CourseMapper {

    public CourseDTO courseToCourseDTO(Course course) {
        return CourseDTO
            .builder()
            .courseName(course.getCourseName())
            .courseContent(course.getCourseContent())
            .courseLocation(course.getCourseLocation())
            .teacherId(course.getTeacherId())
            .build();
    }
}
