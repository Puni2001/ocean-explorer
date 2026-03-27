package com.natwest.oceanexplorer.api;

import com.natwest.oceanexplorer.core.Direction;
import com.natwest.oceanexplorer.core.Position;
import java.util.Date;

public class ProbeStatus {
    private final String probeId;
    private final Position position;
    private final Direction direction;
    private final long totalCommandsExecuted;
    private final double successRate;
    private final Date timestamp;

    public ProbeStatus(String probeId, Position position, Direction direction,
                       long totalCommandsExecuted, double successRate, Date timestamp) {
        this.probeId = probeId;
        this.position = position;
        this.direction = direction;
        this.totalCommandsExecuted = totalCommandsExecuted;
        this.successRate = successRate;
        this.timestamp = timestamp;
    }

    // Getters
    public String getProbeId() { return probeId; }
    public Position getPosition() { return position; }
    public Direction getDirection() { return direction; }
    public long getTotalCommandsExecuted() { return totalCommandsExecuted; }
    public double getSuccessRate() { return successRate; }
    public Date getTimestamp() { return timestamp; }
}