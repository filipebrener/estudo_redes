package SIN352.filipe.brener.resilience.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class RetryService {

    private static final Logger LOG = LoggerFactory.getLogger(RetryService.class);

    @Value("${retry.maxAttempts}")
    private Integer maxAttempts;

    @Value("${retry.backoff}")
    private Integer backoff;

    private final HttpClient client = HttpClient.newBuilder().build();

    @Value("${host.url}")
    private String host;

    @Value("${host.endpoint}")
    private String endpoint;

    private HttpResponse<String> sendRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(host + endpoint)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.backoff}"))
    public String frameworkRetry() throws IOException, InterruptedException {
        HttpResponse<String> response = sendRequest();
        String result = "Resposta do servidor externo: " + response.body() + " --> Status code: " + response.statusCode();
        LOG.info(result);
        return result;
    }

    @Recover
    public String recover(){
        return getErrorMsg();
    }

    public String implementedRetry() throws InterruptedException {
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                HttpResponse<String> response = sendRequest();
                String result = "Resposta do servidor externo: " + response.body() + " --> Status code: " + response.statusCode();
                LOG.info(result);
                return result;
            } catch (IOException | InterruptedException e) {
                // Tratamento de exceção, se necessário
                LOG.error("Erro na tentativa " + (attempts + 1) + " de comunicar com o servidor externo!");
            }

            attempts++;
            Thread.sleep(backoff);
        }

        return getErrorMsg();
    }

    private String getErrorMsg(){
        return String.format("Número máximo de %s tentativas alcançado. Falha na requisição.", maxAttempts);
    }

}
