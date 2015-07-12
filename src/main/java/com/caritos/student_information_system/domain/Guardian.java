package com.caritos.student_information_system.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ibm.icu.text.NumberFormat;

@Entity
@Table(name="guardian")
@DiscriminatorValue("Guardian")
public class Guardian extends Person {

	private static final long serialVersionUID = 3857383109513808523L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORGANIZATION_ID", nullable = true)
	private Organization organization;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "guardian")
	private List<Student> students;
	
	// total amount owed for all students associated with guardian
	private String balance = "0.00";
	
	public Guardian() {
		super();
	}

	public Guardian(String firstName, String lastName, String userName, String password, Organization organization) {
		super(firstName, lastName, userName, password);
		this.organization = organization;
	}
	
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void setBalance() {
		double total = 0.0;
		for(Student student: students) {
			List<Transaction> transactions = student.getTransactions();
			for(Transaction transaction: transactions) {
				total += transaction.getAmount().doubleValue();
			}
		}
		balance = NumberFormat.getCurrencyInstance().format(total);
	}

	public String getBalance() {
		return balance;
	}
}
