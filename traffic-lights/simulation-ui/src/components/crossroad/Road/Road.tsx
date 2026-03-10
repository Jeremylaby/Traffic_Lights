import type {RoadDirection, VehicleDto} from '@/types/simulation.ts';
import {VehicleQueue} from '@/components/crossroad/VehicleQueue/VehicleQueue.tsx';
import {CAR_H} from '@/components/crossroad/VehicleQueue/config.ts';
import RoadContext from "./RoadContext";
import {ASPHALT, LANE_WIDTH, ROAD_BORDER, ROAD_LENGTH, ROAD_ROTATIONS, ROAD_WIDTH} from "./config.ts";
import LaneArrow from "./LaneArrow.tsx";
import DashedLine from "./DashedLine.tsx";


interface RoadProps {
    direction: RoadDirection;
    vehicles: VehicleDto[];
    x: number;
    y: number;
    vehiclesOnly?: boolean;  // ← jedyna zmiana w typie
}


export function Road({direction, vehicles, x, y, vehiclesOnly = false}: RoadProps) {

    const roadRotation = ROAD_ROTATIONS[direction];
    const counterRotation = -roadRotation;

    //const queueLength = Math.min(vehicles.length, MAX_VISIBLE) * (CAR_H + CAR_GAP);
    // const roadLength  = ROAD_LENGTH;

    const roadW = ROAD_WIDTH;
    const roadH = ROAD_LENGTH;

    const cx = x + roadW / 2;
    const cy = y + roadH / 2;

    const queueX = 0;
    const queueY = roadH - CAR_H - 5;

    const arrowY = roadH * 0.5;
    const arrows = [
        {x: LANE_WIDTH + LANE_WIDTH / 2, y: arrowY, arrowRotation: 0, label: 'IN'},
        {x: LANE_WIDTH / 2, y: arrowY, arrowRotation: 180, label: 'OUT'},
    ];

    return (
        <RoadContext.Provider value={{direction, roadRotation}}>
            <g transform={`rotate(${roadRotation}, ${cx}, ${cy})`}>
                <g transform={`translate(${x}, ${y})`}>
                    {!vehiclesOnly && <>
                        <rect
                            x={0} y={0}
                            width={roadW} height={roadH}
                            fill={ASPHALT} stroke={ROAD_BORDER} strokeWidth={1}
                        />

                        <DashedLine x1={LANE_WIDTH} y1={0} x2={LANE_WIDTH} y2={roadH}/>

                        {arrows.map((arrow, i) => (
                            <LaneArrow
                                key={i}
                                {...arrow}
                                counterRotation={counterRotation}
                            />
                        ))}</>}

                    <g transform={`translate(${queueX}, ${queueY})`}>
                        <VehicleQueue vehicles={vehicles}/>
                    </g>
                </g>
            </g>
        </RoadContext.Provider>
    );
}