package pl.jeremy.model.crossroad;

import java.util.HashMap;
import java.util.Map;
import pl.jeremy.model.road.RoadDirection;
import pl.jeremy.model.road.SingleLaneRoad;
import pl.jeremy.model.trafficlights.PolishTrafficLightStatePolicy;
import pl.jeremy.model.trafficlights.RoadTrafficLight;
import pl.jeremy.model.trafficlights.TrafficLightState;
import pl.jeremy.model.vehicle.Vehicle;

public class PolishCrossroad {
    private final HashMap<RoadDirection, SingleLaneRoad> roads;

    public PolishCrossroad() {
        this.roads = new HashMap<>();
        roads.put(
                RoadDirection.NORTH,
                new SingleLaneRoad(
                        RoadDirection.NORTH,
                        new RoadTrafficLight(new PolishTrafficLightStatePolicy(), TrafficLightState.GREEN)));
        roads.put(
                RoadDirection.SOUTH,
                new SingleLaneRoad(
                        RoadDirection.SOUTH,
                        new RoadTrafficLight(new PolishTrafficLightStatePolicy(), TrafficLightState.GREEN)));
        roads.put(
                RoadDirection.WEST,
                new SingleLaneRoad(
                        RoadDirection.WEST,
                        new RoadTrafficLight(new PolishTrafficLightStatePolicy(), TrafficLightState.RED)));
        roads.put(
                RoadDirection.EAST,
                new SingleLaneRoad(
                        RoadDirection.EAST,
                        new RoadTrafficLight(new PolishTrafficLightStatePolicy(), TrafficLightState.RED)));
    }

    public SingleLaneRoad getRoad(RoadDirection direction) {
        return roads.get(direction);
    }

    public Map<RoadDirection, SingleLaneRoad> getRoads() {
        return roads;
    }

    public void addVehicle(Vehicle vehicle) {
        SingleLaneRoad road = getRoad(vehicle.getRoute().startRoad());
        if (road != null) {
            road.getEntanceLane().addVehicle(vehicle);
        }
    }

    public void removeVehicle(Vehicle vehicle) {
        SingleLaneRoad road = getRoad(vehicle.getRoute().startRoad());
        if (road != null) {
            road.getEntanceLane().removeVehicle();
        }
    }
}
