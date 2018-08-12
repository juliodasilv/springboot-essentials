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

import br.com.devdojo.awesome.error.CustomErrorType;
import br.com.devdojo.awesome.model.Student;
import br.com.devdojo.awesome.util.DateUtil;

@RestController
@RequestMapping("students")
public class StudentEndpoint {

	@SuppressWarnings("unused")
	private final DateUtil dateUtil;
	
	@Autowired
	public StudentEndpoint(DateUtil dateUtil) {
		this.dateUtil = dateUtil;
	}
	
	@GetMapping
	public ResponseEntity<?> listAll(){
		return new ResponseEntity<>(Student.studentList, HttpStatus.OK);
	}

	@GetMapping(path="/{id}")
	public ResponseEntity<?> findStudentById(@PathVariable("id") int id){
		int index = Student.studentList.indexOf(new Student(id));
		
		if(index == -1)
			return new ResponseEntity<>(new CustomErrorType("Student not found"), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(Student.studentList.get(index), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody Student student){
		Student.studentList.add(student);
		return new ResponseEntity<>(student, HttpStatus.CREATED);
	}

	@DeleteMapping
	public ResponseEntity<?> delete(@RequestBody Student student){
		Student.studentList.remove(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody Student student){
		Student.studentList.remove(student);
		Student.studentList.add(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}