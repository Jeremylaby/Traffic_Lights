import {MARKING_COLOR} from "@/components/crossroad/Road/config.ts";

interface LaneArrowProps {
    x: number;
    y: number;
    arrowRotation: number;
    label: string;
    counterRotation: number;
}

const LaneArrow = ({ x, y, arrowRotation, label, counterRotation }: LaneArrowProps) => {
    return (
        <g transform={`translate(${x}, ${y})`}>
            <g transform={`rotate(${arrowRotation})`}>
                <g transform={`translate(0, -20)`}>
                    <line
                        x1={0} y1={8} x2={0} y2={-8}
                        stroke={MARKING_COLOR} strokeWidth={1.5} strokeLinecap="round"
                    />
                    <polygon points="0,-12 -4,-6 4,-6" fill={MARKING_COLOR} />
                </g>
            </g>

            <text
                x={0}
                y={0}
                textAnchor="middle"
                dominantBaseline="middle"
                fill={MARKING_COLOR}
                fontSize={6}
                fontFamily="monospace"
                transform={`rotate(${counterRotation})`}
            >
                {label}
            </text>
        </g>
    );
};
export default LaneArrow;