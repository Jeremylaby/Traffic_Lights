import { useMemo } from 'react';
import { BLINKER_COLOR, CAR_H, CAR_W} from '../config.ts';
import type { Movement } from '@/types/simulation.ts';
import CarIcon from '@/components/crossroad/VehicleQueue/Car/CarIcon.tsx';
import { BlinkerDot } from '@/components/crossroad/VehicleQueue/Car/Car.style.ts';
import { useRoad } from '@/components/crossroad/Road/RoadContext.ts';
import {randomCarColor} from "./util.ts";

interface CarProps {
    x: number;
    y: number;
    vehicleId: string;
    movement: Movement;
}

const Car = ({ x, y, vehicleId, movement }: CarProps) => {
    const { roadRotation } = useRoad();
    const color  = useMemo(() => randomCarColor(), []);
    const active = movement !== 'STRAIGHT';

    const blinkerCx = movement === 'RIGHT' ? CAR_W * 0.65 : CAR_W * 0.35;
    const blinkerCy = CAR_H * 0.4;
    const carIconRotation = 180;

    return (
        <g transform={`translate(${x}, ${y})`}>
            <g transform={`rotate(${carIconRotation}, ${CAR_W / 2}, ${CAR_H / 2})`}>
                <CarIcon fill={color.fill} stroke={color.stroke} width={CAR_W} height={CAR_H} />
                {active && (
                    <BlinkerDot
                        $active={active}
                        $color={BLINKER_COLOR}
                        cx={blinkerCx}
                        cy={blinkerCy}
                        r={3}
                    />
                )}
            </g>

            <text
                x={CAR_W / 2}
                y={CAR_H / 2}
                textAnchor="middle"
                dominantBaseline="middle"
                fill="#ffffff"
                fontSize={6}
                fontFamily="monospace"
                transform={`rotate(${-roadRotation}, ${CAR_W / 2}, ${CAR_H / 2})`}
            >
                {vehicleId}
            </text>
        </g>
    );
};

export default Car;