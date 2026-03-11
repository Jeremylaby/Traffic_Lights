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

    public void addVehicle(Vehicle vehicle) {
        vehicles.offer(vehicle);
    }

    public void poolFirstVehicle() {
        vehicles.poll();
    }

    public Vehicle peekFirstVehicle() {
        return vehicles.peek();
    }

    public int getVehicleCount() {
        return vehicles.size();
    }

    public boolean isEmpty() {
        return vehicles.isEmpty();
    }

    public Queue<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lane lane = (Lane) o;
        return laneType == lane.laneType && Objects.equals(vehicles, lane.vehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(laneType, vehicles);
    }
}
