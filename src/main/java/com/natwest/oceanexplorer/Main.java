package com.natwest.oceanexplorer;

import com.natwest.oceanexplorer.api.ProbeController;
import com.natwest.oceanexplorer.core.Direction;
import com.natwest.oceanexplorer.core.Position;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Ocean Explorer Probe Control System");
        System.out.println("===================================\n");


        if (args.length > 0 && args[0].equals("demo")) {
            runDemo();
        } else {
            runInteractiveMode();
        }
    }

    private static void runDemo() {
        System.out.println("Running demo mission...\n");


        Set<Position> obstacles = new HashSet<>();
        obstacles.add(new Position(3, 3));
        obstacles.add(new Position(4, 4));


        ProbeController probe = new ProbeController.Builder()
                .withProbeId("DEMO-001")
                .withStartPosition(new Position(0, 0))
                .withStartDirection(Direction.NORTH)
                .withGridBounds(0, 0, 10, 10)
                .withObstacles(obstacles)
                .build();


        String mission = "FFRFFLFFRFBLLFFRF";
        System.out.println("Executing mission: " + mission);
        probe.executeCommands(mission);


        System.out.println("\n" + probe.getExplorationReport().toFormattedString());
    }

    private static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter grid width (default 10): ");
            int width = scanner.nextInt();
            System.out.print("Enter grid height (default 10): ");
            int height = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.print("Enter start X coordinate: ");
            int startX = scanner.nextInt();
            System.out.print("Enter start Y coordinate: ");
            int startY = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter start direction (N/E/S/W): ");
            String dir = scanner.nextLine().toUpperCase();
            Direction direction = switch(dir) {
                case "N" -> Direction.NORTH;
                case "E" -> Direction.EAST;
                case "S" -> Direction.SOUTH;
                case "W" -> Direction.WEST;
                default -> Direction.NORTH;
            };

            ProbeController probe = new ProbeController.Builder()
                    .withStartPosition(new Position(startX, startY))
                    .withStartDirection(direction)
                    .withGridBounds(0, 0, width - 1, height - 1)
                    .build();

            System.out.println("\nProbe initialized! Enter commands (F/B/L/R):");
            System.out.println("Type 'status' to see current status");
            System.out.println("Type 'report' to see full exploration report");
            System.out.println("Type 'quit' to exit\n");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim().toUpperCase();

                if (input.equals("QUIT")) {
                    break;
                } else if (input.equals("STATUS")) {
                    System.out.println(probe.getStatus());
                } else if (input.equals("REPORT")) {
                    System.out.println(probe.getExplorationReport().toFormattedString());
                } else {
                    probe.executeCommands(input);
                    System.out.println("Current position: " + probe.getStatus().getPosition());
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }

        System.out.println("\nExploration session ended.");
    }
}