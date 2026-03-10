import {ARC_N, LINE_N, R_OFFSET} from "./config.ts";
import type {Movement} from "@/types/simulation.ts";
import {LANE_WIDTH, ROAD_LENGTH} from "@/components/crossroad/Road/config.ts";

/**
 * Generuje `n` próbek na łuku kołowym (pierwsza i ostatnia włącznie).
 *
 * Układ lokalny SVG / drogi:
 *   +X  = prawo
 *   +Y  = DÓŁ  (odwrócone względem matematycznego)
 *   0°  = pojazd skierowany "do przodu" (w górę, czyli −Y)
 *
 * @param cx       odsunięcie środka okręgu od punktu startowego (oś X)
 * @param cy       odsunięcie środka okręgu od punktu startowego (oś Y)
 * @param r        promień
 * @param startDeg kąt startowy [°] w układzie matematycznym (Y w górę)
 * @param endDeg   kąt końcowy  [°]
 * @param n        łączna liczba punktów (min. 2)
 */
const arcSamples = (
    cx: number,
    cy: number,
    r: number,
    startDeg: number,
    endDeg: number,
    n: number,
): { x: number[]; y: number[] } => {
    const xs: number[] = [];
    const ys: number[] = [];

    for (let i = 0; i < n; i++) {
        const t = i / (n - 1);
        const deg = startDeg + (endDeg - startDeg) * t;
        const rad = (deg * Math.PI) / 180;
        xs.push(cx + r * Math.cos(rad));
        ys.push(cy - r * Math.sin(rad));
    }

    return {x: xs, y: ys};
};
const lineSamples = (
    x0: number, y0: number,
    x1: number, y1: number,
    n: number,
): { x: number[]; y: number[] } => {
    const xs: number[] = [];
    const ys: number[] = [];

    for (let i = 0; i < n; i++) {
        const t = n === 1 ? 0 : i / (n - 1);
        xs.push(x0 + (x1 - x0) * t);
        ys.push(y0 + (y1 - y0) * t);
    }

    return {x: xs, y: ys};
};


const lerp = (a: number, b: number, n: number): number[] =>
    Array.from({length: n}, (_, i) => a + (b - a) * (n === 1 ? 0 : i / (n - 1)));

const opacity = (n: number): number[] =>
    Array.from({length: n}, (_, i) => (i < n - 1 ? 1 : 0));

export interface ExitKeyframes {
    x?: number[] | number;
    y?: number[] | number;
    rotate?: number[] | number;
    opacity: number[] | number;
}

const buildStraight = (): ExitKeyframes => {
    return {
        y: ROAD_LENGTH * 2,
        opacity: [1, 1, 1, 0],
    };
};

//git
const buildRight = (): ExitKeyframes => {
    const r = -(LANE_WIDTH / 1.5 + R_OFFSET);

    // Faza 1
    const arc = arcSamples(r, 0, r, 180, 90, ARC_N);
    const arcR = lerp(0, 90, ARC_N);

    // Faza 2
    const ex = arc.x[ARC_N - 1];
    const ey = arc.y[ARC_N - 1];
    const seg = lineSamples(ex, ey, ex - ROAD_LENGTH, ey, LINE_N);

    const x = [...arc.x, ...seg.x.slice(1)];
    const y = [...arc.y, ...seg.y.slice(1)];
    const rotate = [...arcR, ...lerp(90, 90, LINE_N).slice(1)];

    return {x, y, rotate, opacity: opacity(x.length)};
};


const buildLeft = (): ExitKeyframes => {
    const r = LANE_WIDTH / 1.5 + R_OFFSET;

    // Faza 1
    const seg1 = lineSamples(0, 0, 0, LANE_WIDTH, LINE_N);

    // Faza 2
    const arc = arcSamples(r, LANE_WIDTH, r, 180, 270, ARC_N);
    const arcR = lerp(0, -90, ARC_N);

    // Faza 3
    const ex = arc.x[ARC_N - 1];
    const ey = arc.y[ARC_N - 1];
    const seg2 = lineSamples(ex, ey, ex + (r + ROAD_LENGTH), ey, LINE_N);

    const x = [...seg1.x, ...arc.x.slice(1), ...seg2.x.slice(1)];
    const y = [...seg1.y, ...arc.y.slice(1), ...seg2.y.slice(1)];
    const rotate = [...lerp(0, 0, LINE_N), ...arcR.slice(1), ...lerp(-90, -90, LINE_N).slice(1)];

    return {x, y, rotate, opacity: opacity(x.length)};
};

//git
const buildUTurn = (): ExitKeyframes => {
    const r = LANE_WIDTH / 2;
    const nHalf = ARC_N * 2 - 1;

    const seg1 = lineSamples(0, 0, 0, LANE_WIDTH, LINE_N);

    const arc = arcSamples(r, LANE_WIDTH + R_OFFSET, r, 180, 360, nHalf);  // ← cx = -r, kąt 180→360
    const arcR = lerp(0, -180, nHalf);

    const ex = arc.x[nHalf - 1];
    const ey = arc.y[nHalf - 1];
    const seg2 = lineSamples(ex, ey, ex, ey - (LANE_WIDTH + ROAD_LENGTH), LINE_N);

    const x = [...seg1.x, ...arc.x.slice(1), ...seg2.x.slice(1)];
    const y = [...seg1.y, ...arc.y.slice(1), ...seg2.y.slice(1)];
    const rotate = [...lerp(0, 0, LINE_N), ...arcR.slice(1), ...lerp(-180, -180, LINE_N).slice(1)];

    return {x, y, rotate, opacity: opacity(x.length)};
};


export const ANIMATION: Record<Movement, ExitKeyframes> = {
    STRAIGHT: buildStraight(),
    RIGHT: buildRight(),
    LEFT: buildLeft(),
    UTURN: buildUTurn(),
};