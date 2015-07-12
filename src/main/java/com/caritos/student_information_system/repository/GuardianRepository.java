package com.caritos.student_information_system.repository;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.caritos.student_information_system.domain.Guardian;
import com.caritos.student_information_system.domain.Organization;

public interface GuardianRepository extends PagingAndSortingRepository<Guardian, Long> {
	List<Guardian> findByUsernameAndPassword(String username, String password);
	List<Guardian> findByOrganization(Organization organization);
}
