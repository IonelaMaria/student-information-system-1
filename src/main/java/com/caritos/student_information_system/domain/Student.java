package com.caritos.student_information_system.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * I've decided to extract the student model from the user model to it's own
 * separate class. I think it makes it easier to understand although, it might
 * lead to duplicity.
 *
 * A student belongs to a school. A potential student is someone interested in
 * attending the school.
 *
 * A student has many transactions.
 *
 * I've decided not to have an email, facebook account for the student. The
 * guardian/parent is the only one who can access their account to see their
 * transaction. So a student cannot login to the application.
 */
@Entity
@Table(name = "student")
public class Student implements Serializable {
	
	private static final long serialVersionUID = -3051206766118755578L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="GUARD_ID", nullable=true)
	private Guardian guardian;
	
    @Column(name="FIRST_NAME")
    private String firstName;
    
    @Column(name="LAST_NAME")
    private String lastName;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="SCHOOL_ID", nullable=false)
    private School school;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GRADE_ID", nullable = true)
	private Grade grade;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SECTION_ID", nullable = true)
	private Section section;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "student")
	private List<Transaction> transactions;

	// if enrolled with the school for the
	// current/upcoming school year, mark as true
	private boolean enrolled; 

	public Student() {
	}

	public Student(School school, String firstName, String lastName, Guardian guardian) {
		this.school = school;
		this.firstName = firstName;
		this.lastName = lastName;
		this.guardian = guardian;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Student) {
			Student other = (Student) obj;
			return new EqualsBuilder().append(id, other.id).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	public boolean isEnrolled() {
		return enrolled;
	}

	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Guardian getGuardian() {
		return guardian;
	}

	public void setGuardian(Guardian guardian) {
		this.guardian = guardian;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
