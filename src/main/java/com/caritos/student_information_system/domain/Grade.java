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

/**
 * Different schools have different grade levels.
 * One school might have grades 1-6, another school Kinder to Grade 12
 * That's why we associate the grade with the school.
 *
 * When the student is registered to the school, we assign him a grade.
 *
 * Each grade might have multiple sections.
 */
@Entity
@Table(name="grade")
public class Grade implements Serializable {

	private static final long serialVersionUID = 7511224964292495163L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GRADE_ID", nullable = false)
    private Long gradeId;
    private String name;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCHOOL_ID", nullable = false)
    private School school;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "grade")
    private List<Section> sections;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "section")
	private List<Student> students;
	
    public Grade() {}

    public Grade(School school, String name) {
        this.school = school;
        this.name = name;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return name;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}
	
	
}
