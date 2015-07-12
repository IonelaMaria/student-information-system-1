package com.caritos.student_information_system.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.caritos.student_information_system.util.RandomString;

/**
 * Trying to write less code by removing duplicate items.
 * - Not sure if I should put the "id" field in the User model as well since they have different column names.
 * - I didn't import school either as this is only for Admin and Student and not for Guardian
 */
@Entity
@Table(name="PERSON")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TYPE", discriminatorType=DiscriminatorType.STRING)
public abstract class Person implements Serializable {

	private static final long serialVersionUID = -1829936347750170996L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	protected Long id;
	
	@Column(name="USERNAME", unique=true, nullable=false)
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    
    public Person() {
    	// do nothing 
	}
    
    public Person(String firstName, String lastName, String userName, String password) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.username = userName;
    	this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null) {
//			return false;
//		}
//		if (obj == this) {
//			return true;
//		}
//		if (obj instanceof User) {
//			User other = (User) obj;
//			return new EqualsBuilder().append(id, other.id).isEquals();
//		}
//		return false;
//	}
	
	public String getName() {
		return firstName + " " + lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String generateRandomPassword() {
		RandomString x = new RandomString(8);
		return x.nextString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
