package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Category;
import com.caritos.student_information_system.domain.School;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long>{
	List<Category> findBySchool(School school);
}

