package tedu.fintech.translator.services;

public interface TranslationService {
    String translate(String text, String sourceLanguage, String targetLanguage);
}