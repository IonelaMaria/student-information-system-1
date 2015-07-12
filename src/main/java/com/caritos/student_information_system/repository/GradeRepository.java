package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Grade;
import com.caritos.student_information_system.domain.School;

public interface GradeRepository extends PagingAndSortingRepository<Grade, Long> {
	List<Grade> findBySchool(School school);
}
