package SIN352.filipe.brener.resilience.services;

import SIN352.filipe.brener.resilience.interfaces.ResilienceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class RequestService implements ResilienceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitBreakerService.class);

    private final HttpClient client = HttpClient.newBuilder().build();

    @Value("${host.url}")
    private String url;

    @Value("${host.endpoint}")
    private String endpoint;

    public void trySendRequest(Float failureRate) {
        String completeUri = String.format("%s%s?failureRate=%s", url, endpoint, failureRate);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(completeUri)).build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if( response.statusCode() != 200) throw new RuntimeException();
            LOG.info(String.format("MELHORAR ESSE LOG response: %s", response.body()));
        } catch (IOException | InterruptedException e) {
            LOG.error(String.format("MELHORAR ESSE LOG Error! failure_rate: %s",failureRate), e);
            throw new RuntimeException(e);
        }
    }

}
