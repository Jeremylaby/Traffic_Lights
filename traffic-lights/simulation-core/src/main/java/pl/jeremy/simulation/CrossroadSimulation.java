package pl.jeremy.simulation;

import java.util.List;
import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.vehicle.Vehicle;
import pl.jeremy.simulation.strategy.TrafficLightStrategy;
import pl.jeremy.simulation.util.SimulationSnapshot;
import pl.jeremy.simulation.util.StepResult;

public final class CrossroadSimulation {
    private final PolishCrossroad crossroad;
    private final TrafficLightStrategy trafficLightStrategy;
    private final VehicleReleasePolicy vehicleReleasePolicy;
    private int stepNumber;

    public CrossroadSimulation(
            PolishCrossroad crossroad,
            TrafficLightStrategy trafficLightStrategy,
            VehicleReleasePolicy vehicleReleasePolicy) {
        this.crossroad = crossroad;
        this.trafficLightStrategy = trafficLightStrategy;
        this.vehicleReleasePolicy = vehicleReleasePolicy;
        this.stepNumber = 0;

        this.trafficLightStrategy.configure(crossroad);
    }

    public void addVehicle(Vehicle vehicle) {
        crossroad.addVehicle(vehicle);
    }

    public boolean roadsAreEmpty() {
        return crossroad.getRoads().values().stream()
                .allMatch(road -> road.getEntanceLane().isEmpty());
    }

    public StepResult step() {
        List<Vehicle> released = vehicleReleasePolicy.selectVehiclesToRelease(crossroad);
        released.forEach(crossroad::poolFirstVehicle);
        List<String> releasedIds = released.stream().map(Vehicle::getId).toList();

        trafficLightStrategy.advanceTrafficLights(crossroad);

        StepResult result = new StepResult(stepNumber, releasedIds, snapshot());

        stepNumber++;
        return result;
    }

    public SimulationSnapshot snapshot() {
        return SimulationSnapshot.from(crossroad);
    }
}
