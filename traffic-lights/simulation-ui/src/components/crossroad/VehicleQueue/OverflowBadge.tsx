interface OverflowBadgeProps {
    count: number;

}
const OverflowBadge = ({ count }: OverflowBadgeProps) => {
    return (
        <g>
            <rect x={-14} y={-10} width={28} height={16} rx={4} fill="#1e293b" stroke="#475569" strokeWidth={0.8}/>
            <text
                x={0} y={2}
                textAnchor="middle"
                fill="#94a3b8"
                fontSize={8}
                fontFamily="monospace"
                fontWeight="bold"
            >
                +{count}
            </text>
        </g>
    );
};
export default OverflowBadge;