package pl.jeremy.simulation.strategy;

import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.road.RoadDirection;
import pl.jeremy.model.road.SingleLaneRoad;
import pl.jeremy.model.trafficlights.TrafficLight;
import pl.jeremy.model.trafficlights.TrafficLightState;
import pl.jeremy.simulation.strategy.util.Axis;
import pl.jeremy.simulation.strategy.util.Phase;

public abstract class AbstractAxisTrafficLightStrategy implements TrafficLightStrategy {
    protected int currentGreenTime = 0;
    protected int currentSubstate = 0;
    protected Axis activeAxis = Axis.NORTH_SOUTH;
    protected Phase phase = Phase.GREEN;

    protected static void initializeCrossroad(PolishCrossroad crossroad) {
        crossroad.getRoad(RoadDirection.NORTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.SOUTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.EAST).getTrafficLight().setState(TrafficLightState.RED);
        crossroad.getRoad(RoadDirection.WEST).getTrafficLight().setState(TrafficLightState.RED);
    }

    protected void resetBaseState() {
        currentGreenTime = 0;
        currentSubstate = 0;
        activeAxis = Axis.NORTH_SOUTH;
        phase = Phase.GREEN;
    }

    protected int queueSize(PolishCrossroad crossroad, Axis axis) {
        return switch (axis) {
            case NORTH_SOUTH ->
                crossroad.getRoad(RoadDirection.NORTH).getEntanceLane().getVehicleCount()
                        + crossroad
                                .getRoad(RoadDirection.SOUTH)
                                .getEntanceLane()
                                .getVehicleCount();
            case EAST_WEST ->
                crossroad.getRoad(RoadDirection.EAST).getEntanceLane().getVehicleCount()
                        + crossroad.getRoad(RoadDirection.WEST).getEntanceLane().getVehicleCount();
        };
    }

    protected void convertRedLightsToGreenArrow(PolishCrossroad crossroad) {
        crossroad.getRoads().values().stream()
                .map(SingleLaneRoad::getTrafficLight)
                .filter(trafficLight -> trafficLight.getState() == TrafficLightState.RED)
                .forEach(TrafficLight::setNextState);
    }

    protected void advanceAllLights(PolishCrossroad crossroad) {
        crossroad.getRoads().values().stream()
                .map(SingleLaneRoad::getTrafficLight)
                .forEach(TrafficLight::setNextState);
    }

    protected void setNorthSouthGreenEastWestRed(PolishCrossroad crossroad) {
        crossroad.getRoad(RoadDirection.NORTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.SOUTH).getTrafficLight().setState(TrafficLightState.GREEN);
        crossroad.getRoad(RoadDirection.EAST).getTrafficLight().setState(TrafficLightState.RED);
        crossroad.getRoad(RoadDirection.WEST).getTrafficLight().setState(TrafficLightState.RED);
    }
}
