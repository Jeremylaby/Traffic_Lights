package pl.jeremy.model.road;

import pl.jeremy.model.trafficlights.TrafficLight;

public class SingleLaneRoad {
    private final RoadDirection roadDirection;
    private final Lane entanceLane;
    private final Lane exitLane;
    private final TrafficLight trafficLight;

    public SingleLaneRoad(RoadDirection roadDirection, TrafficLight trafficLight) {
        this.roadDirection = roadDirection;
        this.entanceLane = new Lane(LaneType.ENTRANCE);
        this.exitLane = new Lane(LaneType.EXIT);
        this.trafficLight = trafficLight;
    }

    public RoadDirection getRoadDirection() {
        return roadDirection;
    }

    public Lane getEntanceLane() {
        return entanceLane;
    }

    public Lane getExitLane() {
        return exitLane;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }
}
