package pl.jeremy.model.trafficlights;

public interface TrafficLight {
    TrafficLightState getState();

    void setNextState();

    void setState(TrafficLightState state);
}
