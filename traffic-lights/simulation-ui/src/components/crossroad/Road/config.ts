import type {RoadDirection} from "@/types/simulation.ts";

export const LANE_WIDTH = 100;
export const ROAD_WIDTH = LANE_WIDTH * 2;

export const ASPHALT = '#1c1c1e';
export const LANE_DIVIDER = '#374151';
export const ROAD_BORDER = '#4b5563';
export const MARKING_COLOR = '#6b7280';
export const ROAD_LENGTH = 200;

export const ROAD_ROTATIONS: Record<RoadDirection, number> = {
    NORTH: 0,
    EAST: 90,
    SOUTH: 180,
    WEST: 270,
};