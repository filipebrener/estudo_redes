package SIN352.filipe.brener.resilience.controller;

import SIN352.filipe.brener.resilience.dto.ResponseDto;
import SIN352.filipe.brener.resilience.services.CircuitBreakerService;
import SIN352.filipe.brener.resilience.services.RequestService;
import SIN352.filipe.brener.resilience.services.RetryService;
import io.github.resilience4j.springboot3.circuitbreaker.monitoring.endpoint.CircuitBreakerEndpoint;
import io.github.resilience4j.springboot3.retry.monitoring.endpoint.RetryEventsEndpoint;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resilience")
public class ResilienceController {

    @Autowired
    private CircuitBreakerService circuitBreaker;

    @Autowired
    private CircuitBreakerEndpoint circuitBreakerEndpoint;

    @Autowired
    private RetryEventsEndpoint retryEventsEndpoint;

    @Autowired
    private RetryService retry;

    @Autowired
    private RequestService request;

    @GetMapping("/circuitBreaker")
    public ResponseDto circuitBreaker(HttpServletResponse response, @RequestParam(name = "failureRate", required = false) Float failureRate) {
        try {
            circuitBreaker.trySendRequest(failureRate);
        } catch (Exception e){
            return new ResponseDto(circuitBreakerEndpoint.getAllCircuitBreakers(), false);
        }
        return new ResponseDto(circuitBreakerEndpoint.getAllCircuitBreakers(), true);
    }

    @GetMapping("/retry")
    public ResponseDto retry(HttpServletResponse response, @RequestParam(name = "failureRate", required = false) Float failureRate){
        try {
            retry.trySendRequest(failureRate);
        } catch (Throwable e){
            return new ResponseDto(retryEventsEndpoint.getAllRetryEvents(), false);
        }
        return new ResponseDto(retryEventsEndpoint.getAllRetryEvents(), true);
    }

    @GetMapping("/baseline")
    public ResponseEntity<String> baseline(@RequestParam(name = "failureRate", required = false) Float failureRate){
        request.trySendRequest(failureRate);
        return new ResponseEntity<>("Requisição enviada com sucesso!", HttpStatus.OK);
    }

}
