package pl.jeremy.model.trafficlights;

public class RoadTrafficLight implements TrafficLight {
    private TrafficLightState state;
    private final TrafficLightStatePolicy policy;

    public RoadTrafficLight(TrafficLightStatePolicy policy, TrafficLightState state) {
        this.policy = policy;
        this.state = state;
    }

    @Override
    public TrafficLightState getState() {
        return state;
    }

    @Override
    public void setNextState() {
        state = policy.nextState(state);
    }

    @Override
    public void setState(TrafficLightState state) {
        this.state = state;
    }
}
