package pl.jeremy.model.trafficlights;

public class SimpleTrafficLightPolicy implements TrafficLightStatePolicy {
    /**
     * Simple 3-state traffic light program: RED → GREEN → YELLOW → RED.
     *
     * <p>If extended states are provided (e.g. GREEN_ARROW, RED_YELLOW), they are treated as equivalents of the base
     * state (RED) and mapped into the 3-state cycle.
     */
    @Override
    public TrafficLightState nextState(TrafficLightState state) {
        return switch (state) {
            case RED, GREEN_ARROW, RED_YELLOW -> TrafficLightState.GREEN;
            case GREEN -> TrafficLightState.YELLOW;
            case YELLOW -> TrafficLightState.RED;
        };
    }
}
