package SIN352.filipe.brener.resilience.services;

import SIN352.filipe.brener.resilience.domain.CircuitBreaker;
import SIN352.filipe.brener.resilience.dto.CircuitBreakerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static SIN352.filipe.brener.resilience.enums.Estado.OPEN;

@Service
public class CircuitBreakerService {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitBreakerService.class);

    private final HttpClient client = HttpClient.newBuilder().build();

    @Value("${host.url}")
    private String url;

    @Value("${host.endpoint}")
    private String endpoint;

    @Autowired
    private CircuitBreaker circuitBreaker;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    private HttpResponse<String> sendRequest() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(url + endpoint)).build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CircuitBreakerDto frameworkCircuitBreaker() {
        var cb = circuitBreakerFactory.create("circuitbreaker");
        return cb.run(() -> {
            HttpResponse<String> response = sendRequest();
            String result = "Resposta do servidor externo: " + response.body() + " --> Status code: " + response.statusCode();
            LOG.info(result);
            return new CircuitBreakerDto(new CircuitBreaker(), response, false);
        }, throwable -> fallback());
    }

    private CircuitBreakerDto fallback() {
        return new CircuitBreakerDto(new CircuitBreaker(), null, true);
    }

    public CircuitBreakerDto implementedCircuitBreaker() {
        if(!(circuitBreaker.getState() == OPEN)){
            try {
                HttpResponse<String> response = sendRequest();

                if (response.statusCode() == 200) {
                    circuitBreaker.addSuccessRequest();
                } else {
                    circuitBreaker.addFailedRequest();
                }

                return new CircuitBreakerDto(circuitBreaker, response, false);

            }catch(Exception e) {
                circuitBreaker.addFailedRequest();
            }
        }
        return implementedFallback();
    }

    private CircuitBreakerDto implementedFallback(){
        return new CircuitBreakerDto(circuitBreaker, null, true);
    }

}
