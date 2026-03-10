import { ROAD_LENGTH, ROAD_WIDTH} from "./Road/config.ts";
import type {RoadDirection} from "@/types/simulation.ts";


export const INTERSECTION_SIZE = ROAD_WIDTH;
export const PAD = 10;

export const CROSSROAD_W = ROAD_LENGTH * 2 + INTERSECTION_SIZE + PAD * 2;
export const CROSSROAD_H = ROAD_LENGTH * 2 + INTERSECTION_SIZE + PAD * 2;

export const IX = PAD + ROAD_LENGTH;
export const IY = PAD + ROAD_LENGTH;


interface RoadConfig {
    direction: RoadDirection;
    x: number;
    y: number;
}

export const ROAD_CONFIGS: RoadConfig[] = [
    {
        direction: 'NORTH',
        x: IX,
        y: IY - ROAD_LENGTH,
    },
    {
        direction: 'SOUTH',
        x: IX,
        y: IY + INTERSECTION_SIZE,
    },
    {
        direction: 'EAST',
        x: IX + INTERSECTION_SIZE,
        y: IY - (ROAD_LENGTH - ROAD_WIDTH) / 2,
    },
    {
        direction: 'WEST',
        x: IX - ROAD_LENGTH,
        y: IY - (ROAD_LENGTH - ROAD_WIDTH) / 2,
    },
];



interface TrafficLightConfig {
    direction: RoadDirection;
    x: number;
    y: number;
    rotation: number;
}

const LIGHT_OFFSET = 4;
export const TRAFFIC_LIGHT_CONFIGS: TrafficLightConfig[] = [
    {
        direction: 'NORTH',
        x: IX - LIGHT_OFFSET,
        y: IY - LIGHT_OFFSET,
        rotation: 180,
    },
    {
        direction: 'SOUTH',
        x: IX + ROAD_LENGTH + LIGHT_OFFSET,
        y: IY + ROAD_LENGTH + LIGHT_OFFSET,
        rotation: 0,
    },
    {

        direction: 'EAST',
        x: IX + ROAD_LENGTH + LIGHT_OFFSET,
        y: IY - LIGHT_OFFSET,
        rotation: 270,
    },
    {
        direction: 'WEST',
        x: IX - LIGHT_OFFSET,
        y: IY + ROAD_LENGTH + LIGHT_OFFSET,
        rotation: 90,
    },
];