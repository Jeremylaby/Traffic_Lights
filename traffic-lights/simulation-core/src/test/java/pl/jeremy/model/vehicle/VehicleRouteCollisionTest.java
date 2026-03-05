package pl.jeremy.model.vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.jeremy.model.road.RoadDirection;

public class VehicleRouteCollisionTest {

    // --- All 16 possible routes (including U-turns start==end) ---
    private static final VehicleRoute N_S = r(RoadDirection.NORTH, RoadDirection.SOUTH); // straight
    private static final VehicleRoute N_W = r(RoadDirection.NORTH, RoadDirection.WEST); // right
    private static final VehicleRoute N_E = r(RoadDirection.NORTH, RoadDirection.EAST); // left
    private static final VehicleRoute N_N = r(RoadDirection.NORTH, RoadDirection.NORTH); // U-turn

    private static final VehicleRoute S_N = r(RoadDirection.SOUTH, RoadDirection.NORTH); // straight
    private static final VehicleRoute S_E = r(RoadDirection.SOUTH, RoadDirection.EAST); // right
    private static final VehicleRoute S_W = r(RoadDirection.SOUTH, RoadDirection.WEST); // left
    private static final VehicleRoute S_S = r(RoadDirection.SOUTH, RoadDirection.SOUTH); // U-turn

    private static final VehicleRoute E_W = r(RoadDirection.EAST, RoadDirection.WEST); // straight
    private static final VehicleRoute E_N = r(RoadDirection.EAST, RoadDirection.NORTH); // right
    private static final VehicleRoute E_S = r(RoadDirection.EAST, RoadDirection.SOUTH); // left
    private static final VehicleRoute E_E = r(RoadDirection.EAST, RoadDirection.EAST); // U-turn

    private static final VehicleRoute W_E = r(RoadDirection.WEST, RoadDirection.EAST); // straight
    private static final VehicleRoute W_S = r(RoadDirection.WEST, RoadDirection.SOUTH); // right
    private static final VehicleRoute W_N = r(RoadDirection.WEST, RoadDirection.NORTH); // left
    private static final VehicleRoute W_W = r(RoadDirection.WEST, RoadDirection.WEST); // U-turn

    private static VehicleRoute r(RoadDirection start, RoadDirection end) {
        return new VehicleRoute(start, end);
    }

    /**
     * Assumptions for the expected matrix: 1) Any pair involving a U-turn is considered colliding (conservative/safe).
     * 2) Non-colliding pairs are limited to: - Opposite approaches on the same axis, where both movements are straight
     * or right-turn - Perpendicular right-turns (right-turns from different approaches)
     *
     * <p>If you want to relax rules (e.g., allow some U-turn combinations), add such pairs here.
     */
    private static final Set<RoutePair> NON_COLLIDING = Set.of(
            // --- Same approach (lane arrows / dedicated lanes): S + R + L do not collide ---
            // Single lane road is cladding
            //            pair(N_S, N_W),
            //            pair(N_S, N_E),
            //            pair(N_W, N_E),
            //
            //            pair(S_N, S_E),
            //            pair(S_N, S_W),
            //            pair(S_E, S_W),
            //
            //            pair(E_W, E_N),
            //            pair(E_W, E_S),
            //            pair(E_N, E_S),
            //
            //            pair(W_E, W_S),
            //            pair(W_E, W_N),
            //            pair(W_S, W_N),
            // --- Left-turn + right-turn which don't end on the same road or starts from same road ---
            pair(N_W, W_N),
            pair(N_E, E_N),
            pair(S_E, E_S),
            pair(S_W, W_S),

            // --- Opposite approaches: straight/right vs straight/right do not collide ---
            pair(N_S, S_N),
            pair(N_S, S_E),
            pair(N_W, S_N),
            pair(N_W, S_E),
            pair(E_W, W_E),
            pair(E_W, W_S),
            pair(E_N, W_E),
            pair(E_N, W_S),

            // --- Opposite approaches: left vs left does not collide ---
            pair(N_E, S_W),
            pair(E_S, W_N),

            // --- Straight + right-turn from perpendicular approaches (all combinations) ---
            pair(N_S, E_N),
            // Single lane road is cladding
            //            pair(N_S, W_S),
            //            pair(S_N, E_N),
            pair(S_N, W_S),
            // Single lane road is cladding
            //            pair(E_W, N_W),
            pair(E_W, S_E),
            pair(W_E, N_W),
            // Single lane road is cladding
            //            pair(W_E, S_E),

            // --- Right-turns among themselves (perpendicular) ---
            // (opposite right-turn pairs are already covered above: N_W-S_E and E_N-W_S)
            pair(N_W, E_N),
            pair(N_W, W_S),
            pair(S_E, E_N),
            pair(S_E, W_S),

            // --- Left + right from perpendicular approaches (no corner conflict in this simplified model) ---
            pair(N_E, W_S),
            pair(S_W, E_N),
            pair(E_S, N_W),
            pair(W_N, S_E));

    @Test
    void uTurnShouldCollideWithOppositeStraight_inThisModel() {
        // conservative expectation: U-turn is always colliding
        assertTrue(collides(N_N, S_N));
        assertTrue(collides(E_E, W_E));
    }

    @Test
    void oppositeStraightShouldNotCollide() {
        // matches the typical interpretation: two opposite straight movements can pass simultaneously
        assertFalse(collides(S_N, N_S));
        assertFalse(collides(W_E, E_W));
    }

    @ParameterizedTest(name = "{index}: {0} vs {1} -> colliding={2}")
    @MethodSource("allRoutePairsWithExpected")
    void shouldMatchExpectedCollisionMatrix(VehicleRoute a, VehicleRoute b, boolean expectedColliding) {
        boolean actual = collides(a, b);

        assertEquals(
                expectedColliding,
                actual,
                "FAILED for " + fmt(a) + " vs " + fmt(b)
                        + " | expectedColliding=" + expectedColliding
                        + " | actual=" + actual);
    }

    @ParameterizedTest(name = "{index}: symmetry {0} <-> {1}")
    @MethodSource("allRoutePairs")
    void shouldBeSymmetric(VehicleRoute a, VehicleRoute b) {
        boolean ab = collides(a, b);
        boolean ba = collides(b, a);

        assertEquals(ab, ba, "SYMMETRY FAILED for " + fmt(a) + " <-> " + fmt(b) + " | a->b=" + ab + " | b->a=" + ba);
    }

    private static Stream<Arguments> allRoutePairsWithExpected() {
        return allRoutePairs().map(args -> {
            VehicleRoute a = (VehicleRoute) args.get()[0];
            VehicleRoute b = (VehicleRoute) args.get()[1];

            boolean expectedColliding;
            if (isUTurn(a) && isUTurn(b)) {
                expectedColliding = false; // rule #1
            } else {
                expectedColliding = !NON_COLLIDING.contains(pair(a, b)); // rule #2
            }

            return Arguments.of(a, b, expectedColliding);
        });
    }

    private static boolean isUTurn(VehicleRoute r) {
        return r.startRoad() == r.endRoad();
    }

    private static Stream<Arguments> allRoutePairs() {
        List<VehicleRoute> all =
                List.of(N_S, N_W, N_E, N_N, S_N, S_E, S_W, S_S, E_W, E_N, E_S, E_E, W_E, W_S, W_N, W_W);

        List<Arguments> pairs = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            for (int j = i + 1; j < all.size(); j++) {
                pairs.add(Arguments.of(all.get(i), all.get(j)));
            }
        }
        return pairs.stream();
    }

    // --- helper: unordered pair (so A-B == B-A) ---
    private record RoutePair(VehicleRoute a, VehicleRoute b) {}

    private static RoutePair pair(VehicleRoute a, VehicleRoute b) {
        int ka = key(a);
        int kb = key(b);
        return (ka <= kb) ? new RoutePair(a, b) : new RoutePair(b, a);
    }

    private static int key(VehicleRoute r) {
        return r.startRoad().ordinal() * 10 + r.endRoad().ordinal();
    }

    private static boolean collides(VehicleRoute a, VehicleRoute b) {
        return a.isColliding(b);
    }

    private static String fmt(VehicleRoute r) {
        return r.startRoad() + "->" + r.endRoad();
    }
}
