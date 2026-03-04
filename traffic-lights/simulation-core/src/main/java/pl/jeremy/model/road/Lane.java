package pl.jeremy.model.road;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import pl.jeremy.model.vehicle.Vehicle;

public class Lane {
    private final LaneType laneType;
    private final Queue<Vehicle> vehicles;

    public Lane(LaneType laneType) {
        this.laneType = laneType;
        this.vehicles = new LinkedList<>();
    }

    public LaneType getLaneType() {
        return laneType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lane lane = (Lane) o;
        return laneType == lane.laneType && Objects.equals(vehicles, lane.vehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(laneType, vehicles);
    }
}
