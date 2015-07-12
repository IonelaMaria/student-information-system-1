package com.caritos.student_information_system.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.caritos.student_information_system.domain.Student;
import com.caritos.student_information_system.domain.Transaction;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
	List<Transaction> findByStudent(Student student);
}
