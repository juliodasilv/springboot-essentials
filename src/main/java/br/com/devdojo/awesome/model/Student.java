package br.com.devdojo.awesome.model;

import java.io.Serializable;

public class Student implements Serializable{

	private static final long serialVersionUID = -5626546057639629873L;

	private String name;

	public Student() {
	}

	public Student(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}