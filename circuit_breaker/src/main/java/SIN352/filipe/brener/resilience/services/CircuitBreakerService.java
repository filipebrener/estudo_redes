package SIN352.filipe.brener.resilience.services;

import SIN352.filipe.brener.resilience.enums.Estado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static SIN352.filipe.brener.resilience.enums.Estado.*;

@Service
public class CircuitBreakerService {

    private static Estado estado = CLOSED;

    private static int failedRequestsCounter;

    private static int successRequestsCounter;

    @Value("${implemented.circuitbreaker.openStateTime}")
    private int openStateTime;

    @Value("${implemented.circuitbreaker.maxFailureToOpen}")
    private int maxFailureToOpen;

    @Value("${implemented.circuitbreaker.minSuccessToClose}")
    private int minSuccessToClose;

    private static Instant blockedUntil;

    public Estado getState(){
        if(!isBlocked() && estado == OPEN){
            halfOpenState();
        }
        return estado;
    }

    public void closeState(){
        estado = CLOSED;
        failedRequestsCounter = 0;
    }

    public void openState(){
        estado = OPEN;
        successRequestsCounter = 0;
    }

    public void halfOpenState(){
        estado = HALF_OPEN;
    }

    public void addFailedRequest(){
        failedRequestsCounter++;
        if(failedRequestsCounter >= maxFailureToOpen){
            blockedUntil = Instant.now().plusMillis(openStateTime);
            openState();
        }
    }

    public void addSuccessRequest(){
        successRequestsCounter++;
        if(estado == OPEN){
            halfOpenState();
        } else if(estado == CLOSED){
            failedRequestsCounter = 0;
        } else if(successRequestsCounter > minSuccessToClose){
            closeState();
        }
    }

    public boolean isBlocked(){
        if(blockedUntil != null && blockedUntil.isBefore(Instant.now())){
            blockedUntil = null;
        }
        return blockedUntil != null;
    }

    public String getInfo(){
        return String.format("state: %s\nsuccess: %s/%s\nfailed: %s/%s\ntime to unblock: %s", estado,
                successRequestsCounter, minSuccessToClose, failedRequestsCounter, maxFailureToOpen,
                blockedUntil != null ? Duration.between(Instant.now(), blockedUntil).toMillis() : "not blocked");
    }

    public static String getFailedRequestsCounter() {
        return String.valueOf(failedRequestsCounter);
    }

    public static String getSuccessRequestsCounter() {
        return String.valueOf(successRequestsCounter);
    }

    public String getMaxFailureToOpen() {
        return String.valueOf(maxFailureToOpen);
    }

    public String getMinSuccessToClose() {
        return String.valueOf(minSuccessToClose);
    }

    public static String timeToUnblock() {
        return blockedUntil != null ? String.valueOf(Duration.between(Instant.now(), blockedUntil).toMillis()) : "not blocked";
    }


}
