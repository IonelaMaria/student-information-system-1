package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Grade;
import com.caritos.student_information_system.domain.Section;

public interface SectionRepository extends PagingAndSortingRepository<Section, Long> {
	List<Section> findByGrade(Grade grade);
}
