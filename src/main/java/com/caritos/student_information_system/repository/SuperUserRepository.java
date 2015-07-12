package com.caritos.student_information_system.repository;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.caritos.student_information_system.domain.SuperUser;

public interface SuperUserRepository extends PagingAndSortingRepository<SuperUser, Long> {
	List<SuperUser> findByUsernameAndPassword(String username, String password);
}
