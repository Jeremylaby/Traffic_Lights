package pl.jeremy.model.road;

public enum RoadDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public RoadDirection opposite() {
        return this.left().left();
    }

    public RoadDirection right() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case EAST -> SOUTH;
            case WEST -> NORTH;
        };
    }

    public RoadDirection left() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case EAST -> NORTH;
            case WEST -> SOUTH;
        };
    }
}
