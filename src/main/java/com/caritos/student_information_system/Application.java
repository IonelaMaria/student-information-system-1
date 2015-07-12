package com.caritos.student_information_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.caritos.student_information_system.repository.AdministratorRepository;
import com.caritos.student_information_system.repository.CategoryRepository;
import com.caritos.student_information_system.repository.GradeRepository;
import com.caritos.student_information_system.repository.GuardianRepository;
import com.caritos.student_information_system.repository.OrganizationRepository;
import com.caritos.student_information_system.repository.PersonRepository;
import com.caritos.student_information_system.repository.SchoolRepository;
import com.caritos.student_information_system.repository.SectionRepository;
import com.caritos.student_information_system.repository.StudentRepository;
import com.caritos.student_information_system.repository.SuperUserRepository;
import com.caritos.student_information_system.repository.TransactionRepository;

@SpringBootApplication
public class Application {

	@Autowired
	SchoolRepository schoolRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	GradeRepository gradeRepository;
	@Autowired
	SectionRepository sectionRepository;
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	OrganizationRepository organizationRepository;
	@Autowired
	SuperUserRepository superUserRepository;
	@Autowired
	GuardianRepository guardianRepository;
	@Autowired
	AdministratorRepository administratorRepository;
	@Autowired
	PersonRepository userRepository;
	
	public static void main(String[] args) throws Exception {
	    String webPort = System.getenv("PORT");
	    if (webPort == null || webPort.isEmpty()) {
	        webPort = "8080";
	    }
	    System.setProperty("server.port", webPort);
	    
		SpringApplication.run(Application.class, args);
	}

//	@Override
//	public void run(String... arg0) throws Exception {
//		SuperUser superUser = new SuperUser("Eladio", "Caritos", "eladio", "caritos");
//		superUserRepository.save(superUser);
//		SuperUser superA = new SuperUser("a", "a", "a", "a");
//		superUserRepository.save(superA);
//		
//		// Create an organization
//		Organization organizationA = new Organization("A");
//		organizationRepository.save(organizationA);
//		Organization organizationB = new Organization("B");
//		organizationRepository.save(organizationB);
//
//		Administrator admin = new Administrator("admin", "admin", "admin", "admin", organizationA);
//		administratorRepository.save(admin);
//		Administrator adminB = new Administrator("b", "b", "b", "b", organizationB);
//		administratorRepository.save(adminB);
//		
//		// save a couple of schools
//		School s1 = new School(organizationA, "Dhahran Academy");
//		schoolRepository.save(s1);
//		School s2 = new School(organizationB, "Holy Trinity Academy of Calamba");
//		schoolRepository.save(s2);
//
//		// save some categories for the transaction
//		Category bookSchool1 = new Category(s1, "Books");
//		Category tuitionSchool1 = new Category(s1, "Tuition");
//		Category uniformSchool1 = new Category(s1, "Uniform");
//		Category tuitionSchool2 = new Category(s2, "Tuition");
//		Category uniformSchool2 = new Category(s2, "Uniform");
//		categoryRepository.save(uniformSchool1);
//		categoryRepository.save(bookSchool1);
//		categoryRepository.save(tuitionSchool1);
//		categoryRepository.save(tuitionSchool2);
//		categoryRepository.save(uniformSchool2);
//		Category bookSchool2 = new Category(s2, "Books");
//		categoryRepository.save(bookSchool2);
//
//		// save some grades for the student
//		Grade kinder2 = new Grade(s2, "Kinder 2");
//		Grade first = new Grade(s2, "First");
//		Grade second = new Grade(s2, "Second");
//		Grade third = new Grade(s2, "Third");
//		Grade fourth = new Grade(s2, "Fourth");
//		Grade fifth = new Grade(s2, "Fifth");
//		Grade sixth = new Grade(s2, "Sixth");
//		Grade seventh = new Grade(s2, "Seventh");
//		Grade eighth = new Grade(s2, "Eighth");
//		Grade ninth = new Grade(s1, "Ninth");
//		Grade tenth = new Grade(s2, "Tenth");
//		Grade eleventh = new Grade(s2, "Eleventh");
//		Grade twelvth = new Grade(s2, "Twelvth");
//		Grade kinder1 = new Grade(s2, "Kinder 1");
//		gradeRepository.save(kinder1);
//		gradeRepository.save(kinder2);
//		gradeRepository.save(first);
//		gradeRepository.save(second);
//		gradeRepository.save(third);
//		gradeRepository.save(fourth);
//		gradeRepository.save(fifth);
//		gradeRepository.save(sixth);
//		gradeRepository.save(seventh);
//		gradeRepository.save(eighth);
//		gradeRepository.save(ninth);
//		gradeRepository.save(tenth);
//		gradeRepository.save(eleventh);
//		gradeRepository.save(twelvth);
//
//		Section panda = new Section(kinder1, "panda");
//		sectionRepository.save(panda);
//		Section giraffe = new Section(kinder1, "giraffe");
//		sectionRepository.save(giraffe);
//		Section fiveA = new Section(fifth, "5A");
//		Section fiveB = new Section(ninth, "5B");
//		sectionRepository.save(fiveA);
//		sectionRepository.save(fiveB);
//
//		Guardian g1 = new Guardian("First", "Guardian", "x", "x", organizationA);
//		guardianRepository.save(g1);
//		Guardian g2 = new Guardian("Second", "Guardian", "y", "y", organizationB);
//		guardianRepository.save(g2);
//		
//		// save a couple of students
//		Student eladio_caritos = new Student(s1, "Eladio", "Caritos", g1);
//		Student therese_caritos = new Student(s1, "Therese", "Caritos", g1);
//		Student sophia_pauline = new Student(s1, "Sophia", "Pauline", g1);
//		Student claire_alexandra = new Student(s2, "Claire", "Alexandra", g2);
//		Student amelia_francis = new Student(s2, "Amelia", "Francis", g2);
//		Student nina_elizabeth = new Student(s2, "Nina", "Elizabeth", g2);
//		studentRepository.save(eladio_caritos);
//		studentRepository.save(therese_caritos);
//		studentRepository.save(sophia_pauline);
//		studentRepository.save(claire_alexandra);
//		studentRepository.save(amelia_francis);
//		studentRepository.save(nina_elizabeth);
//
//		Transaction t1 = new Transaction(eladio_caritos, LocalDate.now(), "this is a sample desc", new BigDecimal(11.11), bookSchool1);
//		Transaction t2 = new Transaction(therese_caritos, LocalDate.now(), "this is a sample desc", new BigDecimal(22.22), uniformSchool1);
//		Transaction t3 = new Transaction(sophia_pauline, LocalDate.now(), "this is a sample xyza", new BigDecimal(33.33), bookSchool2);
//		Transaction t4 = new Transaction(claire_alexandra, LocalDate.now(), "this is a sample desc", new BigDecimal(44.44), tuitionSchool2);
//		Transaction t5 = new Transaction(amelia_francis, LocalDate.now(), "this is a sample desc", new BigDecimal(55.55), uniformSchool2);
//		Transaction t6 = new Transaction(nina_elizabeth, LocalDate.now(), "this is a sample desc", new BigDecimal(66.66), uniformSchool2);
//		transactionRepository.save(t1);
//		transactionRepository.save(t2);
//		transactionRepository.save(t3);
//		transactionRepository.save(t4);
//		transactionRepository.save(t5);
//		transactionRepository.save(t6);
//	}
	
//	@Bean
//	public ServletRegistrationBean h2servletRegistration() {
//	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
//	    registration.addUrlMappings("/console/*");
//	    return registration;
//	}
}
