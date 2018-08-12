package br.com.devdojo.awesome.endpoint;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.awesome.model.Student;
import br.com.devdojo.awesome.util.DateUtil;

@RestController
@RequestMapping("student")
public class StudentEndpoint {

	@Autowired
	private DateUtil dateUtil;
	
	@RequestMapping(method=RequestMethod.GET, path="/list")
	public List<Student> listAll(){
		return Arrays.asList(new Student("Deku"), new Student("Todoroki"));
	}
	
}
