package SIN352.filipe.brener.resilience.interfaces;

public interface ResilienceInterface {
    void trySendRequest(Float failureRate) throws Throwable;

}
