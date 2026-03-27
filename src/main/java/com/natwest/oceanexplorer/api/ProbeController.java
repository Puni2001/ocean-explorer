package com.natwest.oceanexplorer.api;

import com.natwest.oceanexplorer.core.*;
import com.natwest.oceanexplorer.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class ProbeController {
    private static final Logger logger = LoggerFactory.getLogger(ProbeController.class);

    private final String probeId;
    private final Grid grid;
    private final Probe probe;
    private final ExplorationMetrics metrics;
    private final Map<String, Object> metadata;

    private ProbeController(Builder builder) {
        this.probeId = builder.probeId != null ? builder.probeId : UUID.randomUUID().toString();
        this.grid = initializeGrid(builder);
        this.probe = initializeProbe(builder);
        this.metrics = new ExplorationMetrics();
        this.metadata = new ConcurrentHashMap<>();

        logger.info("Probe {} initialized at position {} facing {}",
                probeId, probe.getPosition(), probe.getDirection());
    }

    private Grid initializeGrid(Builder builder) {
        Grid.Builder gridBuilder = new Grid.Builder()
                .withBounds(builder.minX, builder.minY, builder.maxX, builder.maxY);

        if (builder.obstacles != null && !builder.obstacles.isEmpty()) {
            gridBuilder.withObstacles(builder.obstacles);
            logger.debug("Added {} obstacles to grid", builder.obstacles.size());
        }

        return gridBuilder.build();
    }

    private Probe initializeProbe(Builder builder) {
        try {
            return new Probe(builder.startPosition, builder.startDirection, grid);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to initialize probe: {}", e.getMessage());
            throw new ProbeInitializationException("Cannot place probe", e);
        }
    }


    public CommandResult executeCommand(Command command) {
        logger.debug("Executing command: {} on probe {}", command, probeId);

        long startTime = System.nanoTime();
        try {
            Position previousPosition = probe.getPosition();
            Direction previousDirection = probe.getDirection();

            probe.executeCommand(command);

            long duration = System.nanoTime() - startTime;
            metrics.recordCommand(command, duration);

            CommandResult result = new CommandResult(
                    command,
                    previousPosition,
                    probe.getPosition(),
                    previousDirection,
                    probe.getDirection(),
                    duration,
                    true,
                    null
            );

            logger.debug("Command {} executed successfully in {} ns", command, duration);
            return result;

        } catch (GridBoundaryException | ObstacleCollisionException e) {
            logger.warn("Command {} prevented: {}", command, e.getMessage());
            metrics.recordFailedCommand(command);

            return new CommandResult(
                    command,
                    probe.getPosition(),
                    probe.getPosition(),
                    probe.getDirection(),
                    probe.getDirection(),
                    System.nanoTime() - startTime,
                    false,
                    e.getMessage()
            );
        }
    }


    public List<CommandResult> executeCommands(List<Command> commands) {
        logger.info("Executing {} commands on probe {}", commands.size(), probeId);

        return commands.stream()
                .map(this::executeCommand)
                .collect(Collectors.toList());
    }


    public List<CommandResult> executeCommands(String commandString) {
        if (commandString == null || commandString.trim().isEmpty()) {
            logger.warn("Empty command string received");
            return Collections.emptyList();
        }

        List<Command> commands = new ArrayList<>();
        for (char c : commandString.toCharArray()) {
            try {
                commands.add(Command.fromChar(c));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid command '{}' ignored", c);
                metrics.recordInvalidCommand(String.valueOf(c));
            }
        }

        return executeCommands(commands);
    }


    public ProbeStatus getStatus() {
        return new ProbeStatus(
                probeId,
                probe.getPosition(),
                probe.getDirection(),
                metrics.getTotalCommandsExecuted(),
                metrics.getSuccessRate(),
                new Date()
        );
    }


    public ExplorationReport getExplorationReport() {
        return new ExplorationReport(
                probeId,
                probe.getVisitedPositions(),
                probe.getCurrentPath(),
                metrics.getCommandStatistics(),
                new Date()
        );
    }


    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
        logger.debug("Added metadata {} for probe {}", key, probeId);
    }

    public static class Builder {
        private String probeId;
        private Position startPosition;
        private Direction startDirection;
        private int minX = 0;
        private int minY = 0;
        private int maxX = Integer.MAX_VALUE;
        private int maxY = Integer.MAX_VALUE;
        private Set<Position> obstacles = new HashSet<>();

        public Builder withProbeId(String probeId) {
            this.probeId = probeId;
            return this;
        }

        public Builder withStartPosition(Position position) {
            this.startPosition = position;
            return this;
        }

        public Builder withStartDirection(Direction direction) {
            this.startDirection = direction;
            return this;
        }

        public Builder withGridBounds(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            return this;
        }

        public Builder withObstacles(Set<Position> obstacles) {
            this.obstacles = obstacles != null ? new HashSet<>(obstacles) : new HashSet<>();
            return this;
        }

        public ProbeController build() {
            validate();
            return new ProbeController(this);
        }

        private void validate() {
            if (startPosition == null) {
                throw new IllegalArgumentException("Start position is required");
            }
            if (startDirection == null) {
                throw new IllegalArgumentException("Start direction is required");
            }
            if (minX > maxX || minY > maxY) {
                throw new IllegalArgumentException("Invalid grid bounds");
            }
        }
    }
}