# Ocean Explorer Kata (Java)

Java implementation of the Ocean Explorer kata: control a remotely operated probe on a bounded ocean-floor grid using interpreted commands.

## Problem Scope

The probe must:

- start at a given `(x, y)` coordinate and direction
- process command input from the surface
- move forward/backward
- turn left/right
- remain inside grid bounds
- avoid obstacle cells
- provide a summary of visited coordinates

## Solution Overview

The solution is structured around clear domain objects and API orchestration:

- `Position` - immutable coordinate value object
- `Direction` - orientation and turning behavior
- `Command` - parsed control commands (`F`, `B`, `L`, `R`)
- `Grid` - boundaries and obstacle rules
- `Probe` - movement/turning execution with safety checks
- `ProbeController` - command interpretation, execution flow, reporting
- `ExplorationReport` / `ProbeStatus` / metrics classes - reporting and telemetry

This design keeps movement rules inside the domain model while exposing a simple API-style controller for surface commands.

## Requirement Mapping

- **Defined grid with x/y location** -> `Grid`, `Position`
- **Initial start point + direction** -> `ProbeController.Builder`
- **Interpreted controls** -> `Command.fromChar(...)`, `executeCommands(String)`
- **Move forwards/backwards** -> `Probe.move(...)` via `F` and `B`
- **Turn left/right** -> `Direction.turnLeft()`, `Direction.turnRight()`
- **Stay on grid** -> boundary validation before position update
- **Avoid obstacles** -> obstacle collision checks before move commit
- **Print summary of coordinates visited** -> `ExplorationReport` and formatted output

## Test Strategy (TDD Evidence)

Tests are separated by level:

- **Unit tests** (`src/test/java/.../unit`)
  - `PositionTest`
  - `DirectionTest`
  - `CommandTest`
  - `GridTest`
- **Integration tests** (`src/test/java/.../integration`)
  - end-to-end mission flow
  - boundary behavior
  - obstacle behavior
  - concurrent command execution
  - performance sanity check

Approach used:

- start from expected behavior for domain rules
- add/adjust implementation to satisfy tests
- verify edge scenarios (invalid input, blocked movement, boundary limits)

## Edge Cases Covered

- attempts to move outside grid bounds
- attempts to move into obstacle coordinates
- invalid commands in input stream
- empty/null command strings
- concurrent command execution requests
- large command volume performance path

## Build and Run

### Prerequisites

- Java 17+
- Maven 3.9+

### Verify

```bash
mvn clean install
```

### Package

```bash
mvn clean package
```

## Test Demonstration

### Full Verification

Run:

```bash
mvn clean install
```

Expected:

- Build completes successfully.
- All unit and integration tests pass (`Tests run: 21, Failures: 0, Errors: 0`).

### Example Behavioral Validation

- **Boundary safety**: Starting at `(0, 0)` facing `SOUTH`, executing `FFFF` keeps the probe at `(0, 0)` and records unsuccessful movement commands.
- **Obstacle safety**: Probe does not enter obstacle cells and can navigate around obstacle walls when given turn + move sequences.
- **Concurrent execution**: Multiple concurrent command submissions complete and aggregate command metrics remain consistent.

## Design Notes

- Uses object-oriented modeling for core domain concepts.
- `Position` is immutable to reduce accidental state mutation.
- Metrics and command statistics are tracked for reporting.
- Thread-safe structures are used in controller/metrics paths where concurrency is exercised.

## Assumptions

- Grid bounds are inclusive (`min <= x <= max`, `min <= y <= max`).
- If a move would violate boundary or obstacle rule, the probe remains in place and the command result is marked unsuccessful.
- Turn commands update direction only and do not change position.


## Submission Checklist

- [x] Core requirements implemented
- [x] Unit and integration tests included
- [x] `mvn clean install` passing
- [x] Documentation and assumptions included

