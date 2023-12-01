package SIN352.filipe.brener.resilience.dto;

import io.github.resilience4j.common.circuitbreaker.monitoring.endpoint.CircuitBreakerEndpointResponse;
import io.github.resilience4j.common.retry.monitoring.endpoint.RetryEventsEndpointResponse;

public class ResponseDto {

    private boolean success;

    private Object resilience;

    public ResponseDto(CircuitBreakerEndpointResponse allCircuitBreakers, boolean success) {
        this.resilience = allCircuitBreakers;
        this.success = success;
    }

    public ResponseDto(RetryEventsEndpointResponse allRetries, boolean success) {
        this.resilience = allRetries;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResilience() {
        return resilience;
    }

    public void setResilience(Object resilience) {
        this.resilience = resilience;
    }
}
