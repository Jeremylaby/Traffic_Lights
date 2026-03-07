package pl.jeremy.model.trafficlights;

public class PolishTrafficLightStatePolicy implements TrafficLightStatePolicy {

    @Override
    public TrafficLightState nextState(TrafficLightState state) {
        return switch (state) {
            case RED -> TrafficLightState.GREEN_ARROW;
            case GREEN_ARROW -> TrafficLightState.RED_YELLOW;
            case RED_YELLOW -> TrafficLightState.GREEN;
            case GREEN -> TrafficLightState.YELLOW;
            case YELLOW -> TrafficLightState.RED;
        };
    }
}
