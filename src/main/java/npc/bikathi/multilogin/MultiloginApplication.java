package npc.bikathi.multilogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)
public class MultiloginApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiloginApplication.class, args);
	}

}
