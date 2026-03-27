// ExplorationReport.java - Rich reporting capabilities
package com.natwest.oceanexplorer.api;

import com.natwest.oceanexplorer.core.Position;
import com.natwest.oceanexplorer.core.Command;

import java.util.*;
import java.util.stream.Collectors;


public class ExplorationReport {
    private final String probeId;
    private final List<Position> allVisitedPositions;
    private final List<Position> currentPath;
    private final Map<Command, CommandStatistics> commandStats;
    private final Date timestamp;
    private final Map<String, Object> analytics;

    public ExplorationReport(String probeId, List<Position> visitedPositions,
                             List<Position> currentPath, Map<Command, CommandStatistics> commandStats,
                             Date timestamp) {
        this.probeId = probeId;
        this.allVisitedPositions = new ArrayList<>(visitedPositions);
        this.currentPath = new ArrayList<>(currentPath);
        this.commandStats = new HashMap<>(commandStats);
        this.timestamp = timestamp;
        this.analytics = calculateAnalytics();
    }

    private Map<String, Object> calculateAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        // Unique positions visited
        Set<Position> uniquePositions = new HashSet<>(allVisitedPositions);
        analytics.put("uniquePositionsVisited", uniquePositions.size());

        // Exploration efficiency
        analytics.put("explorationEfficiency",
                (double) uniquePositions.size() / allVisitedPositions.size());

        // Distance traveled
        double totalDistance = calculateTotalDistance();
        analytics.put("totalDistanceTraveled", totalDistance);

        // Revisits
        analytics.put("revisitCount", allVisitedPositions.size() - uniquePositions.size());

        // Command success rate
        if (!commandStats.isEmpty()) {
            long totalCommands = commandStats.values().stream()
                    .mapToLong(CommandStatistics::getTotalExecutions)
                    .sum();
            long successfulCommands = commandStats.values().stream()
                    .mapToLong(CommandStatistics::getSuccessfulExecutions)
                    .sum();
            analytics.put("commandSuccessRate",
                    totalCommands > 0 ? (double) successfulCommands / totalCommands : 0.0);
        }

        return analytics;
    }

    private double calculateTotalDistance() {
        double distance = 0.0;
        for (int i = 1; i < allVisitedPositions.size(); i++) {
            Position prev = allVisitedPositions.get(i - 1);
            Position curr = allVisitedPositions.get(i);
            distance += Math.sqrt(
                    Math.pow(curr.getX() - prev.getX(), 2) +
                            Math.pow(curr.getY() - prev.getY(), 2)
            );
        }
        return distance;
    }

    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("EXPLORATION REPORT - Probe ").append(probeId).append("\n");
        sb.append("Generated: ").append(timestamp).append("\n");
        sb.append("========================================\n\n");

        sb.append("EXPLORATION STATISTICS:\n");
        sb.append("-----------------------\n");
        analytics.forEach((key, value) ->
                sb.append(String.format("%-30s: %s\n",
                        formatKey(key), value))
        );

        sb.append("\nCOMMAND STATISTICS:\n");
        sb.append("-------------------\n");
        commandStats.forEach((cmd, stats) ->
                sb.append(String.format("%-10s - Executions: %d, Success Rate: %.2f%%\n",
                        cmd, stats.getTotalExecutions(), stats.getSuccessRate() * 100))
        );

        sb.append("\nEXPLORATION PATH (last 10 positions):\n");
        sb.append("------------------------------------\n");
        currentPath.stream()
                .skip(Math.max(0, currentPath.size() - 10))
                .forEach(pos -> sb.append("  ").append(pos).append("\n"));

        sb.append("\n========================================\n");
        return sb.toString();
    }

    private String formatKey(String key) {
        return key.replaceAll("([A-Z])", " $1")
                .toLowerCase()
                .trim();
    }


    public String getProbeId() { return probeId; }
    public List<Position> getAllVisitedPositions() { return allVisitedPositions; }
    public List<Position> getCurrentPath() { return currentPath; }
    public Map<Command, CommandStatistics> getCommandStats() { return commandStats; }
    public Date getTimestamp() { return timestamp; }
    public Map<String, Object> getAnalytics() { return analytics; }
}