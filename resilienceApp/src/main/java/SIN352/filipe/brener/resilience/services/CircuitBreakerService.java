package SIN352.filipe.brener.resilience.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerService {

    @Autowired
    private RequestService request;

    @CircuitBreaker(name = "hostInconsistenteCB")
    public void trySendRequest() {
        request.send();
    }

}
