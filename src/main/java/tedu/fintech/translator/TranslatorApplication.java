package tedu.fintech.translator;

import tedu.fintech.translator.services.GoogleTranslateService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class TranslatorApplication {
	private final Logger logger = LoggerFactory.getLogger(TranslatorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(TranslatorApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(GoogleTranslateService googleTranslateService) {
		return args -> {
			// logger.info(googleTranslateService.translate("Hello world, this is my first program", "en", "es"));
			logger.info(googleTranslateService.translate("Hello world, this is my first program", "en", "ru"));
		};
	}
}
