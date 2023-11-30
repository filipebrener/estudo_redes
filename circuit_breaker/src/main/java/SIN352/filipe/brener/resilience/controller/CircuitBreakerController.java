package SIN352.filipe.brener.resilience.controller;

import SIN352.filipe.brener.resilience.dto.CircuitBreakerDto;
import SIN352.filipe.brener.resilience.services.CircuitBreakerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static SIN352.filipe.brener.resilience.enums.Estado.OPEN;

@RestController
@RequestMapping("/circuitBreaker")
public class CircuitBreakerController {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitBreakerController.class);

    private final HttpClient client = HttpClient.newBuilder().build();

    @Value("${host.and.port}")
    private String host;

    @Value("${host.endpoint}")
    private String endpoint;

    @Autowired
    private CircuitBreakerService circuitBreaker;

    private HttpResponse<String> sendRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(host + endpoint)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @GetMapping("/framework")
    @CircuitBreaker(name = "sendRequest", fallbackMethod = "fallback")
    public String framework_circuitBreaker() throws IOException, InterruptedException {
        HttpResponse<String> response = sendRequest();
        String result = "Resposta do servidor externo: " + response.body() + " --> Status code: " + response.statusCode();
        LOG.info(result);
        return result;
    }

    public String fallback(Throwable throwable) {
        LOG.error("fallback ativado");
        return "fallback ativado!";
    }

    @GetMapping("/implemented")
    public CircuitBreakerDto implemented_circuitBreaker() {
        if(!(circuitBreaker.getState() == OPEN)){
            try {
                HttpResponse<String> response = sendRequest();

                if (response.statusCode() == 200) {
                    circuitBreaker.addSuccessRequest();
                } else {
                    circuitBreaker.addFailedRequest();
                }

                return new CircuitBreakerDto(circuitBreaker, response, false);

            }catch(InterruptedException | IOException e) {
                circuitBreaker.addFailedRequest();
            }
        }
        return implementedFallback();
    }

    private CircuitBreakerDto implementedFallback(){
        return new CircuitBreakerDto(circuitBreaker, null, true);
    }

}
