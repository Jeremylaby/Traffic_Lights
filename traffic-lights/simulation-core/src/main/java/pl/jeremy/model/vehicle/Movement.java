package pl.jeremy.model.vehicle;

public enum Movement {
    LEFT,
    STRAIGHT,
    RIGHT,
    UTURN;

    public static Movement from(VehicleRoute route) {
        if (route.isLeftTurn()) {
            return LEFT;
        }
        if (route.isRightTurn()) {
            return RIGHT;
        }
        if (route.isUTurn()) {
            return UTURN;
        }
        return STRAIGHT;
    }
}
