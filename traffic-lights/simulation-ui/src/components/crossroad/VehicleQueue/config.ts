import type {Movement} from "@/types/simulation.ts";

export const R_OFFSET = 12;//chyba szerokość samochodu przez 2

export const MAX_VISIBLE = 3;
export const CAR_W = 100;
export const CAR_H = 50;
export const CAR_GAP = 6;


export const BLINKER_COLOR = '#f59e0b';


export const ARC_N = 5;
export const LINE_N = 5;


export const EXIT_DURATION: Record<Movement, number> = {
    STRAIGHT: 1,
    RIGHT: 1,
    LEFT: 1.15,
    UTURN: 1.35,
};


