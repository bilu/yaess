package pl.biltec.yaess.clpapp.adapter.web;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
class TestController {


	@GetMapping("/test")
	String test() {

		return LocalDateTime.now().toString();
	}
}
