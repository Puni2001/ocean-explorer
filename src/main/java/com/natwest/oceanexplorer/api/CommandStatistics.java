package com.natwest.oceanexplorer.api;

public class CommandStatistics {
    private long totalExecutions = 0;
    private long successfulExecutions = 0;
    private long totalExecutionTimeNanos = 0;

    public synchronized void recordExecution(boolean successful, long executionTimeNanos) {
        totalExecutions++;
        if (successful) {
            successfulExecutions++;
        }
        totalExecutionTimeNanos += executionTimeNanos;
    }

    public long getTotalExecutions() { return totalExecutions; }
    public long getSuccessfulExecutions() { return successfulExecutions; }
    public double getSuccessRate() {
        return totalExecutions > 0 ? (double) successfulExecutions / totalExecutions : 0.0;
    }
    public double getAverageExecutionTimeNanos() {
        return totalExecutions > 0 ? (double) totalExecutionTimeNanos / totalExecutions : 0.0;
    }
}