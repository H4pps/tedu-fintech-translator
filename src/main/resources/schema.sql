CREATE TABLE IF NOT EXISTS Requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ip_address VARCHAR(255),
    input_text VARCHAR(255),
    translated_text VARCHAR(255)
);