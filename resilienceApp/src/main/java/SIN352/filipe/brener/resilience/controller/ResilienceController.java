package SIN352.filipe.brener.resilience.controller;

import SIN352.filipe.brener.resilience.dto.ResponseDto;
import SIN352.filipe.brener.resilience.services.CircuitBreakerService;
import SIN352.filipe.brener.resilience.services.RetryService;
import io.github.resilience4j.springboot3.circuitbreaker.monitoring.endpoint.CircuitBreakerEndpoint;
import io.github.resilience4j.springboot3.retry.monitoring.endpoint.RetryEventsEndpoint;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/circuitBreaker")
    public ResponseDto circuitBreaker(HttpServletResponse response) {
        try {
            circuitBreaker.trySendRequest();
        } catch (Exception e){
            return new ResponseDto(circuitBreakerEndpoint.getAllCircuitBreakers(), false);
        }
        return new ResponseDto(circuitBreakerEndpoint.getAllCircuitBreakers(), true);
    }

    @GetMapping("/retry")
    public ResponseDto retry(HttpServletResponse response){
        try {
            retry.trySendRequest();
        } catch (Exception e){
            return new ResponseDto(retryEventsEndpoint.getAllRetryEvents(), false);
        }
        return new ResponseDto(retryEventsEndpoint.getAllRetryEvents(), true);
    }

}
