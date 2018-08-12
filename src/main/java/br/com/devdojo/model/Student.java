package br.com.devdojo.model;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Student extends AbstractEntity{

	private static final long serialVersionUID = -645790858764812308L;

	@NotEmpty
	private String name;

	@NotEmpty
	@Email
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}