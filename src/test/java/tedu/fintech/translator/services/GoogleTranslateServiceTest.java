package tedu.fintech.translator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
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
    public void testTranslateWordSuccess() {
        String text = "hello";
        String sourceLanguage = "en";
        String targetLanguage = "es";
        String expectedResponse = "{\"data\":{\"translations\":[{\"translatedText\":\"hola\"}]}}";

        String url = constructUrl(text, sourceLanguage, targetLanguage);

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        String translatedText = googleTranslateService.translateWord(text, sourceLanguage, targetLanguage);

        assertEquals("hola", translatedText);
    }

    @Test
    public void testTranslateWordError() {
        String text = "hello";
        String sourceLanguage = "eng";
        String targetLanguage = "es";

        String url = constructUrl(text, sourceLanguage, targetLanguage);
        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        String translatedText = googleTranslateService.translateWord(text, sourceLanguage, targetLanguage);

        assertEquals("http 400 Передан неподдерживаемый язык", translatedText);
    }

    @Test
    public void testTranslateWordUnsupportedLanguage() {
        String text = "hello";
        String sourceLanguage = "en";
        String targetLanguage = "xx";

        String url = constructUrl(text, sourceLanguage, targetLanguage);
        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        String translatedText = googleTranslateService.translateWord(text, sourceLanguage, targetLanguage);

        assertEquals("http 400 Передан неподдерживаемый язык", translatedText);
    }

    @Test
    public void testTranslateEmptyString() {
        String text = "";
        String sourceLanguage = "en";
        String targetLanguage = "es";
        String expectedResponse = "{\"data\":{\"translations\":[{\"translatedText\":\"\"}]}}";

        String url = constructUrl(text, sourceLanguage, targetLanguage);

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        String translatedText = googleTranslateService.translateWord(text, sourceLanguage, targetLanguage);

        assertEquals("", translatedText);
    }

    @Test
    public void testTranslateNetworkError() {
        String text = "hello";
        String sourceLanguage = "en";
        String targetLanguage = "es";

        String url = constructUrl(text, sourceLanguage, targetLanguage);
        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        String translatedText = googleTranslateService.translateWord(text, sourceLanguage, targetLanguage);

        assertEquals("http 503 Ошибка сервера", translatedText);
    }

    @Test
    public void testTranslateServerError() {
        String text = "hello";
        String sourceLanguage = "en";
        String targetLanguage = "es";

        String url = constructUrl(text, sourceLanguage, targetLanguage);
        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        String translatedText = googleTranslateService.translateWord(text, sourceLanguage, targetLanguage);

        assertEquals("http 500 Ошибка сервера", translatedText);
    }

    private String constructUrl(String text, String sourceLanguage, String targetLanguage) {
        try {
            return "https://translation.googleapis.com/language/translate/v2?key=" + API_KEY
                    + "&q=" + URLEncoder.encode(text, StandardCharsets.UTF_8.toString())
                    + "&source=" + sourceLanguage + "&target=" + targetLanguage;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


}