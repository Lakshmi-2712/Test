package com.example.document_processor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {


		// TODO Auto-generated method stub
		@GetMapping("/home")
	    @ResponseBody
	    public String home() {
	        return "Welcome to the Document Processor!";
	    }

	}


