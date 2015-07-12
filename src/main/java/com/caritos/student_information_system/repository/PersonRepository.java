package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Person;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
	List<Person> findByUsernameAndPassword(String username, String password);
}
