import type {RoadDirection, RoadDto} from '@/types/simulation.ts';
import {Road} from '@/components/crossroad/Road/Road.tsx';
import TrafficLight from '@/components/crossroad/TrafficLight/TrafficLight.tsx';
import {ASPHALT, ROAD_BORDER} from './Road/config.ts';
import {CROSSROAD_H, CROSSROAD_W, INTERSECTION_SIZE, IX, IY, ROAD_CONFIGS, TRAFFIC_LIGHT_CONFIGS} from "./config.ts";

interface CrossroadProps {
    north: RoadDto;
    south: RoadDto;
    east: RoadDto;
    west: RoadDto;
    broken?: boolean;
}


export function Crossroad({north, south, east, west, broken = false}: CrossroadProps) {
    const roadMap: Record<RoadDirection, RoadDto> = {
        NORTH: north,
        SOUTH: south,
        EAST: east,
        WEST: west,
    }
    return (
        <svg
            width={CROSSROAD_W}
            height={CROSSROAD_H}
            overflow="visible"
            style={{display: 'block'}}
        >
            <rect
                x={IX} y={IY}
                width={INTERSECTION_SIZE}
                height={INTERSECTION_SIZE}
                fill={ASPHALT}
                stroke={ROAD_BORDER}
                strokeWidth={1}
            />

            {ROAD_CONFIGS.map(({direction, x, y}) => {
                const {waitingVehicles} = roadMap[direction];
                return (
                    <Road
                        key={direction}
                        direction={direction}
                        vehicles={waitingVehicles}
                        x={x}
                        y={y}
                    />
                );
            })}

            {TRAFFIC_LIGHT_CONFIGS.map(({direction, x, y, rotation}) => {
                const {trafficLightState} = roadMap[direction];
                return (
                    <g key={direction} transform={`translate(${x}, ${y})`}>
                        <TrafficLight
                            state={trafficLightState}
                            rotation={rotation}
                            broken={broken}
                        />
                    </g>
                );
            })}
        </svg>
    );
}