package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserCourse;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    Optional<UserCourse> findOneByUserAndCourse(User user, Course course);

    //增加，删除，更改需要加transactional
    @Transactional
    void deleteByUserAndCourse(User user, Course course);

    List<UserCourse> findAllByUser(User user);
}
