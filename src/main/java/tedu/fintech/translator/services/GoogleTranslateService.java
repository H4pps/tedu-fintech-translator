package tedu.fintech.translator.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleTranslateService implements TranslationService {
    private final RestTemplate restTemplate;
    private final String API_KEY = "AIzaSyD6E0pPRK7JpLYEYtrmv8Wf0Kozt5Eqyd4";

    public GoogleTranslateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String translate(String text, String sourceLanguage, String targetLanguage) {
        String url = "https://translation.googleapis.com/language/translate/v2?key=" + API_KEY
                + "&q=" + text
                + "&source=" + sourceLanguage
                + "&target=" + targetLanguage;
        String response = restTemplate.getForObject(url, String.class);
        // Assuming the response is in JSON format and we need to parse it to get the translated text
        // This is a simplified example and actual implementation might require more complex parsing
        // For example, using Jackson or Gson to parse JSON
        // Here, we'll assume the response is a simple JSON object with a "translatedText" field
        // {"translatedText": "translated text"}
        // In a real scenario, you would need to handle errors and edge cases properly
        return response.substring(response.indexOf("translatedText") + 14, response.length() - 1);
    }
}
