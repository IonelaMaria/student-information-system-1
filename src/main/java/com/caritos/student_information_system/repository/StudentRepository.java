package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Guardian;
import com.caritos.student_information_system.domain.School;
import com.caritos.student_information_system.domain.Student;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
	List<Student> findBySchool(School school);
	List<Student> findByGuardian(Guardian guardian);
}
