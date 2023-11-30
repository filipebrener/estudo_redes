package SIN352.filipe.brener.resilience.controller;

import SIN352.filipe.brener.resilience.dto.CircuitBreakerDto;
import SIN352.filipe.brener.resilience.services.CircuitBreakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/circuitBreaker")
public class CircuitBreakerController {

    @Autowired
    private CircuitBreakerService circuitBreaker;

    @GetMapping("/framework")
    public CircuitBreakerDto frameworkCircuitBreaker() throws IOException, InterruptedException {
        return circuitBreaker.frameworkCircuitBreaker();
    }

    @GetMapping("/implemented")
    public CircuitBreakerDto implemented_circuitBreaker() {
        return circuitBreaker.implementedCircuitBreaker();
    }

}
