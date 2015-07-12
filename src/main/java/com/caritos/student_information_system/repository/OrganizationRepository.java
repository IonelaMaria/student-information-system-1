package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Organization;

public interface OrganizationRepository extends PagingAndSortingRepository<Organization, Long> {
	List<Organization> findByName(String name);
}
