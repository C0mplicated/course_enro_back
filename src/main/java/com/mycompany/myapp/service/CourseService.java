package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserCourse;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.repository.UserCourseRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.CourseDTO;
import com.mycompany.myapp.service.mapper.CourseMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseService {

    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private UserCourseRepository userCourseRepository;

    private CourseMapper courseMapper;

    public void dropCourse(String userName, String courseName) {
        UserCourse userCourse = getUserCourse(userName, courseName);
        //remove
        userCourseRepository.deleteByUserAndCourse(userCourse.getUser(), userCourse.getCourse());
    }

    public List<CourseDTO> listAllCourses() {
        List<Course> all = courseRepository.findAll();
        //        List<CourseDTO> courseDTOList = new ArrayList<>();
        //        for(Course courses : all){
        //            courseDTOList.add(courseMapper.courseToCourseDTO(courses));
        //        }
        //        return courseDTOList;
        //Java8 stream
        return all.stream().map(course -> courseMapper.courseToCourseDTO(course)).collect(Collectors.toList());
    }

    /**
     * 1. check user existence ? get User
     * 2. check course existence ? get course
     * 3. new UserCourse(user, course)
     * 4. check if userCourse not exists
     * 5. save this new UserCourse
     * @param userName
     * @param courseName
     */
    public void enrollCourse(String userName, String courseName) {
        UserCourse userCourse = getUserCourse(userName, courseName);
        Optional<UserCourse> optionalUserCourse = userCourseRepository.findOneByUserAndCourse(userCourse.getUser(), userCourse.getCourse());
        optionalUserCourse.ifPresent(userCourse1 -> {
            throw new IllegalArgumentException("UserCourse already exists: " + userCourse1.toString());
        });
        //save
        userCourseRepository.save(userCourse);
    }

    private UserCourse getUserCourse(String userName, String courseName) {
        //check user
        Optional<User> optionalUser = userRepository.findOneByLogin(userName);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("No such user: " + userName));
        //check course
        Optional<Course> optionalCourse = courseRepository.findOneByCourseName(courseName);
        Course course = optionalCourse.orElseThrow(() -> new IllegalArgumentException("No such course" + courseName));
        return new UserCourse(user, course);
    }

    /**
     * 1.check user existence
     * 2.check course exist based on username
     * 3. get list course
     * 4. convert course to DTO
     * @param userName
     * @return
     */
    public List<CourseDTO> getEnrolledCourse(String userName) {
        Optional<User> optionalUser = userRepository.findOneByLogin(userName);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("No such user: " + userName));
        List<UserCourse> allByUser = userCourseRepository.findAllByUser(user);

        return allByUser
            .stream()
            .map(userCourse -> userCourse.getCourse())
            .map(course -> courseMapper.courseToCourseDTO(course))
            .collect(Collectors.toList());
    }
}
