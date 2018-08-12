package br.com.devdojo.awesome.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.awesome.error.ResourceNotFoundException;
import br.com.devdojo.awesome.model.Student;
import br.com.devdojo.awesome.repository.StudentRepository;

@RestController
@RequestMapping("students")
public class StudentEndpoint {

	private final StudentRepository studentDAO;
	
	@Autowired
	public StudentEndpoint(StudentRepository studentDAO) {
		this.studentDAO = studentDAO;
	}
	
	@GetMapping
	public ResponseEntity<?> listAll(){
		return new ResponseEntity<>(studentDAO.findAll(), HttpStatus.OK);
	}

	@GetMapping(path="/{id}")
	public ResponseEntity<?> findStudentById(@PathVariable("id") Long id){
		Student student = studentDAO.findOne(id);
		verifyIfStudentExists(student, id);
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody Student student){
		return new ResponseEntity<>(studentDAO.save(student), HttpStatus.CREATED);
	}

	@DeleteMapping(path="/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id){
		verifyIfStudentExists(studentDAO.findOne(id), id);
		studentDAO.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody Student student){
		verifyIfStudentExists(studentDAO.findOne(student.getId()), student.getId());
		studentDAO.save(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(path="/findByName/{name}")
	public ResponseEntity<?> findStudentsByName(@PathVariable("name") String name) {
		return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
	}

	private Student verifyIfStudentExists(Student student, Long id) {
		if(student == null)
			throw new ResourceNotFoundException("Student not found for ID: " + id);
		return student;
	}
}