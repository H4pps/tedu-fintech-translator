package tedu.fintech.translator.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.jdbc.core.simple.JdbcClient;
import tedu.fintech.translator.domain.Translation;

import java.util.List;

@Repository
public class InMemoryTranslationRepository implements TranslationRepository {
    
    private final JdbcClient jdbcClient;

    public InMemoryTranslationRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void create(Translation request) {
        var updated = jdbcClient.sql("INSERT INTO Requests(ip_address, input_text, translated_text) VALUES (?, ?, ?)")
        .params(List.of(request.ipAddress(), request.inputText(), request.translatedText()))
        // .params(List.of(request.inputText(), request.translatedText()))
        .update();

        Assert.state(updated == 1, "Ошибка сохранения перевода");
    }
}