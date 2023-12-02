package SIN352.filipe.brener.resilience.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public class BenchmarkDto {

    private String label;

    private float failureRate;

    private int numThreads;

    private String executionTime;

    private int successCounter;

    private int errorCounter;

    private int maxConsecutiveErrors;

    @JsonIgnore
    private Duration duration;

    private static String formatDuration(Duration duration) {
        long minutos = (duration.toHoursPart() * 60L)  + duration.toMinutesPart();
        long segundos = duration.toSecondsPart();
        long milissegundos = duration.toMillisPart();

        return String.format("%d:%02d:%06d", minutos, segundos, milissegundos);
    }

    public BenchmarkDto(String name, float failureRate, int numThreads, Duration executionTime, int successCounter, int errorCounter, int maxConsecutiveErrors){
        this.label = String.format("%s | Taxa de falha: %s%% | %s threads", name, (failureRate * 100), numThreads);
        this.numThreads = numThreads;
        this.failureRate = failureRate;
        this.executionTime = formatDuration(executionTime);
        this.duration = executionTime;
        this.successCounter = successCounter;
        this.errorCounter = errorCounter;
        this.maxConsecutiveErrors = maxConsecutiveErrors;
    }

    public BenchmarkDto(List<BenchmarkDto> benchmarkList, boolean lowerCase) {
        if (benchmarkList != null && !benchmarkList.isEmpty()) {
            this.label = benchmarkList.get(0).label;
            this.numThreads = benchmarkList.get(0).numThreads;
            this.failureRate = benchmarkList.get(0).getFailureRate();
            BenchmarkDto benchmark;
            if(lowerCase){
                benchmark = benchmarkList.stream()
                        .max(Comparator.comparing(BenchmarkDto::getDuration)).orElseThrow();
            } else {
                benchmark = benchmarkList.stream()
                        .min(Comparator.comparing(BenchmarkDto::getDuration)).orElseThrow();
            }
            this.executionTime = benchmark.getExecutionTime();
            this.duration = benchmark.getDuration();
            this.successCounter = benchmark.getSuccessCounter();
            this.errorCounter = benchmark.getErrorCounter();
            this.maxConsecutiveErrors = benchmark.getMaxConsecutiveErrors();

        }
    }

    public float getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(float failureRate) {
        this.failureRate = failureRate;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public int getSuccessCounter() {
        return successCounter;
    }

    public int getErrorCounter() {
        return errorCounter;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setSuccessCounter(int successCounter) {
        this.successCounter = successCounter;
    }

    public void setErrorCounter(int errorCounter) {
        this.errorCounter = errorCounter;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getFailRate() {
        return failureRate;
    }

    public void setFailRate(float failureRate) {
        this.failureRate = failureRate;
    }

    public int getMaxConsecutiveErrors() {
        return maxConsecutiveErrors;
    }

    public void setMaxConsecutiveErrors(int maxConsecutiveErrors) {
        this.maxConsecutiveErrors = maxConsecutiveErrors;
    }

    public Duration getDuration(){
        return this.duration;
    }

}
