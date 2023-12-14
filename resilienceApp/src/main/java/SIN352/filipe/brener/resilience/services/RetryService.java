package SIN352.filipe.brener.resilience.services;

import SIN352.filipe.brener.resilience.interfaces.ResilienceInterface;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class RetryService implements ResilienceInterface {

    private final RequestService request;

    public RetryService(RequestService request) {
        this.request = request;
    }

    @Retry(name = "hostInconsistenteRetry")
    public void trySendRequest(Float failureRate) {
        request.trySendRequest(failureRate);
    }
}
