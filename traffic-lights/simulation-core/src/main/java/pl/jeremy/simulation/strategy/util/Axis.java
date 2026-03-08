package pl.jeremy.simulation.strategy.util;

public enum Axis {
    NORTH_SOUTH,
    EAST_WEST;

    public Axis opposite() {
        return this == NORTH_SOUTH ? EAST_WEST : NORTH_SOUTH;
    }
}
