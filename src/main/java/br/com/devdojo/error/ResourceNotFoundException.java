package br.com.devdojo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2627730476614932097L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}