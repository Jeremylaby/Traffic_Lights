package pl.jeremy.simulation.strategy;

import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.road.RoadDirection;
import pl.jeremy.model.road.SingleLaneRoad;
import pl.jeremy.model.trafficlights.TrafficLight;
import pl.jeremy.model.trafficlights.TrafficLightState;

public class SimpleTrafficLightStrategy implements TrafficLightStrategy {
    private static final int GREEN_TIME = 5;
    private static final int SUBSTATE_TIME = 1;
    private int currentGreenTime = 0;
    private int currentSubstate = 0;
    /**
     * Simulates a simple fixed-time traffic light controller. Each axis is served using constant green and transition
     * times, without adapting to current queue lengths.
     */
    @Override
    public void configure(PolishCrossroad crossroad) {
        crossroad.getRoad(RoadDirection.NORTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.SOUTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.EAST).getTrafficLight().setState(TrafficLightState.RED);
        crossroad.getRoad(RoadDirection.WEST).getTrafficLight().setState(TrafficLightState.RED);
    }

    @Override
    public void advanceTrafficLights(PolishCrossroad crossroad) {
        crossroad.getRoads().values().stream()
                .map(SingleLaneRoad::getTrafficLight)
                .forEach(this::changeLights);
        if (currentGreenTime < GREEN_TIME) {
            currentGreenTime++;
        } else {
            currentGreenTime = 0;
        }
        if (currentSubstate < SUBSTATE_TIME) {
            currentSubstate++;
        } else {
            currentSubstate = 0;
        }
    }

    private void changeLights(TrafficLight trafficLight) {
        if (trafficLight.getState() == TrafficLightState.RED) {
            trafficLight.setNextState();
        } else if ((trafficLight.getState() == TrafficLightState.GREEN
                        || trafficLight.getState() == TrafficLightState.GREEN_ARROW)
                && currentGreenTime >= GREEN_TIME) {
            trafficLight.setNextState();
        } else if ((trafficLight.getState() == TrafficLightState.YELLOW
                        || trafficLight.getState() == TrafficLightState.RED_YELLOW)
                && currentSubstate >= SUBSTATE_TIME) {
            trafficLight.setNextState();
        }
    }
}
