# Traffic_Lights

A CLI-based simulation of an intelligent traffic-light controlled crossroads.

## Requirements

- **JDK 21** (Java 21)

The project uses the **Gradle Wrapper**, so you don’t need a local Gradle installation.

## Project setup

From the repository root:

```bash
cd traffic-lights
```

## Build, format, and verify

Run the full verification pipeline (tests + Checkstyle + Spotless):

```bash
./gradlew check
```

On Windows (PowerShell):

```powershell
.\gradlew.bat check
```

## Run tests

All tests:

```bash
./gradlew test
```

Or only the CLI module tests:

```bash
./gradlew :simulation-cli:test
```

## Run the program (CLI)

Example (input JSON → output JSON):

```bash
./gradlew :simulation-cli:run --args="input.json output.json"
```

On Windows (PowerShell):

```powershell
.\gradlew.bat :simulation-cli:run --args="input.json output.json"
```

The commands above run the application with the default `SIMPLE` traffic light strategy.

To explicitly select a traffic light strategy, add `MODE` as the third argument:

```bash
./gradlew :simulation-cli:run --args="input.json output.json MODE"
```

On Windows (PowerShell):

```powershell
.\gradlew.bat :simulation-cli:run --args="input.json output.json MODE"
```

Available `MODE` values:

- `SIMPLE`
- `BROKEN`
- `ADAPTIVE`
- `FEEDBACK`
- `PROPORTIONAL`

---

## Visual simulation (simulation-ui + simulation-api)

> **Additional deliverable** — The original task specification covered only the CLI-based simulation engine. The REST API (`simulation-api`) and the browser-based visualisation (`simulation-ui`) were added as an extra layer on top of the core logic. Due to time constraints this part was developed quickly and may contain rough edges.

### Prerequisites

| Tool                                                              | Minimum version                  |
| ----------------------------------------------------------------- | -------------------------------- |
| [Docker Desktop](https://www.docker.com/products/docker-desktop/) | 24.x                             |
| Docker Compose                                                    | v2 (bundled with Docker Desktop) |

> No JDK, Node, or any other runtime is required — everything runs inside containers.

### Project layout

```
Traffic_Lights/
├── docker-compose.yml
└── traffic-lights/               ← Gradle multi-project root
    ├── settings.gradle.kts
    ├── simulation-core/          ← shared domain logic
    ├── simulation-cli/           ← original CLI module
    ├── simulation-api/           ← Spring Boot REST API (depends on core)
    │   └── Dockerfile
    └── simulation-ui/            ← React 19 + Vite frontend
        └── Dockerfile
```

### Quick start

From the **repository root** (where `docker-compose.yml` lives):

```bash
docker compose up --build
```

First build downloads base images and compiles all modules — allow a few minutes. Subsequent starts are much faster due to Docker layer caching.

Once both containers are healthy:

| Service      | URL                          |
| ------------ | ---------------------------- |
| UI           | http://localhost:4173        |
| REST API     | http://localhost:8080        |
| Health check | http://localhost:8080/health |

### Stopping

```bash
docker compose down
```

## Smart traffic lights algorithm

The project separates two concerns:

- **Traffic light strategy** decides **which axis should be served** and **how long the current green phase should last**.
- **Vehicle release policy** decides **which vehicles may actually leave the intersection in a single step**, including conflict resolution for green and green-arrow states.

The overall design intentionally does **not** optimize for raw throughput at all costs. Instead, it favors **predictability, fairness, and basic starvation prevention**. In practice, this means the controller may give up some theoretical maximum throughput in exchange for more balanced service between both axes and lower risk that one direction waits indefinitely.

### Traffic light state model

The simulation uses a project-specific Polish traffic-light state cycle:

`RED -> GREEN_ARROW -> RED_YELLOW -> GREEN -> YELLOW -> RED`

In this model, `GREEN_ARROW` is treated as a restricted state: it is much closer to `RED` than to full `GREEN`, but it still allows selected right turns. Several strategies therefore operate in a stable pattern where the active axis has full `GREEN`, while the passive axis may remain on `GREEN_ARROW`.

## Available traffic light strategies

**SimpleTrafficLightStrategy**
A fixed-time controller. It uses constant green and transition durations and does not inspect current queue lengths. This is the simplest and most predictable strategy, but it is also the least responsive to changing traffic conditions.
**Closest CPU scheduling analogy:** **Round Robin / fixed time slice scheduling** — each side gets service based on a simple timer, not on current demand.

**BrokenTrafficLightStrategy**
A special mode where all approaches stay green at the same time. In this mode, light control no longer provides separation between directions, so correctness depends on the `VehicleReleasePolicy`, which resolves conflicts and selects a safe subset of vehicles that may move in the same step.
**Closest CPU scheduling analogy:** not a classic scheduler; it is closer to a **shared-resource arbiter** where conflict resolution matters more than time slicing.

**AdaptiveTrafficLightStrategy**
A queue-responsive controller. It keeps one axis active, but may switch earlier when the active axis becomes empty or when the passive axis has a clearly larger queue. At the same time, it enforces lower and upper bounds on green duration, so the controller stays responsive without oscillating too quickly.
**Closest CPU scheduling analogy:** **dynamic priority scheduling** with fairness guards — service adapts to observed load, but minimum and maximum green times prevent overly aggressive switching.

**ProportionalAllocationTrafficLightStrategy**
A planning-based controller. It computes planned green durations for the north-south and east-west axes proportionally to their relative queue sizes, then refreshes that plan periodically after a number of full cycles. This makes it less reactive than the adaptive strategy, but more stable and easier to reason about over longer runs.
**Closest CPU scheduling analogy:** **Weighted Round Robin / weighted time-slice scheduling** — both axes receive service in proportion to their observed demand.

**FeedbackControlTrafficLightStrategy**
A controller based on accumulated traffic pressure. Instead of looking only at the current queue, it maintains internal pressure values for both axes, applies decay to older observations, and subtracts a service penalty from the currently active axis. The passive axis takes over when its pressure becomes sufficiently larger than the active one.
**Closest CPU scheduling analogy:** **feedback-based dynamic scheduling** or **Multilevel Feedback Queue–style thinking** — recent history influences future scheduling decisions, rather than every phase being decided from scratch.

### Design trade-off: fairness and starvation prevention over maximum throughput

The current strategies are intentionally conservative. They do not try to maximize the number of vehicles released in every possible instant. Instead, they try to keep service **fair between both axes**, avoid overly long monopolization of one side, and provide **basic starvation prevention** through bounded green times, axis switching, and deterministic conflict resolution. This is also consistent with the vehicle release policy, which uses deterministic tie-breaking and fairness toggles in deadlock-prone configurations instead of globally maximizing the number of simultaneously released vehicles.

### Vehicle release policy

Traffic light strategy only decides which axis has stronger right-of-way. The final decision about which vehicles may actually move is handled by `PolishVehicleReleasePolicy`. It evaluates the first waiting vehicle from green approaches, allows selected right turns on `GREEN_ARROW`, resolves conflicts using route priority rules, and uses explicit deadlock-breaking heuristics for symmetric cases. This means the final behavior of the intersection is the result of both: **phase scheduling** and **per-step conflict resolution**.

## Conflict Resolution & Vehicle Release Policy (Design Notes)

#### Motivation

The simulation supports a “broken lights” mode where multiple approaches can be green at the same time (even all of them). In this scenario, multiple vehicles may attempt to enter the intersection in the same tick, so the system needs a deterministic conflict-resolution rule to decide which vehicles are allowed to move.

#### Primary Rule: Right-Hand Priority (Polish-style)

When there is a potential conflict between vehicles, the selection is based on the **right-hand rule** (priority to traffic coming from the right), aligned with the general priority principle used in Polish traffic rules. This is a **project design decision**.

#### Allowed “Non-Realistic but Useful” Concurrent Moves

This model intentionally permits some concurrent move sets to keep the simulation simple and to support “broken lights” scenarios. One explicitly allowed configuration is:

- **N_LEFT + S_LEFT + E_RIGHT + W_RIGHT** (opposite left-turns plus perpendicular right-turns in the same tick).

#### Deadlock / Tie Handling: Opposite-Axis Release With Fairness Toggle

Some configurations can still lead to deadlocks or “soft-deadlocks” (e.g., symmetric conflicts). To ensure progress, the system applies deadlock-breaking heuristics:

- **4 candidates, all the same “deadlock-prone” class** (all U-turns OR all left-turns OR all straight): release an **opposite pair** (N+S or W+E), and **alternate the preferred axis** across ticks for fairness (`preferNorthSouthAxis`).

- **3 candidates tie case (all scores == 1):** instead of removing an arbitrary “minimum”, the system selects vehicles on the preferred axis (**NS or WE**) and flips the axis next tick to avoid starvation. This is mainly to avoid a soft-deadlock where nobody moves in repeated symmetric situations.

- **3 candidates with a clear winner (score == 2):** if one vehicle has priority over both others, it is released immediately (dominant candidate rule).

#### U-turn Special Case (Pragmatic Progress Rule)

U-turns are treated conservatively in collision logic, but in the “broken lights” conflict resolver there is a pragmatic progress rule:

- If **two vehicles are both U-turning and come from opposite approaches**, they are allowed to proceed together (project decision / “magic pass” to avoid getting stuck).

#### Throughput vs Simplicity

A more optimal approach would be to select the **largest set of non-conflicting vehicles** per tick (maximize throughput). However, once the project was structured around the above deterministic rules, refactoring toward a global “maximum compatible set” algorithm would require significant changes. For the current stage, the focus is on determinism, simplicity, and guaranteed progress.
