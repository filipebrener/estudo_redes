package SIN352.filipe.brener.resilience.services;

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
public class RequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitBreakerService.class);

    private final HttpClient client = HttpClient.newBuilder().build();

    @Value("${host.url}")
    private String url;

    @Value("${host.endpoint}")
    private String endpoint;

    public void send() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + endpoint)).build();
        try {
            if(client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode() != 200) throw new RuntimeException();
            LOG.info("Sucesso ao se comunicar com o servidor!");
        } catch (IOException | InterruptedException e) {
            LOG.error("Erro ao se comunicar com o servidor!");
            throw new RuntimeException(e);
        }
    }

}
