package pl.jeremy.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.road.Lane;
import pl.jeremy.model.road.RoadDirection;
import pl.jeremy.model.road.SingleLaneRoad;
import pl.jeremy.model.trafficlights.TrafficLightState;
import pl.jeremy.model.vehicle.Vehicle;
import pl.jeremy.simulation.util.ScoredCandidate;

public class PolishVehicleReleasePolicy implements VehicleReleasePolicy {
    /** I hope it works... */
    private boolean preferNorthSouthAxis = true;

    public List<Vehicle> selectVehiclesToRelease(PolishCrossroad crossroad) {
        // 1) Collect candidates = first vehicles from each GREEN approach (max 4)
        List<Vehicle> waitingOnGreen = selectGreenCandidates(crossroad);
        List<Vehicle> waitingOnGreenArrow = selectGreenArrowCandidates(crossroad);
        List<Vehicle> greenArrowNonConflicting = waitingOnGreenArrow.stream()
                .filter(arrowVehicle -> waitingOnGreen.stream()
                        .noneMatch(greenVehicle -> arrowVehicle.getRoute().isColliding(greenVehicle.getRoute())))
                .toList();
        return resolveCandidates(waitingOnGreen, greenArrowNonConflicting);
    }

    private static List<Vehicle> selectGreenArrowCandidates(PolishCrossroad crossroad) {
        return crossroad.getRoads().values().stream()
                .filter(singleLaneRoad -> singleLaneRoad.getTrafficLight().getState() == TrafficLightState.GREEN_ARROW)
                .map(SingleLaneRoad::getEntanceLane)
                .map(Lane::peekFirstVehicle)
                .filter(Objects::nonNull)
                .filter(vehicle -> vehicle.getRoute().isRightTurn())
                .toList();
    }

    private static List<Vehicle> selectGreenCandidates(PolishCrossroad crossroad) {
        return crossroad.getRoads().values().stream()
                .filter(singleLaneRoad -> singleLaneRoad.getTrafficLight().getState() == TrafficLightState.GREEN)
                .map(SingleLaneRoad::getEntanceLane)
                .map(Lane::peekFirstVehicle)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Vehicle> resolveCandidates(List<Vehicle> waitingOnGreen, List<Vehicle> greenArrowNonConflicting) {
        if (waitingOnGreen.isEmpty()) {
            return greenArrowNonConflicting;
        }
        List<Vehicle> left;
        // 2) Score each candidate: +1 for every other candidate it has priority over
        if (waitingOnGreen.size() == 4) {
            left = handleFourCandidates(waitingOnGreen);
        } else if (waitingOnGreen.size() == 3) {
            left = handleThreeCandidates(waitingOnGreen);
        } else if (waitingOnGreen.size() == 2) {
            left = handleTwoCandidates(waitingOnGreen);
        } else {
            left = waitingOnGreen;
        }
        left = Stream.concat(left.stream(), greenArrowNonConflicting.stream()).toList();
        return left;
    }

    private List<ScoredCandidate> scoreCandidates(List<Vehicle> candidates) {
        List<ScoredCandidate> out = new ArrayList<>(candidates.size());

        for (int i = 0; i < candidates.size(); i++) {
            Vehicle vehicle = candidates.get(i);
            int score = 0;

            for (int j = 0; j < candidates.size(); j++) {
                if (i == j) {
                    continue;
                }
                Vehicle other = candidates.get(j);
                if (vehicle.getRoute().priority(other.getRoute())) {
                    score++;
                }
            }
            out.add(new ScoredCandidate(vehicle, score));
        }
        return out;
    }

    private List<Vehicle> handleFourCandidates(List<Vehicle> candidates) {
        List<ScoredCandidate> scored = scoreCandidates(candidates);
        if (scored.stream().anyMatch(s -> s.score() == 3)) {
            return scored.stream()
                    .filter(sc -> sc.score() == 3)
                    .map(ScoredCandidate::candidate)
                    .toList();
        }
        if (scored.stream().allMatch(s -> s.candidate().getRoute().isUTurn())
                || scored.stream().allMatch(s -> s.candidate().getRoute().isLeftTurn())
                || scored.stream().allMatch(s -> s.candidate().getRoute().isStraight())) {

            List<Vehicle> ns = candidates.stream()
                    .filter(c -> c.getStartRoad() == RoadDirection.NORTH || c.getStartRoad() == RoadDirection.SOUTH)
                    .toList();
            List<Vehicle> we = candidates.stream()
                    .filter(c -> c.getStartRoad() == RoadDirection.WEST || c.getStartRoad() == RoadDirection.EAST)
                    .toList();

            if (preferNorthSouthAxis) {
                preferNorthSouthAxis = false;
                return ns;
            }
            preferNorthSouthAxis = true;
            return we;
        }

        List<Vehicle> remaining = scored.stream()
                .filter(sc -> sc.score() > 1)
                .map(ScoredCandidate::candidate)
                .toList();
        if (remaining.size() == 3) {
            return handleThreeCandidates(remaining);
        }
        if (remaining.size() == 2) {
            return handleTwoCandidates(remaining);
        }

        System.out.println(remaining.size());
        return remaining;
    }

    private List<Vehicle> handleThreeCandidates(List<Vehicle> candidates) {
        List<ScoredCandidate> scored3 = scoreCandidates(candidates);

        // If all three are right turns, allow all (your simplified rule).
        if (scored3.stream().anyMatch(s -> s.score() == 2)) {
            return scored3.stream()
                    .filter(sc -> sc.score() == 2)
                    .map(ScoredCandidate::candidate)
                    .toList();
        }
        List<Vehicle> remaining;
        if (scored3.size() == 3 && scored3.stream().allMatch(sc -> sc.score() == 1)) {
            List<Vehicle> vs = scored3.stream().map(ScoredCandidate::candidate).toList();

            List<Vehicle> ns = vs.stream()
                    .filter(v -> v.getRoute().startRoad() == RoadDirection.NORTH
                            || v.getRoute().startRoad() == RoadDirection.SOUTH)
                    .toList();

            List<Vehicle> we = vs.stream()
                    .filter(v -> v.getRoute().startRoad() == RoadDirection.WEST
                            || v.getRoute().startRoad() == RoadDirection.EAST)
                    .toList();
            if (preferNorthSouthAxis) {
                preferNorthSouthAxis = false;
                remaining = ns;
            } else {
                preferNorthSouthAxis = true;
                remaining = we;
            }

        } else {

            // Remove candidates with the minimal score (could be 1 element or more).
            int minScore =
                    scored3.stream().mapToInt(ScoredCandidate::score).min().orElseThrow();

            remaining = scored3.stream()
                    .filter(sc -> sc.score() != minScore)
                    .map(ScoredCandidate::candidate)
                    .toList();
        }

        // If we ended up with exactly 2, handle the 2-candidate conflict case.
        if (remaining.size() == 2) {
            return handleTwoCandidates(remaining);
        }
        return remaining;
    }

    private List<Vehicle> handleTwoCandidates(List<Vehicle> candidates) {
        if (candidates.size() != 2) {
            return candidates;
        }

        Vehicle a = candidates.get(0);
        Vehicle b = candidates.get(1);

        RoadDirection sa = a.getRoute().startRoad();
        RoadDirection sb = b.getRoute().startRoad();
        if (candidates.stream().allMatch(c -> c.getRoute().isUTurn() && sa.opposite() == sb)) {
            return candidates;
        }
        return scoreCandidates(candidates).stream()
                .filter(sc -> sc.score() >= 1)
                .map(ScoredCandidate::candidate)
                .toList();
    }
}
