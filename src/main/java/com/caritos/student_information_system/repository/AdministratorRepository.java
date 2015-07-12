package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Administrator;
import com.caritos.student_information_system.domain.Organization;

public interface AdministratorRepository extends PagingAndSortingRepository<Administrator, Long> {
	
	List<Administrator> findByUsernameAndPassword(String username, String password);
	List<Administrator> findByOrganization(Organization organization);
}
