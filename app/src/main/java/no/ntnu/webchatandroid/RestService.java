package no.ntnu.webchatandroid;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class RestService {
    public boolean verifyVerificationCode(String verificationCode, String email) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder url = new StringBuilder();
        url.append("http://localhost:8081");
        url.append("/verifymember");
        url.append("?email=").append(email);
        url.append("&hash=").append(verificationCode);

        return getResponseFromRestCall(restTemplate, url);
    }

    private boolean getResponseFromRestCall(RestTemplate restTemplate, StringBuilder url) {
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(url.toString(),
                    HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), Boolean.class);
            return response.getBody();
        } catch (ResourceAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
