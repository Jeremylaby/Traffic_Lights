package pl.jeremy.model.crossroad;

import pl.jeremy.model.road.RoadDirection;
import pl.jeremy.model.road.RoadDto;

public record CrossroadDto(RoadDto northRoad, RoadDto southRoad, RoadDto eastRoad, RoadDto westRoad) {
    public static CrossroadDto from(PolishCrossroad crossroad) {
        return new CrossroadDto(
                RoadDto.from(crossroad.getRoad(RoadDirection.NORTH)),
                RoadDto.from(crossroad.getRoad(RoadDirection.SOUTH)),
                RoadDto.from(crossroad.getRoad(RoadDirection.EAST)),
                RoadDto.from(crossroad.getRoad(RoadDirection.WEST)));
    }
}
