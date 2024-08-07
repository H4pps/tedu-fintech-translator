package tedu.fintech.translator.services;

public interface TranslationService {
    String translate(String text, String sourceLanguage, String targetLanguage);
    String request(String text, String sourceLanguage, String targetLanguage);
}