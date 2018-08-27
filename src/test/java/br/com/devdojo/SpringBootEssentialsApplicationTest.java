package br.com.devdojo;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SpringBootEssentialsApplicationTest {

	@Autowired
	private StudentRepository studentRepository;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createShouldPersistData() {
		Student student = new Student("Ze Luis", "zeluis@gmail.com");
		this.studentRepository.save(student);
		Assertions.assertThat(student.getId()).isNotNull();
		Assertions.assertThat(student.getName()).isNotNull();
	}

	@Test
	public void updateShouldSaveAndPersistData() {
		Student student = new Student("Ze Luis", "zeluis@gmail.com");
		this.studentRepository.save(student);
		Assertions.assertThat(student.getName()).isEqualTo("Ze Luis");
		Assertions.assertThat(student.getEmail()).isEqualTo("zeluis@gmail.com");

		student.setName("Zé Luís");
		student.setEmail("zeluis@bol.com");
		this.studentRepository.save(student);

		student = studentRepository.findOne(student.getId());

		Assertions.assertThat(student.getName()).isEqualTo("Zé Luís");
		Assertions.assertThat(student.getEmail()).isEqualTo("zeluis@bol.com");
	}

	@Test
	public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
		Student student = new Student("ze luis", "zeluis@gmail.com");
		Student student2 = new Student("ZE LUIS", "zeluis@gmail.com");
		this.studentRepository.save(student);
		this.studentRepository.save(student2);

		List<Student> studentList = studentRepository.findByNameIgnoreCaseContaining("ze luis");

		Assertions.assertThat(studentList.size()).isEqualTo(2);
	}

	@Test
	public void deleteShouldRemoveData() {
		Student student = new Student("Zé Luis", "zeluis@bol.com");
		this.studentRepository.save(student);
		this.studentRepository.delete(student);
		Assertions.assertThat(studentRepository.findOne(student.getId())).isNull();
	}

	@Test
	public void createWhenNameIsNullShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("O campo nome do estudante é obrigatório");
		this.studentRepository.save(new Student());
	}

	@Test
	public void createWhenEmailIsNullShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		Student student = new Student();
		student.setName("William");
		this.studentRepository.save(student);
	}

	@Test
	public void createWhenEmailIsNotValidShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("Digite um email válido");
		Student student = new Student();
		student.setName("William");
		student.setEmail("William");
		this.studentRepository.save(student);
	}

}