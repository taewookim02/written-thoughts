package com.written.app;

//import org.mybatis.spring.annotation.MapperScan;
import com.written.app.config.JwtProperties;
import com.written.app.model.Entry;
import com.written.app.repository.EntryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
//@MapperScan
//@EnableConfigurationProperties(JwtProperties.class)
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(EntryRepository entryRepository) {
		return args -> {
			// dummy entry
			var entry = Entry.builder()
					.isPrivate(false)
					.createdAt(LocalDateTime.now())
					.content("today\n" +
							"lorem\n" +
							"ipsum")
					.title("title01")
					.build();
			entryRepository.save(entry);
		};
	}
}
