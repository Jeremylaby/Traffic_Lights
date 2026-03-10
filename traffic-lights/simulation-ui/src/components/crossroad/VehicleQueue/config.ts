import {ROAD_LENGTH} from "@/components/crossroad/Road/config.ts";


export const MAX_VISIBLE = 3;
export const CAR_W = 100;
export const CAR_H = 50;
export const CAR_GAP = 6;


export const BLINKER_COLOR = '#f59e0b';

export const CAR_COLORS = [
    { fill: '#3b82f6', stroke: '#93c5fd' }, // blue
    { fill: '#ef4444', stroke: '#fca5a5' }, // red
    { fill: '#22c55e', stroke: '#86efac' }, // green
    { fill: '#f97316', stroke: '#fdba74' }, // orange
];

export function randomCarColor() {
    return CAR_COLORS[Math.floor(Math.random() * CAR_COLORS.length)];
}

export const ANIMATION = {
    "STRAIGHT": { opacity: 0, y: ROAD_LENGTH * 2 },
    "LEFT": { ...},
    "RIGHT": { ...},
    "UTURN": { ...},
}