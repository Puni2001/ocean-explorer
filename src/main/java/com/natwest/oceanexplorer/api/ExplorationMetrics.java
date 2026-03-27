package com.natwest.oceanexplorer.api;

import com.natwest.oceanexplorer.core.Command;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ExplorationMetrics {
    private final Map<Command, CommandStatistics> commandStats = new ConcurrentHashMap<>();
    private final AtomicLong totalCommands = new AtomicLong(0);
    private final AtomicLong failedCommands = new AtomicLong(0);
    private final AtomicLong invalidCommands = new AtomicLong(0);

    public void recordCommand(Command command, long executionTimeNanos) {
        commandStats.computeIfAbsent(command, k -> new CommandStatistics())
                .recordExecution(true, executionTimeNanos);
        totalCommands.incrementAndGet();
    }

    public void recordFailedCommand(Command command) {
        commandStats.computeIfAbsent(command, k -> new CommandStatistics())
                .recordExecution(false, 0);
        totalCommands.incrementAndGet();
        failedCommands.incrementAndGet();
    }

    public void recordInvalidCommand(String command) {
        invalidCommands.incrementAndGet();
    }

    public long getTotalCommandsExecuted() { return totalCommands.get(); }
    public double getSuccessRate() {
        long total = totalCommands.get();
        return total > 0 ? (double) (total - failedCommands.get()) / total : 0.0;
    }
    public Map<Command, CommandStatistics> getCommandStatistics() {
        return new ConcurrentHashMap<>(commandStats);
    }
}