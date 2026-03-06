package pl.jeremy.model.vehicle;

import pl.jeremy.model.road.RoadDirection;

/**
 * Represents a movement through the intersection: {@code startRoad} = the approach direction (where the vehicle comes
 * from), {@code endRoad} = the exit direction (where the vehicle leaves).
 *
 * <p>This model is intentionally simplified and is primarily designed to work even when traffic lights are "broken"
 * (e.g., all approaches show GREEN at the same time).
 *
 * <p>The goal is: - detect whether two movements can be considered safe to execute in the same tick (isColliding), -
 * provide a lightweight priority rule to decide which vehicle to release when collisions exist (priority).
 *
 * <p>Important: this logic may still produce deadlocks (e.g., 4 vehicles going straight / all turning left / all
 * U-turning), which is an accepted design decision and is expected to be resolved by a higher-level deadlock breaker
 * later.
 */
public record VehicleRoute(RoadDirection startRoad, RoadDirection endRoad) {

    /**
     * Returns true if executing {@code this} route and {@code otherRoute} in the same tick is considered unsafe in this
     * simplified intersection model.
     *
     * <p>Design decisions documented: - Any route involving a U-turn is treated as colliding with any other route,
     * including another U-turn (conservative safety rule / project decision). - Opposite approaches performing
     * "mirrored" movements are allowed simultaneously (e.g., opposite straight, opposite right-turns, opposite
     * left-turns). This explicitly allows simultaneous left-turn from opposite directions (project decision). - Right
     * turns are treated as "low conflict" in this simplified geometry, so many combinations involving at least one
     * right-turn are allowed (except the same start/end constraints below). - Same startRoad (same approach) or same
     * endRoad (same exit/merge) is considered colliding.
     */
    public boolean isColliding(VehicleRoute otherRoute) {

        // Conservative rule: any U-turn is considered colliding with any movement,
        // including another U-turn (project decision).
        if (isUTurn() || otherRoute.isUTurn()) {
            return true;
        }

        // Mirrored movements from opposite approaches are allowed simultaneously.
        // This enables e.g., opposite straight or opposite left-left (project decision).
        if (startRoad.opposite() == otherRoute.startRoad && endRoad.opposite() == otherRoute.endRoad) {
            return false;
        }

        // The same approach or same exit conflicts in this single-lane/merge model.
        if (startRoad == otherRoute.startRoad || endRoad == otherRoute.endRoad) {
            return true;
        }

        // Fallback rule:
        // If BOTH movements are NOT right-turns, treat them as colliding.
        // If at least one movement is a right-turn, treat it as non-colliding.
        //
        // This intentionally allows, among others:
        //  - simultaneous right-turns from perpendicular approaches,
        //  - simultaneous left-turn from one approach with a right-turn from a perpendicular approach,
        // as a project simplification.
        return !isRightTurn() && !otherRoute.isRightTurn();
    }

    /**
     * U-turn: the vehicle leaves in the same direction it entered. (startRoad == endRoad)
     *
     * <p>Note: U-turns are treated as colliding with everything by {@link #isColliding(VehicleRoute)}.
     */
    public boolean isUTurn() {
        return startRoad == endRoad;
    }

    /**
     * Right turn in this coordinate system.
     *
     * <p>Note: RoadDirection.left() is counter-clockwise, but because startRoad represents the approach direction, in
     * this project a "right turn" corresponds to startRoad.left() == endRoad.
     */
    public boolean isRightTurn() {
        return startRoad.left() == endRoad;
    }

    /**
     * Left turn in this coordinate system.
     *
     * <p>Note: RoadDirection.right() is clockwise, and in this project a "left turn" corresponds to startRoad.right()
     * == endRoad.
     */
    public boolean isLeftTurn() {
        return startRoad.right() == endRoad;
    }

    /** Straight movement: the exit is in the opposite direction of the approach. */
    public boolean isStraight() {
        return startRoad.opposite() == endRoad;
    }

    /**
     * Priority rule used when traffic lights are "broken" and multiple approaches can request passage at once.
     *
     * <p>Semantics: returns true if "this" movement is allowed to go (has priority) over {@code otherRoute} in a
     * conflicting situation.
     *
     * <p>This is NOT a complete traffic law model. It is a pragmatic, deterministic-ish heuristic intended to help
     * select a subset of vehicles to release in a tick.
     *
     * <p>Design decisions documented: - Right-turn is treated as the highest priority among non-U-turn movements
     * (project simplification). - Any U-turn is treated as the lowest priority. Two U-turns are always considered in
     * conflict; some pairs can still deadlock (project decision). - Left-turn yields to traffic from the right and to
     * oncoming traffic (simplified). - Deadlocks are accepted for certain combinations (e.g., all 4
     * straight/left/U-turn), and will be resolved later by a higher-level rule.
     */
    public boolean priority(VehicleRoute otherRoute) {
        // If movements do not collide, both can go.
        if (!isColliding(otherRoute)) {
            return true;
        }

        // Project simplification: right turn "wins" over anything that collides with it.
        // (Arrows/traffic-light permissions are handled elsewhere.)
        if (isRightTurn()) {
            return true;
        }
        if (otherRoute.isRightTurn()) {
            return false;
        }

        // Special handling: two U-turns are always colliding (project decision).
        // We try to break ties for adjacent approaches using a right-hand-like rule.
        // Opposite U-turns can deadlock (accepted; resolved later).
        if ((isUTurn() && otherRoute.isUTurn()) && startRoad.left() == otherRoute.startRoad) {
            return false;
        }
        if ((isUTurn() && otherRoute.isUTurn()) && otherRoute.startRoad.left() == startRoad) {
            return true;
        }
        if (isUTurn() && otherRoute.isUTurn()) {
            return false; // deadlock/tie by design in remaining U-turn pairs
        }

        // U-turn yields to any non-U-turn.
        if (otherRoute.isUTurn()) {
            return true;
        }
        if (isUTurn()) {
            return false;
        }

        // Left-turn yields to:
        //  - traffic from the right (other.start == my.start.left()),
        //  - oncoming traffic (other.start == my.oncoming).
        //
        // The "oncoming" check is encoded here as (endRoad.right() == other.startRoad),
        // which for a left turn corresponds to the opposite approach in this coordinate system.
        if (isLeftTurn() && (startRoad.left() == otherRoute.startRoad || endRoad.right() == otherRoute.startRoad)) {
            return false;
        }

        // Symmetric rule: if the other is left-turning, and we are on its right or oncoming, we have priority.
        if (otherRoute.isLeftTurn()
                && (otherRoute.startRoad.left() == startRoad || otherRoute.endRoad.right() == startRoad)) {
            return true;
        }

        // Straight movement yields to traffic from the right (classic right-hand rule fallback).
        if (isStraight() && startRoad.left() == otherRoute.startRoad) {
            return false;
        }
        return otherRoute.isStraight() && otherRoute.startRoad.left() == startRoad;

        // Remaining ties/conflicts: return false => potential deadlock is acceptable by design.
    }
}
