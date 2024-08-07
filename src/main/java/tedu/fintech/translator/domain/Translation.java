package tedu.fintech.translator.domain;

public record Translation(
        String ipAddress,
        String inputText,
        String translatedText) {
}