

package br.com.devdojo.javaclient;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.devdojo.model.Student;

public class JavaSpringClientTest {

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplateBuilder()
				.rootUri("http://localhost:8080/v1/protected/students")
				.basicAuthorization("juliodasilv", "Athena00)")
				.build();
		
		//Retorna diretamente o objeto student
		Student student = restTemplate.getForObject("/{id}", Student.class, "1");
		System.out.println(student);
		
		//Retorna o objeto student junto com os cabecalhos http
		ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, "1");
		System.out.println(forEntity.getBody());
		
		//Retorna um array de student
		Student[] students = restTemplate.getForObject("/", Student[].class);
		System.out.println(Arrays.toString(students));
		
		//Retorna uma lista de student com os cabeçalhos http
		ResponseEntity<List<Student>> exchange = restTemplate.exchange("/", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
		});
		System.out.println(exchange.getBody());
	}
}
