package pl.jeremy.simulation.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.road.RoadDirection;
import pl.jeremy.model.road.SingleLaneRoad;
import pl.jeremy.model.trafficlights.TrafficLightState;
import pl.jeremy.model.vehicle.Vehicle;
import pl.jeremy.simulation.PolishVehicleReleasePolicy;

public class PolishVehicleReleasePolicyTest {

    // =========================
    // Case definitions (EDIT ME)
    // =========================
    private static final Set<RoadDirection> G4 =
            EnumSet.of(RoadDirection.NORTH, RoadDirection.SOUTH, RoadDirection.EAST, RoadDirection.WEST);

    private static final Set<RoadDirection> G3 =
            EnumSet.of(RoadDirection.NORTH, RoadDirection.SOUTH, RoadDirection.EAST);

    static Stream<Arguments> cases() {
        return Stream.of(
                        fourGreenNoUTurnCases(),
                        fourGreenUTurnCases(),
                        threeGreenNoUTurnCases(),
                        threeGreenUTurnCases())
                .flatMap(s -> s);
    }

    private static Stream<Arguments> fourGreenNoUTurnCases() {
        return Stream.of(
                arg(new Case(
                        "4 green: 4x straight", G4, allMoves(G4, Move.STRAIGHT), Set.of("N_STRAIGHT", "S_STRAIGHT"))),
                arg(new Case("4 green: 4x left", G4, allMoves(G4, Move.LEFT), Set.of("N_LEFT", "S_LEFT"))),
                arg(new Case(
                        "4 green: 4x right",
                        G4,
                        allMoves(G4, Move.RIGHT),
                        Set.of("N_RIGHT", "S_RIGHT", "E_RIGHT", "W_RIGHT"))),
                arg(new Case(
                        "4 green: 3x left + N straight",
                        G4,
                        withOverride(allMoves(G4, Move.LEFT), RoadDirection.NORTH, Move.STRAIGHT),
                        Set.of("W_LEFT"))),
                arg(new Case(
                        "4 green: 3x straight + N left",
                        G4,
                        withOverride(allMoves(G4, Move.STRAIGHT), RoadDirection.NORTH, Move.LEFT),
                        Set.of("E_STRAIGHT"))),
                arg(new Case(
                        "4 green: 3x straight + N right",
                        G4,
                        withOverride(allMoves(G4, Move.STRAIGHT), RoadDirection.NORTH, Move.RIGHT),
                        Set.of("N_RIGHT"))),
                arg(new Case(
                        "4 green: 3x right + N straight",
                        G4,
                        withOverride(allMoves(G4, Move.RIGHT), RoadDirection.NORTH, Move.STRAIGHT),
                        Set.of("E_RIGHT", "S_RIGHT", "W_RIGHT"))),
                arg(new Case(
                        "4 green: 3x right + N left",
                        G4,
                        withOverride(allMoves(G4, Move.RIGHT), RoadDirection.NORTH, Move.LEFT),
                        Set.of("E_RIGHT", "S_RIGHT", "W_RIGHT"))),
                arg(new Case(
                        "4 green: 3x left + N right",
                        G4,
                        withOverride(allMoves(G4, Move.LEFT), RoadDirection.NORTH, Move.RIGHT),
                        Set.of("N_RIGHT", "E_LEFT"))));
    }

    private static Stream<Arguments> fourGreenUTurnCases() {
        return Stream.of(
                arg(new Case("4 green: 4x uturn", G4, allMoves(G4, Move.UTURN), Set.of("N_UTURN", "S_UTURN"))),

                // 3U + N X (odd always NORTH)
                arg(new Case(
                        "4 green: 3x uturn + N right",
                        G4,
                        withOverride(allMoves(G4, Move.UTURN), RoadDirection.NORTH, Move.RIGHT),
                        Set.of("N_RIGHT"))),
                arg(new Case(
                        "4 green: 3x uturn + N straight",
                        G4,
                        withOverride(allMoves(G4, Move.UTURN), RoadDirection.NORTH, Move.STRAIGHT),
                        Set.of("N_STRAIGHT"))),
                arg(new Case(
                        "4 green: 3x uturn + N left",
                        G4,
                        withOverride(allMoves(G4, Move.UTURN), RoadDirection.NORTH, Move.LEFT),
                        Set.of("N_LEFT"))),

                // 3X + N U (odd always NORTH)
                arg(new Case(
                        "4 green: 3x right + N uturn",
                        G4,
                        withOverride(allMoves(G4, Move.RIGHT), RoadDirection.NORTH, Move.UTURN),
                        Set.of("S_RIGHT", "E_RIGHT", "W_RIGHT"))),
                arg(new Case(
                        "4 green: 3x straight + N uturn",
                        G4,
                        withOverride(allMoves(G4, Move.STRAIGHT), RoadDirection.NORTH, Move.UTURN),
                        Set.of("E_STRAIGHT"))),
                arg(new Case(
                        "4 green: 3x left + N uturn",
                        G4,
                        withOverride(allMoves(G4, Move.LEFT), RoadDirection.NORTH, Move.UTURN),
                        Set.of("E_LEFT"))),

                // 2U + 2X  (N,S=U ; E,W=X)
                arg(new Case(
                        "4 green: 2x uturn + 2x right",
                        G4,
                        moves4(Move.UTURN, Move.UTURN, Move.RIGHT, Move.RIGHT),
                        Set.of("E_RIGHT", "W_RIGHT"))),
                arg(new Case(
                        "4 green: 2x uturn + 2x straight",
                        G4,
                        moves4(Move.UTURN, Move.UTURN, Move.STRAIGHT, Move.STRAIGHT),
                        Set.of("E_STRAIGHT", "W_STRAIGHT"))),
                arg(new Case(
                        "4 green: 2x uturn + 2x left",
                        G4,
                        moves4(Move.UTURN, Move.UTURN, Move.LEFT, Move.LEFT),
                        Set.of("E_LEFT", "W_LEFT"))),
                arg(new Case(
                        "4 green: 2x right + 2x left",
                        G4,
                        moves4(Move.LEFT, Move.LEFT, Move.RIGHT, Move.RIGHT),
                        Set.of("N_LEFT", "E_RIGHT", "S_LEFT", "W_RIGHT"))),

                // 2U + X + Y (N,S=U ; E=X ; W=Y)
                arg(new Case(
                        "4 green: 2x uturn + right + straight",
                        G4,
                        moves4(Move.UTURN, Move.UTURN, Move.RIGHT, Move.STRAIGHT),
                        Set.of("E_RIGHT", "W_STRAIGHT"))),
                arg(new Case(
                        "4 green: 2x uturn + right + left",
                        G4,
                        moves4(Move.UTURN, Move.UTURN, Move.RIGHT, Move.LEFT),
                        Set.of("E_RIGHT"))),
                arg(new Case(
                        "4 green: 2x uturn + straight + left",
                        G4,
                        moves4(Move.UTURN, Move.UTURN, Move.STRAIGHT, Move.LEFT),
                        Set.of("E_STRAIGHT"))),

                // 2X + 1U + 1Y (N=U ; S=X ; E=X ; W=Y)
                arg(new Case(
                        "4 green: 2x right + 1x uturn + 1x straight",
                        G4,
                        moves4(Move.UTURN, Move.RIGHT, Move.RIGHT, Move.STRAIGHT),
                        Set.of("S_RIGHT", "E_RIGHT"))),
                arg(new Case(
                        "4 green: 2x right + 1x uturn + 1x left",
                        G4,
                        moves4(Move.UTURN, Move.RIGHT, Move.RIGHT, Move.LEFT),
                        Set.of("S_RIGHT", "E_RIGHT"))),
                arg(new Case(
                        "4 green: 2x straight + 1x uturn + 1x right",
                        G4,
                        moves4(Move.UTURN, Move.STRAIGHT, Move.STRAIGHT, Move.RIGHT),
                        Set.of("E_STRAIGHT", "W_RIGHT"))),
                arg(new Case(
                        "4 green: 2x straight + 1x uturn + 1x left",
                        G4,
                        moves4(Move.UTURN, Move.STRAIGHT, Move.STRAIGHT, Move.LEFT),
                        Set.of("E_STRAIGHT"))),
                arg(new Case(
                        "4 green: 2x left + 1x uturn + 1x right",
                        G4,
                        moves4(Move.UTURN, Move.LEFT, Move.LEFT, Move.RIGHT),
                        Set.of("W_RIGHT"))),
                arg(new Case(
                        "4 green: 2x left + 1x uturn + 1x straight",
                        G4,
                        moves4(Move.UTURN, Move.LEFT, Move.LEFT, Move.STRAIGHT),
                        Set.of("S_LEFT"))));
    }

    private static Stream<Arguments> threeGreenNoUTurnCases() {
        return Stream.of(
                arg(new Case("3 green (N,S,E): 3x straight", G3, allMoves(G3, Move.STRAIGHT), Set.of("N_STRAIGHT"))),
                arg(new Case("3 green (N,S,E): 3x left", G3, allMoves(G3, Move.LEFT), Set.of("N_LEFT"))),
                arg(new Case(
                        "3 green (N,S,E): 3x right",
                        G3,
                        allMoves(G3, Move.RIGHT),
                        Set.of("N_RIGHT", "S_RIGHT", "E_RIGHT"))),
                arg(new Case(
                        "3 green (N,S,E): 2x left + N straight",
                        G3,
                        withOverride(allMoves(G3, Move.LEFT), RoadDirection.NORTH, Move.STRAIGHT),
                        Set.of("N_STRAIGHT"))),
                arg(new Case(
                        "3 green (N,S,E): 2x straight + N left",
                        G3,
                        withOverride(allMoves(G3, Move.STRAIGHT), RoadDirection.NORTH, Move.LEFT),
                        Set.of("S_STRAIGHT"))),
                arg(new Case(
                        "3 green (N,S,E): 2x right + N straight",
                        G3,
                        withOverride(allMoves(G3, Move.RIGHT), RoadDirection.NORTH, Move.STRAIGHT),
                        Set.of("N_STRAIGHT", "S_RIGHT", "E_RIGHT"))),
                arg(new Case(
                        "3 green (N,S,E): 2x straight + N right",
                        G3,
                        withOverride(allMoves(G3, Move.STRAIGHT), RoadDirection.NORTH, Move.RIGHT),
                        Set.of("N_RIGHT"))));
    }

    private static Stream<Arguments> threeGreenUTurnCases() {
        return Stream.of(
                arg(new Case("3 green (N,S,E): 3x uturn", G3, allMoves(G3, Move.UTURN), Set.of("N_UTURN"))),
                arg(new Case(
                        "3 green (N,S,E): 2x right + N uturn",
                        G3,
                        moves3(Move.UTURN, Move.RIGHT, Move.RIGHT),
                        Set.of("S_RIGHT", "E_RIGHT"))),
                arg(new Case(
                        "3 green (N,S,E): 2x straight + N uturn",
                        G3,
                        moves3(Move.UTURN, Move.STRAIGHT, Move.STRAIGHT),
                        Set.of("E_STRAIGHT"))),
                arg(new Case(
                        "3 green (N,S,E): 2x left + N uturn",
                        G3,
                        moves3(Move.UTURN, Move.LEFT, Move.LEFT),
                        Set.of("E_LEFT"))),
                arg(new Case(
                        "3 green (N,S,E): uturn + right + straight (N=U,S=R,E=S)",
                        G3,
                        moves3(Move.UTURN, Move.RIGHT, Move.STRAIGHT),
                        Set.of("S_RIGHT", "E_STRAIGHT"))),
                arg(new Case(
                        "3 green (N,S,E): uturn + right + left (N=U,S=R,E=L)",
                        G3,
                        moves3(Move.UTURN, Move.RIGHT, Move.LEFT),
                        Set.of("S_RIGHT", "E_LEFT"))),
                arg(new Case(
                        "3 green (N,S,E): uturn + straight + left (N=U,S=S,E=L)",
                        G3,
                        moves3(Move.UTURN, Move.STRAIGHT, Move.LEFT),
                        Set.of("E_LEFT"))));
    }

    private static Arguments arg(Case c) {
        return Arguments.of(c.name(), c);
    }

    // =========================
    // Helpers: lights, routes
    // =========================
    private static Map<RoadDirection, Move> moves4(Move n, Move s, Move e, Move w) {
        Map<RoadDirection, Move> m = new EnumMap<>(RoadDirection.class);
        m.put(RoadDirection.NORTH, n);
        m.put(RoadDirection.SOUTH, s);
        m.put(RoadDirection.EAST, e);
        m.put(RoadDirection.WEST, w);
        return m;
    }

    private static Map<RoadDirection, Move> moves3(Move n, Move s, Move e) {
        Map<RoadDirection, Move> m = new EnumMap<>(RoadDirection.class);
        m.put(RoadDirection.NORTH, n);
        m.put(RoadDirection.SOUTH, s);
        m.put(RoadDirection.EAST, e);
        return m;
    }

    private static void setLights(PolishCrossroad crossroad, Set<RoadDirection> greenRoads) {
        for (SingleLaneRoad r : crossroad.getRoads().values()) {
            r.getTrafficLight().setState(TrafficLightState.RED);
        }
        for (RoadDirection d : greenRoads) {
            crossroad.getRoad(d).getTrafficLight().setState(TrafficLightState.GREEN);
        }
    }

    private static RoadDirection end(RoadDirection start, Move move) {
        return switch (move) {
            case RIGHT -> start.left(); // your "right" convention
            case LEFT -> start.right(); // your "left" convention
            case STRAIGHT -> start.opposite();
            case UTURN -> start;
        };
    }

    private static String id(RoadDirection dir, Move move) {
        return dir.name().charAt(0) + "_" + move.name(); // e.g. N_STRAIGHT
    }

    private static Map<RoadDirection, Move> allMoves(Set<RoadDirection> dirs, Move move) {
        Map<RoadDirection, Move> m = new EnumMap<>(RoadDirection.class);
        for (RoadDirection d : dirs) {
            m.put(d, move);
        }
        return m;
    }

    private static Map<RoadDirection, Move> withOverride(Map<RoadDirection, Move> base, RoadDirection d, Move move) {
        Map<RoadDirection, Move> m = new EnumMap<>(base);
        m.put(d, move);
        return m;
    }

    private static String prettyMoves(Map<RoadDirection, Move> moves, Set<RoadDirection> dirs) {
        List<RoadDirection> sorted = new ArrayList<>(dirs);
        sorted.sort(Comparator.comparingInt(Enum::ordinal));
        StringBuilder sb = new StringBuilder("{");
        for (RoadDirection d : sorted) {
            sb.append(d.name().charAt(0)).append("=").append(moves.get(d)).append(", ");
        }
        if (!sorted.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    // ==========================================
    // Core: invoke move + collect "left vehicles"
    // ==========================================
    private static Set<String> invokeMoveAndCollectLeft(
            PolishVehicleReleasePolicy vm, PolishCrossroad crossroad, Map<RoadDirection, String> idByRoad) {
        // Snapshot (1 vehicle per green road => if lane becomes empty, that vehicle "left")
        return vm.selectVehiclesToRelease(crossroad).stream()
                .map(PolishVehicleReleasePolicyTest::vehicleId)
                .collect(Collectors.toSet());
    }

    private static Object invokeMove(PolishVehicleReleasePolicy vm, PolishCrossroad crossroad) {
        // supports both "moveVehicles" and (if you rename) "moveCars"
        for (String methodName : List.of("moveVehicles", "moveCars")) {
            try {
                Method m = vm.getClass().getDeclaredMethod(methodName, PolishCrossroad.class);
                m.setAccessible(true);
                return m.invoke(vm, crossroad);
            } catch (NoSuchMethodException ignored) {
                // try next name
            } catch (Exception e) {
                throw new RuntimeException("Cannot invoke " + methodName + "(PolishCrossroad)", e);
            }
        }
        fail("No method moveVehicles(PolishCrossroad) / moveCars(PolishCrossroad) found on VehicleManager.");
        return null;
    }

    private static String vehicleId(Vehicle v) {
        // record accessor "id()" or getter "getId()"/"getVehicleId()"
        for (String mName : List.of("id", "getId", "getVehicleId", "vehicleId")) {
            try {
                Method m = v.getClass().getMethod(mName);
                Object out = m.invoke(v);
                if (out != null) {
                    return out.toString();
                }
            } catch (Exception ignored) {
            }
        }
        // fallback: field "id"
        try {
            Field f = v.getClass().getDeclaredField("id");
            f.setAccessible(true);
            Object out = f.get(v);
            if (out != null) {
                return out.toString();
            }
        } catch (Exception ignored) {
        }
        return v.toString();
    }

    private static String extractIdByReflection(Object o) {
        for (String mName : List.of("id", "getId", "getVehicleId", "vehicleId")) {
            try {
                Method m = o.getClass().getMethod(mName);
                Object out = m.invoke(o);
                if (out != null) {
                    return out.toString();
                }
            } catch (Exception ignored) {
            }
        }
        return o.toString();
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("cases")
    void selectVehiclesShouldReturnExpectedVehiclesToRelease(String displayName, Case c) {
        // Arrange
        PolishCrossroad crossroad = new PolishCrossroad();
        setLights(crossroad, c.greenRoads());

        // Put exactly 1 vehicle on every green approach
        Map<RoadDirection, String> idByRoad = new EnumMap<>(RoadDirection.class);
        for (RoadDirection dir : c.greenRoads()) {
            Move move = c.movesByRoad().get(dir);
            assertNotNull(move, "Missing move for road=" + dir + " in case=" + c.name());

            String id = id(dir, move);
            idByRoad.put(dir, id);

            Vehicle v = new Vehicle(id, dir, end(dir, move));
            crossroad.addVehicle(v);
        }

        PolishVehicleReleasePolicy vm = new PolishVehicleReleasePolicy();

        // Act
        Set<String> actualLeftIds = invokeMoveAndCollectLeft(vm, crossroad, idByRoad);

        // Assert
        String msg = "case=" + c.name()
                + " greenRoads=" + c.greenRoads()
                + " moves=" + prettyMoves(c.movesByRoad(), c.greenRoads())
                + " expected=" + c.expectedLeftIds()
                + " actual=" + actualLeftIds;

        assertEquals(c.expectedLeftIds(), actualLeftIds, msg);

        // Also verify: every returned vehicle is actually removed from entrance lane (basic sanity)
        // logic changed now it only pick Vehicles to remove
        //        for (RoadDirection d : c.greenRoads()) {
        //            String id = idByRoad.get(d);
        //            boolean shouldHaveLeft = actualLeftIds.contains(id);
        //
        //            boolean laneEmpty = crossroad.getRoad(d).getEntanceLane().isEmpty();
        //            if (shouldHaveLeft) {
        //                assertTrue(laneEmpty, "Returned as left but still on entrance lane. " + msg + " road=" + d);
        //            }
        //        }
    }

    enum Move {
        RIGHT,
        STRAIGHT,
        LEFT,
        UTURN
    }

    record Case(
            String name,
            Set<RoadDirection> greenRoads,
            Map<RoadDirection, Move> movesByRoad,
            Set<String> expectedLeftIds) {}
}
