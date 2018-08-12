package br.com.devdojo.awesome.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<?> listAll(){
		return new ResponseEntity<>(Student.studentList, HttpStatus.OK);
	}

	@RequestMapping(method=RequestMethod.GET, path="/{id}")
	public ResponseEntity<?> findStudentById(@PathVariable("id") int id){
		int index = Student.studentList.indexOf(new Student(id));
		
		if(index == -1)
			return new ResponseEntity<>(new CustomErrorType("Student not found"), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(Student.studentList.get(index), HttpStatus.OK);
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody Student student){
		Student.studentList.add(student);
		return new ResponseEntity<>(student, HttpStatus.CREATED);
	}
}