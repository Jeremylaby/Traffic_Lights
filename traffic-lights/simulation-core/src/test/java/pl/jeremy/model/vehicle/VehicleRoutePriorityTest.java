package pl.jeremy.model.vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import pl.jeremy.model.road.RoadDirection;

public class VehicleRoutePriorityTest {

    // We fix "this" source to SOUTH and test its RIGHT/STRAIGHT/LEFT/U_TURN
    private static final RoadDirection THIS_START = RoadDirection.SOUTH;

    enum Move {
        RIGHT,
        STRAIGHT,
        LEFT,
        U_TURN
    }

    record Case(Move thisMove, RoadDirection otherStart, Move otherMove, boolean expectedThisPriority) {}

    /**
     * Fill expectedThisPriority manually for each case. NOTE: In your coordinate system: - RIGHT means: end =
     * start.left() - LEFT means: end = start.right() - STRAIGHT means: end = start.opposite() - U_TURN means: end =
     * start
     */
    private static final List<Case> CASES = List.of(
            // ========================
            // this = SOUTH RIGHT (S -> E)
            // ========================
            new Case(Move.RIGHT, RoadDirection.NORTH, Move.RIGHT, true),
            new Case(Move.RIGHT, RoadDirection.NORTH, Move.STRAIGHT, true),
            new Case(Move.RIGHT, RoadDirection.NORTH, Move.LEFT, true),
            new Case(Move.RIGHT, RoadDirection.NORTH, Move.U_TURN, true),
            new Case(Move.RIGHT, RoadDirection.EAST, Move.RIGHT, true),
            new Case(Move.RIGHT, RoadDirection.EAST, Move.STRAIGHT, true),
            new Case(Move.RIGHT, RoadDirection.EAST, Move.LEFT, true),
            new Case(Move.RIGHT, RoadDirection.EAST, Move.U_TURN, true),
            new Case(Move.RIGHT, RoadDirection.WEST, Move.RIGHT, true),
            new Case(Move.RIGHT, RoadDirection.WEST, Move.STRAIGHT, true),
            new Case(Move.RIGHT, RoadDirection.WEST, Move.LEFT, true),
            new Case(Move.RIGHT, RoadDirection.WEST, Move.U_TURN, true),

            // ===========================
            // this = SOUTH STRAIGHT (S -> N)
            // ===========================
            new Case(Move.STRAIGHT, RoadDirection.NORTH, Move.RIGHT, true),
            new Case(Move.STRAIGHT, RoadDirection.NORTH, Move.STRAIGHT, true),
            new Case(Move.STRAIGHT, RoadDirection.NORTH, Move.LEFT, true),
            new Case(Move.STRAIGHT, RoadDirection.NORTH, Move.U_TURN, true),
            new Case(Move.STRAIGHT, RoadDirection.EAST, Move.RIGHT, false),
            new Case(Move.STRAIGHT, RoadDirection.EAST, Move.STRAIGHT, false),
            new Case(Move.STRAIGHT, RoadDirection.EAST, Move.LEFT, false),
            new Case(Move.STRAIGHT, RoadDirection.EAST, Move.U_TURN, true),
            new Case(Move.STRAIGHT, RoadDirection.WEST, Move.RIGHT, true),
            new Case(Move.STRAIGHT, RoadDirection.WEST, Move.STRAIGHT, true),
            new Case(Move.STRAIGHT, RoadDirection.WEST, Move.LEFT, true),
            new Case(Move.STRAIGHT, RoadDirection.WEST, Move.U_TURN, true),

            // ======================
            // this = SOUTH LEFT (S -> W)
            // ======================
            new Case(Move.LEFT, RoadDirection.NORTH, Move.RIGHT, false),
            new Case(Move.LEFT, RoadDirection.NORTH, Move.STRAIGHT, false),
            new Case(Move.LEFT, RoadDirection.NORTH, Move.LEFT, true),
            new Case(Move.LEFT, RoadDirection.NORTH, Move.U_TURN, true),
            new Case(Move.LEFT, RoadDirection.EAST, Move.RIGHT, true),
            new Case(Move.LEFT, RoadDirection.EAST, Move.STRAIGHT, false),
            new Case(Move.LEFT, RoadDirection.EAST, Move.LEFT, false),
            new Case(Move.LEFT, RoadDirection.EAST, Move.U_TURN, true),
            new Case(Move.LEFT, RoadDirection.WEST, Move.RIGHT, true),
            new Case(Move.LEFT, RoadDirection.WEST, Move.STRAIGHT, true),
            new Case(Move.LEFT, RoadDirection.WEST, Move.LEFT, true),
            new Case(Move.LEFT, RoadDirection.WEST, Move.U_TURN, true),

            // =========================
            // this = SOUTH U_TURN (S -> S)
            // =========================
            new Case(Move.U_TURN, RoadDirection.NORTH, Move.RIGHT, false),
            new Case(Move.U_TURN, RoadDirection.NORTH, Move.STRAIGHT, false),
            new Case(Move.U_TURN, RoadDirection.NORTH, Move.LEFT, false),
            new Case(Move.U_TURN, RoadDirection.NORTH, Move.U_TURN, false),
            new Case(Move.U_TURN, RoadDirection.EAST, Move.RIGHT, false),
            new Case(Move.U_TURN, RoadDirection.EAST, Move.STRAIGHT, false),
            new Case(Move.U_TURN, RoadDirection.EAST, Move.LEFT, false),
            new Case(Move.U_TURN, RoadDirection.EAST, Move.U_TURN, false),
            new Case(Move.U_TURN, RoadDirection.WEST, Move.RIGHT, false),
            new Case(Move.U_TURN, RoadDirection.WEST, Move.STRAIGHT, false),
            new Case(Move.U_TURN, RoadDirection.WEST, Move.LEFT, false),
            new Case(Move.U_TURN, RoadDirection.WEST, Move.U_TURN, true));

    @Test
    void manualMatrixExpectedValuesNoPairDeadlocks() {
        for (Case c : CASES) {
            VehicleRoute me = route(THIS_START, c.thisMove());
            VehicleRoute other = route(c.otherStart(), c.otherMove());

            boolean mePriority = me.priority(other);
            boolean otherPriority = other.priority(me);

            String msg = "me=" + fmt(me) + " other=" + fmt(other) + " colliding=" + me.isColliding(other);

            // 1) Manual expectation: you decide true/false
            assertEquals(c.expectedThisPriority(), mePriority, msg);

            // 2) Invariant you requested:
            // If this.priority(other) is false, then other.priority(this) must be true.
            if (!mePriority && !(me.isUTurn() && other.isUTurn())) {
                assertTrue(otherPriority, "Invariant failed: this.priority=false but other.priority=false. " + msg);
            }
        }
    }

    // ===== helpers =====

    private static VehicleRoute route(RoadDirection start, Move move) {
        return switch (move) {
            case RIGHT -> new VehicleRoute(start, start.left()); // your RIGHT
            case STRAIGHT -> new VehicleRoute(start, start.opposite());
            case LEFT -> new VehicleRoute(start, start.right()); // your LEFT
            case U_TURN -> new VehicleRoute(start, start); // U-turn
        };
    }

    private static String fmt(VehicleRoute r) {
        return r.startRoad() + "->" + r.endRoad()
                + " (R=" + r.isRightTurn()
                + ", S=" + r.isStraight()
                + ", L=" + r.isLeftTurn()
                + ", U=" + r.isUTurn() + ")";
    }
}
