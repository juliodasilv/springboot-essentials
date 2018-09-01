package br.com.devdojo;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTest {
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private StudentRepository studentRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@TestConfiguration
	static class config{
		@Bean
		public RestTemplateBuilder restTemplateBuilder(){
			return new RestTemplateBuilder().basicAuthorization("juliodasilv", "12345678");
		}
	}
	
	@Before
	public void setup(){
		System.out.println("Servidor iniciado na porta " + port);
		Student student = new Student(1L, "Legolas", "legolas@lotr.com");
		BDDMockito.when(studentRepository.findOne(1L)).thenReturn(student);
	}
	
	@Test
	public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
		restTemplate = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/v1/protected/students/", String.class);
		Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
	}

	@Test
	public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200(){
		List<Student> students = Arrays.asList(new Student(1L, "Legolas", "legolas@lotr.com"), new Student(2L, "Aragorn", "aragorn@lotr.com"));
		BDDMockito.when(studentRepository.findAll()).thenReturn(students);
		restTemplate = restTemplate.withBasicAuth("juliodasilv", "12345678");
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/v1/protected/students/", String.class);
		Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void getStudentsByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
		restTemplate = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/v1/protected/students/1", String.class);
		Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
	}

	@Test
	public void getStudentsByIdWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200(){
		restTemplate = restTemplate.withBasicAuth("juliodasilv", "12345678");
		ResponseEntity<Student> responseEntity = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, 1L);
		Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void getStudentsByIdWhenStudentDoesNotExistShouldReturnStatusCode404(){
		restTemplate = restTemplate.withBasicAuth("juliodasilv", "12345678");
		ResponseEntity<Student> responseEntity = restTemplate.getForEntity("/v1/protected/students/{id}", Student.class, -1);
		Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200(){
		BDDMockito.doNothing().when(studentRepository).delete(1l);
		ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, 1L);
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	@WithMockUser(username="xx", password="xx", roles="ADMIN")
	public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() throws Exception{
		BDDMockito.doNothing().when(studentRepository).delete(1l);
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/v1/admin/students/{id}", -1L))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@WithMockUser(username="xx", password="xx", roles="USER")
	public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception{
		BDDMockito.doNothing().when(studentRepository).delete(1l);
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/v1/admin/students/{id}", -1L))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	public void createWhenNameIsNullShouldReturnStatusCode400() throws Exception{
		Student student = new Student(3L, null, "sam@lotr.com");
		BDDMockito.when(studentRepository.save(student)).thenReturn(student);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/admin/students/", student, String.class);
		Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
	}

	@Test
	public void createShouldReturnStatusCode201() throws Exception{
		Student student = new Student(3L, "Sam", "sam@lotr.com");
		BDDMockito.when(studentRepository.save(student)).thenReturn(student);
		ResponseEntity<Student> responseEntity = restTemplate.postForEntity("/v1/admin/students/", student, Student.class);
		Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
		Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(3);
	}
}