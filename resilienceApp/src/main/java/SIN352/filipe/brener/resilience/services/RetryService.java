package SIN352.filipe.brener.resilience.services;

import SIN352.filipe.brener.resilience.interfaces.ResilienceInterface;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.stereotype.Service;
import static io.github.resilience4j.core.IntervalFunction.ofExponentialBackoff;

@Service
public class RetryService implements ResilienceInterface {

    private final RequestService request;
    private final Retry retryPolicy;

    public RetryService(RequestService request) {
        this.request = request;
        this.retryPolicy = createRetryWithExponentialBackoff();
    }

    private Retry createRetryWithExponentialBackoff() {
        var retryConfig = RetryConfig.from(RetryConfig.ofDefaults())
                .maxAttempts(5)
                .intervalFunction(ofExponentialBackoff(100, 1.5f))
                .build();
        return RetryRegistry.of(retryConfig).retry("hostInconsistenteRetry");
    }

    public void trySendRequest(Float failureRate) {
        try {
            Retry.decorateCheckedSupplier(retryPolicy, () -> {
                request.trySendRequest(failureRate);
                return null;
            }).get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
