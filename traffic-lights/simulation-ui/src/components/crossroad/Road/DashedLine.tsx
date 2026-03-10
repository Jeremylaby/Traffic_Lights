import {LANE_DIVIDER} from "./config.ts";



interface DashedLineProps {
    x1: number; y1: number;
    x2: number; y2: number;
}

const DashedLine = ({ x1, y1, x2, y2 }: DashedLineProps)=> {
    return (
        <line
            x1={x1} y1={y1} x2={x2} y2={y2}
            stroke={LANE_DIVIDER}
            strokeWidth={1}
            strokeDasharray="8 6"
        />
    );
};
export default DashedLine;