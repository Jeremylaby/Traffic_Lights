package pl.jeremy.simulation;

import pl.jeremy.model.crossroad.CrossroadDto;

public record SimulationSnapshot(int stepNumber, CrossroadDto crossroad) {}
