package SIN352.filipe.brener.resilience.services;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    @Autowired
    private RequestService request;
    @Retry(name = "hostInconsistente")
    public void trySendRequest() {
        request.send();
    }

}
