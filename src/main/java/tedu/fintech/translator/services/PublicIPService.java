package tedu.fintech.translator.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PublicIPService {

    private final RestTemplate restTemplate;

    public PublicIPService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getPublicIP() {
        String url = "http://checkip.amazonaws.com";
        try {
            String response = restTemplate.getForObject(url, String.class);
            return response != null ? response.trim() : "Error fetching public IP";
        } catch (Exception e) {
            return "Error fetching public IP";
        }
    }
}