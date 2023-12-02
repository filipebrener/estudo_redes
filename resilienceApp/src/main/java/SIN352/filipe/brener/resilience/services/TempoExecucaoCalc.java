package SIN352.filipe.brener.resilience.services;

import java.time.Duration;
import java.time.LocalDateTime;

public class TempoExecucaoCalc {

    private LocalDateTime tempoInicio;
    private LocalDateTime tempoFim;
    private Duration tempoExecucao;

    public void start() {
        tempoInicio = LocalDateTime.now();
        tempoFim = null;
        tempoExecucao = null;
    }

    public void finish() {
        tempoFim = LocalDateTime.now();
        if (tempoInicio != null) {
            tempoExecucao = Duration.between(tempoInicio, tempoFim);
        } else {
            throw new IllegalStateException("O tempo de início e não foi definido corretamente.");
        }
    }

    public Duration getExecutionTime() {
        return tempoExecucao;
    }
}
