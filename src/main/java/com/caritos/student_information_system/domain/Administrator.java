package com.caritos.student_information_system.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ADMINISTRATOR")
@DiscriminatorValue("Administrator")
public class Administrator extends Person {

	private static final long serialVersionUID = 5476469628825719150L;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORGANIZATION_ID", nullable = true)
	private Organization organization;
	
	public Administrator() {
		super();
	}
	
	public Administrator(Organization organization) {
		super();
		this.organization = organization;
	}
	
	public Administrator(String firstName, String lastName, String userName, String password, Organization organization) {
		super(firstName, lastName, userName, password);
		this.organization = organization;
	}
	
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}
