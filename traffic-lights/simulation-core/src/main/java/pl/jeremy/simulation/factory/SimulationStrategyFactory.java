package pl.jeremy.simulation.factory;

import pl.jeremy.simulation.CrossroadSimulation;
import pl.jeremy.simulation.PolishVehicleReleasePolicy;
import pl.jeremy.simulation.strategy.AdaptiveTrafficLightStrategy;
import pl.jeremy.simulation.strategy.BrokenTrafficLightStrategy;
import pl.jeremy.simulation.strategy.FeedbackControlTrafficLightStrategy;
import pl.jeremy.simulation.strategy.ProportionalAllocationTrafficLightStrategy;
import pl.jeremy.simulation.strategy.SimpleTrafficLightStrategy;

public class SimulationStrategyFactory {

    public static CrossroadSimulation create(TrafficLightMode mode) {
        return switch (mode) {
            case SIMPLE -> new CrossroadSimulation(new SimpleTrafficLightStrategy(), new PolishVehicleReleasePolicy());
            case BROKEN -> new CrossroadSimulation(new BrokenTrafficLightStrategy(), new PolishVehicleReleasePolicy());
            case ADAPTIVE ->
                new CrossroadSimulation(new AdaptiveTrafficLightStrategy(), new PolishVehicleReleasePolicy());
            case FEEDBACK ->
                new CrossroadSimulation(new FeedbackControlTrafficLightStrategy(), new PolishVehicleReleasePolicy());
            case PROPORTIONAL ->
                new CrossroadSimulation(
                        new ProportionalAllocationTrafficLightStrategy(), new PolishVehicleReleasePolicy());
        };
    }
}
