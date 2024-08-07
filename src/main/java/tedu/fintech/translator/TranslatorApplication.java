package tedu.fintech.translator;

import tedu.fintech.translator.services.GoogleTranslateService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

@SpringBootApplication
public class TranslatorApplication {
    private final Logger logger = LoggerFactory.getLogger(TranslatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TranslatorApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(GoogleTranslateService googleTranslateService) {
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("Введите исходный язык: ");
                    String sourceLanguage = scanner.nextLine();

                    System.out.print("Введите целевой язык: ");
                    String targetLanguage = scanner.nextLine();

                    System.out.print("Введите текст для перевода: ");
                    String text = scanner.nextLine();

                    String translationResult = googleTranslateService.request(text, sourceLanguage, targetLanguage);
                    System.out.println("Перевод: " + translationResult);

                    System.out.print("Завершить приложение? (y/n): ");
                    String finish = scanner.nextLine();
                    if (finish.equalsIgnoreCase("y")) {
                        break;
                    }
                }
            }
            System.exit(0);
        };
    }
}