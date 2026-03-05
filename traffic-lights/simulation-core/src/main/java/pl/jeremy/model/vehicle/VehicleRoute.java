package pl.jeremy.model.vehicle;

import pl.jeremy.model.road.RoadDirection;

public record VehicleRoute(RoadDirection startRoad, RoadDirection endRoad) {

    boolean isColliding(VehicleRoute otherRoute) {
        if (isUTurn() && otherRoute.isUTurn()) {
            return false;
        }

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
}
