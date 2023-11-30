package SIN352.filipe.brener.resilience.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppPropertiesDTO {

    @Value("${host.url:http://host-inconsistente:3000}")
    private String url;

    @Value("${host.endpoint:/healthCheck}")
    private String hostEndpoint;

    @Value("${resilience4j.circuitbreaker.instances.sendRequest.minimunNumberOfCalls:4}")
    private int resilienceMinCalls;

    @Value("${resilience4j.circuitbreaker.instances.sendRequest.slidingWindowSize:8}")
    private int resilienceSlidingWindow;

    @Value("${circuit.breaker.wait.duration.open.state:15000}")
    private int implementedOpenStateTime;

    @Value("${circuit.breaker.min.of.calls:5}")
    private int implementedMaxFailure;

    @Value("${circuit.breaker.min.of.calls:5}")
    private int implementedMinSuccess;

    @Value("${retry.maxAttempts:5}")
    private int retryMaxAttempts;

    @Value("${retry.backoff:1000}")
    private int retryBackoff;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHostEndpoint() {
        return hostEndpoint;
    }

    public void setHostEndpoint(String hostEndpoint) {
        this.hostEndpoint = hostEndpoint;
    }

    public int getResilienceMinCalls() {
        return resilienceMinCalls;
    }

    public void setResilienceMinCalls(int resilienceMinCalls) {
        this.resilienceMinCalls = resilienceMinCalls;
    }

    public int getResilienceSlidingWindow() {
        return resilienceSlidingWindow;
    }

    public void setResilienceSlidingWindow(int resilienceSlidingWindow) {
        this.resilienceSlidingWindow = resilienceSlidingWindow;
    }

    public int getImplementedOpenStateTime() {
        return implementedOpenStateTime;
    }

    public void setImplementedOpenStateTime(int implementedOpenStateTime) {
        this.implementedOpenStateTime = implementedOpenStateTime;
    }

    public int getImplementedMaxFailure() {
        return implementedMaxFailure;
    }

    public void setImplementedMaxFailure(int implementedMaxFailure) {
        this.implementedMaxFailure = implementedMaxFailure;
    }

    public int getImplementedMinSuccess() {
        return implementedMinSuccess;
    }

    public void setImplementedMinSuccess(int implementedMinSuccess) {
        this.implementedMinSuccess = implementedMinSuccess;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public void setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public int getRetryBackoff() {
        return retryBackoff;
    }

    public void setRetryBackoff(int retryBackoff) {
        this.retryBackoff = retryBackoff;
    }
}
