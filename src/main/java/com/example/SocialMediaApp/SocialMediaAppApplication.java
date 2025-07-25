package com.example.SocialMediaApp;

import com.example.SocialMediaApp.dto.PreferenceType;
import com.example.SocialMediaApp.repository.PreferenceTypeRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
@EnableScheduling
@SpringBootApplication
public class SocialMediaAppApplication {

	public static void main(String[] args) {
		// Load .env file only in local development
		if (System.getenv("RENDER") == null) {
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();

			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});
		}
		SpringApplication.run(SocialMediaAppApplication.class, args);
	}
	@Bean
	@Transactional
	CommandLineRunner loadPreferences(PreferenceTypeRepository repo) {
		return args -> {
			List<String> defaultPrefs = List.of("Nature", "Animals", "Technology", "Art", "Travel", "Food","Cars","Bikes");
			for (String name : defaultPrefs) {
				if (repo.findByName(name).isEmpty()) {
					repo.save(new PreferenceType(null, name));
				}
			}
		};
	}
}
