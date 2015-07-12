package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Organization;
import com.caritos.student_information_system.domain.School;

public interface SchoolRepository extends PagingAndSortingRepository<School, Long> {
	List<School> findByName(String name);
	List<School> findByOrganization(Organization organization);
}
