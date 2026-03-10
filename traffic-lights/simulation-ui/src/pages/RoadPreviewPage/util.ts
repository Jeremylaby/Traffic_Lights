import type {RoadDirection, VehicleDto} from "@/types/simulation.ts";
import {ROAD_WIDTH} from "@/components/crossroad/Road/config.ts";
import {CAR_GAP, CAR_H, MAX_VISIBLE} from "@/components/crossroad/VehicleQueue/config.ts";

function roadLength(vehicleCount: number): number {
    const queueLength = Math.min(vehicleCount, MAX_VISIBLE) * (CAR_H + CAR_GAP);
    return Math.max(160, queueLength + 40);
}

export function svgSize(dir: RoadDirection, vehicleCount: number): { w: number; h: number } {
    const length = roadLength(vehicleCount);
    const pad    = 20;
    const isVertical = dir === 'NORTH' || dir === 'SOUTH';
    return isVertical
        ? { w: ROAD_WIDTH + pad * 2, h: length + pad * 2 }
        : { w: length  + pad * 2,   h: ROAD_WIDTH + pad * 2 };
}

export function roadOrigin(dir: RoadDirection, vehicleCount: number): { x: number; y: number } {
    const length = roadLength(vehicleCount);
    const pad    = 20;
    const isVertical = dir === 'NORTH' || dir === 'SOUTH';

    if (isVertical) {
        return { x: pad, y: pad };
    }
    const svgCx = (length + pad * 2) / 2;
    const svgCy = (ROAD_WIDTH + pad * 2) / 2;
    return {
        x: svgCx - ROAD_WIDTH / 2,
        y: svgCy - length / 2,
    };
}

export function mockVehicles(dir: RoadDirection, count: number): VehicleDto[] {
    const movements = ['STRAIGHT', 'LEFT', 'RIGHT', 'STRAIGHT', 'STRAIGHT', 'STRAIGHT', 'LEFT'] as const;
    const ends: RoadDirection[] = ['NORTH', 'EAST', 'WEST', 'SOUTH', 'NORTH', 'EAST', 'WEST'];
    return Array.from({ length: count }, (_, i) => ({
        vehicleId: `v${i + 1}`,
        startRoad: dir,
        endRoad:   ends[i % ends.length],
        movement:  movements[i % movements.length],
    }));
}
