package pl.biltec.yaess.clpapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pl.biltec.yaess"})
public class ClpAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClpAppApplication.class, args);
	}
}
