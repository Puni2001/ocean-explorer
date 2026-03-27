// Comprehensive Integration Test
package com.natwest.oceanexplorer.integration;

import com.natwest.oceanexplorer.api.*;
import com.natwest.oceanexplorer.core.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OceanExplorerIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(OceanExplorerIntegrationTest.class);

    @Test
    @Order(1)
    @DisplayName("Complete mission simulation")
    void completeMissionSimulation() {
        // Setup
        Set<Position> obstacles = new HashSet<>();
        obstacles.add(new Position(3, 3));
        obstacles.add(new Position(4, 4));

        ProbeController probe = new ProbeController.Builder()
                .withProbeId("MISSION-001")
                .withStartPosition(new Position(0, 0))
                .withStartDirection(Direction.NORTH)
                .withGridBounds(0, 0, 10, 10)
                .withObstacles(obstacles)
                .build();

        // Execute complex mission
        String missionCommands = "FFRFFLFFRFBLLFFRF";
        List<CommandResult> results = probe.executeCommands(missionCommands);

        // Verify
        assertNotNull(results);
        assertTrue(results.size() > 0);

        // Get report
        ExplorationReport report = probe.getExplorationReport();
        assertNotNull(report);

        logger.info("Mission completed:\n{}", report.toFormattedString());

        // Validate no collisions with obstacles
        Set<Position> finalPositions = report.getAllVisitedPositions().stream()
                .collect(Collectors.toSet());

        for (Position obstacle : obstacles) {
            assertFalse(finalPositions.contains(obstacle),
                    "Probe should not visit obstacle positions");
        }
    }

    @Test
    @Order(2)
    @DisplayName("Concurrent command execution")
    void concurrentCommandExecution() throws InterruptedException {
        ProbeController probe = new ProbeController.Builder()
                .withStartPosition(new Position(5, 5))
                .withStartDirection(Direction.NORTH)
                .withGridBounds(0, 0, 10, 10)
                .build();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<List<CommandResult>>> futures = new ArrayList<>();

        // Submit multiple command sequences concurrently
        for (int i = 0; i < 10; i++) {
            final int sequenceId = i;
            futures.add(executor.submit(() -> {
                String commands = generateRandomCommands(20);
                return probe.executeCommands(commands);
            }));
        }

        // Wait for completion
        executor.shutdown();
        boolean finished = executor.awaitTermination(30, TimeUnit.SECONDS);
        assertTrue(finished);

        // Verify results
        for (Future<List<CommandResult>> future : futures) {
            try {
                List<CommandResult> results = future.get();
                assertNotNull(results);
            } catch (ExecutionException e) {
                fail("Concurrent execution failed: " + e.getMessage());
            }
        }

        // Check final consistency
        ProbeStatus status = probe.getStatus();
        assertNotNull(status);
        assertTrue(status.getTotalCommandsExecuted() >= 200);

        logger.info("Concurrent test completed: {} commands executed",
                status.getTotalCommandsExecuted());
    }

    @Test
    @Order(3)
    @DisplayName("Edge case - Boundary navigation")
    void boundaryNavigationTest() {
        ProbeController probe = new ProbeController.Builder()
                .withStartPosition(new Position(0, 0))
                .withStartDirection(Direction.SOUTH)
                .withGridBounds(0, 0, 5, 5)
                .build();

        // Try to move beyond boundaries
        List<CommandResult> results = probe.executeCommands("FFFF");

        // Should stay at boundary
        ProbeStatus status = probe.getStatus();
        assertEquals(0, status.getPosition().getX());
        assertEquals(0, status.getPosition().getY());

        // Verify boundary violations were recorded as failures
        long failures = results.stream()
                .filter(r -> !r.isSuccessful())
                .count();

        assertTrue(failures > 0);
    }

    @Test
    @Order(4)
    @DisplayName("Edge case - Obstacle avoidance pathfinding")
    void obstacleAvoidanceTest() {
        Set<Position> obstacles = new HashSet<>();
        // Create a wall of obstacles
        for (int y = 2; y <= 8; y++) {
            obstacles.add(new Position(5, y));
        }

        ProbeController probe = new ProbeController.Builder()
                .withStartPosition(new Position(5, 1))
                .withStartDirection(Direction.NORTH)
                .withGridBounds(0, 0, 10, 10)
                .withObstacles(obstacles)
                .build();

        // Try to move through the wall
        probe.executeCommands("FFFFF");

        // Should not have passed through obstacles
        ProbeStatus status = probe.getStatus();
        assertTrue(status.getPosition().getY() < 2);

        // Try to go around
        probe.executeCommands("RFFFFLFFFFFFFF");
        status = probe.getStatus();

        // Should be above the obstacle wall now
        assertTrue(status.getPosition().getY() > 8);
    }

    @Test
    @Order(5)
    @DisplayName("Performance - Large grid exploration")
    void performanceTest() {
        long startTime = System.currentTimeMillis();

        ProbeController probe = new ProbeController.Builder()
                .withStartPosition(new Position(50, 50))
                .withStartDirection(Direction.NORTH)
                .withGridBounds(0, 0, 100, 100)
                .build();

        // Generate 10,000 random commands
        StringBuilder commands = new StringBuilder();
        Random random = new Random();
        Command[] commandValues = Command.values();

        for (int i = 0; i < 10000; i++) {
            commands.append(commandValues[random.nextInt(commandValues.length)].getChar());
        }

        long executionStart = System.currentTimeMillis();
        probe.executeCommands(commands.toString());
        long executionTime = System.currentTimeMillis() - executionStart;

        ExplorationReport report = probe.getExplorationReport();

        logger.info("Performance test results:");
        logger.info("  Total commands: 10,000");
        logger.info("  Execution time: {} ms", executionTime);
        logger.info("  Commands/second: {}", 10000.0 / (executionTime / 1000.0));
        logger.info("  Unique positions: {}",
                report.getAnalytics().get("uniquePositionsVisited"));

        assertTrue(executionTime < 5000, "Should execute 10,000 commands within 5 seconds");
    }

    private String generateRandomCommands(int length) {
        Random random = new Random();
        char[] commands = {'F', 'B', 'L', 'R'};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(commands[random.nextInt(commands.length)]);
        }
        return sb.toString();
    }
}