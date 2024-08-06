package tedu.fintech.translator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
@SpringBootTest
public class GoogleTranslateServiceTest {

    @Autowired
    private GoogleTranslateService googleTranslateService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private final String API_KEY = "AIzaSyD6E0pPRK7JpLYEYtrmv8Wf0Kozt5Eqyd4";

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testSingleWordEnToEs() {
        String text = "Hello";
        String sourceLanguage = "en";
        String targetLanguage = "es";
        String expectedResponse = "{\"data\":{\"translations\":[{\"translatedText\":\"Hola\"}]}}";

        String url = constructUrl(text, sourceLanguage, targetLanguage);

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        String translatedText = googleTranslateService.translate(text, sourceLanguage, targetLanguage);

        assertEquals("http 200 Hola", translatedText);
    }

    @Test
    public void testSingleWordRuToEn() {
        String text = "привет";
        String sourceLanguage = "ru";
        String targetLanguage = "en";
        String expectedResponse = "{\"data\":{\"translations\":[{\"translatedText\":\"hello\"}]}}";

        String url = constructUrl(text, sourceLanguage, targetLanguage);

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        String translatedText = googleTranslateService.translate(text, sourceLanguage, targetLanguage);

        assertEquals("http 200 hello", translatedText);
    }


    @Test
    public void bigTestEnToRu() {
        String text = "Hello world, this is my first program";
        String sourceLanguage = "en";
        String targetLanguage = "ru";
        String expectedResponse = "{\"data\":{\"translations\":[{\"translatedText\":\"Привет мир, это является мой первый программа\"}]}}";

        String url = constructUrl(text, sourceLanguage, targetLanguage);

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        String translatedText = googleTranslateService.translate(text, sourceLanguage, targetLanguage);
        System.out.println(translatedText);

        assertEquals("http 200 Привет мир, это является мой первый программа", translatedText);
    }
    
    @Test
    public void bigTestRuToEn() {
        String text = "Привет мир, это является мой первый программа";
        String sourceLanguage = "ru";
        String targetLanguage = "en";
        String expectedResponse = "{\"data\":{\"translations\":[{\"translatedText\":\"Hello world, this is my first program\"}]}}";

        String url = constructUrl(text, sourceLanguage, targetLanguage);

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        String translatedText = googleTranslateService.translate(text, sourceLanguage, targetLanguage);

        assertEquals("http 200 Hello world, this is my first program", translatedText);
    }

    @Test
    public void testUnsupportedSourceLanguage() {
        String text = "Hello world, this is my first program";
        String sourceLanguage = "unsupported";
        String targetLanguage = "en";

        String url = constructUrl(text, sourceLanguage, targetLanguage);
        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        String translatedText = googleTranslateService.translate(text, sourceLanguage, targetLanguage);

        assertEquals("http 400 Передан неподдерживаемый язык", translatedText);
    }

    @Test
    public void testUnsupportedTargetLanguage() {
        String text = "Hello world, this is my first program";
        String sourceLanguage = "en";
        String targetLanguage = "unsupported";

        String url = constructUrl(text, sourceLanguage, targetLanguage);
        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        String translatedText = googleTranslateService.translate(text, sourceLanguage, targetLanguage);

        assertEquals("http 400 Передан неподдерживаемый язык", translatedText);
    }

    @Test
    public void testApiAccessDenied() {
        String text = "Hello world, this is my first program";
        String sourceLanguage = "en";
        String targetLanguage = "es";

        String url = constructUrl(text, sourceLanguage, targetLanguage);
        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.FORBIDDEN));

        String translatedText = googleTranslateService.translate(text, sourceLanguage, targetLanguage);

        assertEquals("http 403 Доступ к API запрещен", translatedText);
    }

    @Test
    public void testInternalServerError() {
        String text = "Hello world, this is my first program";
        String sourceLanguage = "en";
        String targetLanguage = "es";

        String url = constructUrl(text, sourceLanguage, targetLanguage);
        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        String translatedText = googleTranslateService.translate(text, sourceLanguage, targetLanguage);

        assertEquals("http 500 Ошибка сервера", translatedText);
    }

    private String constructUrl(String text, String sourceLanguage, String targetLanguage) {
        try {
            StringBuilder encodedText = new StringBuilder();
            for (char c : text.toCharArray()) {
                if (Character.isLetter(c)) {
                    encodedText.append(URLEncoder.encode(String.valueOf(c), StandardCharsets.UTF_8.toString()));
                } else if (Character.isSpaceChar(c)) {
                    encodedText.append("%20");
                } else {
                    encodedText.append(c);
                }
            }
            return "https://translation.googleapis.com/language/translate/v2?key=" + API_KEY
                    + "&q=" + encodedText.toString() + "&source=" + sourceLanguage + "&target=" + targetLanguage;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}