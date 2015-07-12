package com.caritos.student_information_system.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="SUPERUSER")
@DiscriminatorValue("SuperUser")
public class SuperUser extends Person {

	private static final long serialVersionUID = 2663763432552136110L;

	public SuperUser() {
		super();
	}
	
	public SuperUser(String firstName, String lastName, String userName, String password) {
		super(firstName, lastName, userName, password);
	}
}
