package pl.jeremy.model.trafficlights;

public interface TrafficLightStatePolicy {
    TrafficLightState nextState(TrafficLightState state);
}
