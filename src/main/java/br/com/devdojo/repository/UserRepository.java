package br.com.devdojo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devdojo.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUserName(String userName);
	
}
