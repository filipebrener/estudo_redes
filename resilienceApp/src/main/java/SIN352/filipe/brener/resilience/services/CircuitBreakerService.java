package SIN352.filipe.brener.resilience.services;

import SIN352.filipe.brener.resilience.interfaces.ResilienceInterface;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CircuitBreakerService implements ResilienceInterface {

    @Autowired
    private RequestService request;

    @CircuitBreaker(name = "hostInconsistenteCB")
    public void trySendRequest(Float failureRate) {
        request.trySendRequest(failureRate);
    }

}
