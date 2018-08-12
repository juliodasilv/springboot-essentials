package br.com.devdojo.awesome.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devdojo.awesome.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
	List<Student> findByNameIgnoreCaseContaining(String name);
}