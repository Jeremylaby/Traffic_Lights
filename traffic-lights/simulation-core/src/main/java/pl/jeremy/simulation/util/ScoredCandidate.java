package pl.jeremy.simulation.util;

import pl.jeremy.model.vehicle.Vehicle;

public record ScoredCandidate(Vehicle candidate, int score) {}
