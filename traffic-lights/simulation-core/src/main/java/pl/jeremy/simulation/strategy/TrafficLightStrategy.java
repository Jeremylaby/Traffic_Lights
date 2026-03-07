package pl.jeremy.simulation.strategy;

import pl.jeremy.model.crossroad.PolishCrossroad;

public interface TrafficLightStrategy {
    void configure(PolishCrossroad crossroad);

    void advanceTrafficLights(PolishCrossroad crossroad);
}
