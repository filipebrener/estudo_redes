package SIN352.filipe.brener.resilience.dto;

import SIN352.filipe.brener.resilience.domain.CircuitBreaker;

import java.net.http.HttpResponse;

public class CircuitBreakerDto {

    private String state;

    private String successRate;

    private String failRate;

    private String timeToUnblock;

    private int serverStatusCode;

    private String serverResponseBody;

    private boolean isFallback;

    public CircuitBreakerDto(CircuitBreaker circuitBreaker, HttpResponse<String> response, boolean isFallback) {
        this.state = circuitBreaker.getState().name();
        this.successRate = String.format("%s/%s", circuitBreaker.getSuccessRequestsCounter(), circuitBreaker.getMinSuccessToClose());
        this.failRate = String.format("%s/%s", circuitBreaker.getFailedRequestsCounter(), circuitBreaker.getMaxFailureToOpen());
        this.timeToUnblock = CircuitBreaker.timeToUnblock();
        if(response != null){
            this.serverStatusCode = response.statusCode();
            this.serverResponseBody = response.body();
        }
        this.isFallback = isFallback;
    }

    public CircuitBreakerDto() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getFailRate() {
        return failRate;
    }

    public void setFailRate(String failRate) {
        this.failRate = failRate;
    }

    public String getTimeToUnblock() {
        return timeToUnblock;
    }

    public void setTimeToUnblock(String timeToUnblock) {
        this.timeToUnblock = timeToUnblock;
    }

    public int getServerStatusCode() {
        return serverStatusCode;
    }

    public void setServerStatusCode(int serverStatusCode) {
        this.serverStatusCode = serverStatusCode;
    }

    public String getServerResponseBody() {
        return serverResponseBody;
    }

    public void setServerResponseBody(String serverResponseBody) {
        this.serverResponseBody = serverResponseBody;
    }

    public boolean isFallback() {
        return isFallback;
    }

    public void setFallback(boolean fallback) {
        isFallback = fallback;
    }
}
