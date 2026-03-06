package pl.jeremy.model.vehicle;

import pl.jeremy.model.road.RoadDirection;

public record VehicleRoute(RoadDirection startRoad, RoadDirection endRoad) {

    boolean isColliding(VehicleRoute otherRoute) {

        if (isUTurn() || otherRoute.isUTurn()) {
            return true;
        }

        if (startRoad.opposite() == otherRoute.startRoad && endRoad.opposite() == otherRoute.endRoad) {
            return false;
        }
        if (startRoad == otherRoute.startRoad || endRoad == otherRoute.endRoad) {
            return true;
        }
        return !isRightTurn() && !otherRoute.isRightTurn();
    }

    boolean isUTurn() {
        return startRoad == endRoad;
    }

    // left() means counter clockwise
    boolean isRightTurn() {
        return startRoad.left() == endRoad;
    }

    // right() means clockwise
    boolean isLeftTurn() {
        return startRoad.right() == endRoad;
    }

    boolean isStraight() {
        return startRoad.opposite() == endRoad;
    }

    boolean priority(VehicleRoute otherRoute) {
        if (!isColliding(otherRoute)) {
            return true;
        }
        if (isRightTurn()) {
            return true;
        }
        if (otherRoute.isRightTurn()) {
            return false;
        }
        if ((isUTurn() && otherRoute.isUTurn()) && startRoad.left() == otherRoute.startRoad) {
            return false;
        }
        if ((isUTurn() && otherRoute.isUTurn()) && otherRoute.startRoad.left() == startRoad) {
            return true;
        }
        if (isUTurn() && otherRoute.isUTurn()) {
            return false;
        }
        if (otherRoute.isUTurn()) {
            return true;
        }

        if (isUTurn()) {
            return false;
        }
        if (isLeftTurn() && (startRoad.left() == otherRoute.startRoad || endRoad.right() == otherRoute.startRoad)) {
            return false;
        }
        if (otherRoute.isLeftTurn()
                && (otherRoute.startRoad.left() == startRoad || otherRoute.endRoad.right() == startRoad)) {
            return true;
        }
        if (isStraight() && startRoad.left() == otherRoute.startRoad) {
            return false;
        }
        if (otherRoute.isStraight() && otherRoute.startRoad.left() == startRoad) {
            return true;
        }
        return false;
    }
}
