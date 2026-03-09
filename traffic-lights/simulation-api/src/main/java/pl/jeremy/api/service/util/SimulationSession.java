package pl.jeremy.api.service.util;

import pl.jeremy.simulation.CrossroadSimulation;
import pl.jeremy.simulation.factory.TrafficLightMode;

public record SimulationSession(String simulationId, TrafficLightMode strategy, CrossroadSimulation simulation) {}
