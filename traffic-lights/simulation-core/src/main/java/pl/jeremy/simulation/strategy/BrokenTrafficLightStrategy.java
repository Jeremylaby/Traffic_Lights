package pl.jeremy.simulation.strategy;

import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.road.RoadDirection;
import pl.jeremy.model.trafficlights.TrafficLightState;

public class BrokenTrafficLightStrategy implements TrafficLightStrategy {

    @Override
    public void configure(PolishCrossroad crossroad) {
        crossroad.getRoad(RoadDirection.NORTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.SOUTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.EAST).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.WEST).getTrafficLight().setState(TrafficLightState.GREEN);
    }

    @Override
    public void advanceTrafficLights(PolishCrossroad crossroad) {
        changeLights(crossroad);
    }

    /**
     * Simulates broken traffic lights mode. In this mode every approach stays GREEN all the time, and possible
     * conflicts are resolved by VehicleReleasePolicy.
     */
    private void changeLights(PolishCrossroad crossroad) {
        crossroad.getRoad(RoadDirection.NORTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.SOUTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.EAST).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.WEST).getTrafficLight().setState(TrafficLightState.GREEN);
    }
}
