package SIN352.filipe.brener.resilience.services;

import SIN352.filipe.brener.resilience.dto.BenchmarkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class BenchmarkService {

    @Autowired
    private RequestService request;

    @Autowired
    private CircuitBreakerService circuitBreaker;

    @Autowired
    private RetryService retry;

    // TODO: criar todas as entradas pelo controller, montar um objeto e passar para o startBench
    // TODO: adicionar na resposta do benchmark, quais configurações foram usadas
    // TODO: refatorar quais casos são retornados, hoje é o melhor ou pior, deve ser retornado os dois em uma lista
    @Value("${benchmark.min.success:25}")
    private int numMinSuccess;

    @Value("${benchmark.max.requests:1000}")
    private int maxNumRequests;

    @Value("${benchmark.max.fail.requests.inARow:5}")
    private int maxRequestsInARow;

    public List<BenchmarkDto> startBench(boolean lowerCase, int[] numOfThreadsList, Float[] serverFailRates) {
        List<BenchmarkDto> benchmarks = new ArrayList<>();

        Map<String, Consumer<Float>> tests = Map.of(
                "Circuit Breaker", failureRate -> circuitBreaker.trySendRequest(failureRate),
                "Retry", failureRate -> retry.trySendRequest(failureRate),
                "Baseline", failureRate -> request.trySendRequest(failureRate)
        );

        for (Map.Entry<String, Consumer<Float>> test : tests.entrySet()) {
            for (float failureRate : serverFailRates) {
                for (int numThreads : numOfThreadsList) {
                    benchmarks.add(runBenchmark(test, failureRate, numThreads, lowerCase));
                }
            }
        }

        return benchmarks;
    }

    private BenchmarkDto runBenchmark(Map.Entry<String, Consumer<Float>> test, float failureRate, int numThreads, boolean lowerCase) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        List<BenchmarkDto> benchmarks = new ArrayList<>();

        try (ExecutorService executorService = Executors.newFixedThreadPool(numThreads)) {
            for (int i = 0; i < numThreads; i++) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    TempoExecucaoCalc exec = new TempoExecucaoCalc();

                    int localSuccessCounter = 0;
                    int localFailsCounter = 0;
                    int localMaxConsecutiveErrors = 0;
                    int currentConsecutiveErrors = 0;

                    exec.start();
                    while (localSuccessCounter < numMinSuccess && (localSuccessCounter + localFailsCounter) < maxNumRequests) {
                        try {
                            test.getValue().accept(failureRate);
                            localSuccessCounter++;
                            currentConsecutiveErrors = 0;
                        } catch (Throwable e) {
                            localFailsCounter++;
                            localMaxConsecutiveErrors = Math.max(++currentConsecutiveErrors, localMaxConsecutiveErrors);
                        }
                    }
                    exec.finish();
                    benchmarks.add(new BenchmarkDto(test.getKey(), failureRate, numThreads, exec.getExecutionTime(),
                            localSuccessCounter, localFailsCounter, localMaxConsecutiveErrors));
                }, executorService);
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

        return new BenchmarkDto(benchmarks, lowerCase);
    }

}

