package pl.jeremy.config;

import pl.jeremy.simulation.strategy.AdaptiveTrafficLightStrategy;
import pl.jeremy.simulation.strategy.BrokenTrafficLightStrategy;
import pl.jeremy.simulation.strategy.FeedbackControlTrafficLightStrategy;
import pl.jeremy.simulation.strategy.ProportionalAllocationTrafficLightStrategy;
import pl.jeremy.simulation.strategy.SimpleTrafficLightStrategy;
import pl.jeremy.simulation.strategy.TrafficLightStrategy;

public class TrafficLightStrategyFactory {

    public static TrafficLightStrategy create(TrafficLightMode mode) {
        return switch (mode) {
            case SIMPLE -> new SimpleTrafficLightStrategy();
            case BROKEN -> new BrokenTrafficLightStrategy();
            case ADAPTIVE -> new AdaptiveTrafficLightStrategy();
            case FEEDBACK -> new FeedbackControlTrafficLightStrategy();
            case PROPORTIONAL -> new ProportionalAllocationTrafficLightStrategy();
        };
    }
}
