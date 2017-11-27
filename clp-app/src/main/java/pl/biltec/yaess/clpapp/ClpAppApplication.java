package pl.biltec.yaess.clpapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"pl.biltec.yaess"})
@EnableJpaRepositories(basePackages = "pl.biltec.yaess")
@EntityScan(basePackages = "pl.biltec.yaess")
public class ClpAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClpAppApplication.class, args);
	}
}
