import styled, {keyframes} from "styled-components";


export const pulse = keyframes`
    0%, 100% {
        opacity: 0.5;
    }
    50% {
        opacity: 1;
    }
`;

const fadeIn = keyframes`
    from {
        opacity: 0;
        transform: translateY(8px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
`;

export const Page = styled.div`
    background: #16161e;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    font-family: 'Courier New', Courier, monospace;
    color: #e2e8f0;
    position: relative;

    &::before {
        content: '';
        position: absolute;
        inset: 0;
        background-image: linear-gradient(rgba(255, 255, 255, 0.015) 1px, transparent 1px),
        linear-gradient(90deg, rgba(255, 255, 255, 0.015) 1px, transparent 1px);
        background-size: 40px 40px;
        pointer-events: none;
    }
`;

export const TopBar = styled.div`
    position: relative;
    z-index: 10;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 14px 32px;
    border-bottom: 1px solid #0f172a;
    background: rgba(6, 6, 8, 0.9);
    backdrop-filter: blur(8px);
`;

export const TopLeft = styled.div`
    display: flex;
    flex-direction: column;
    gap: 2px;
`;

export const SystemLabel = styled.div`
    font-size: 9px;
    letter-spacing: 3px;
    color: #4ade80;
    text-transform: uppercase;
    animation: ${pulse} 3s ease-in-out infinite;
`;

export const PageTitle = styled.div`
    font-size: 14px;
    color: #f1f5f9;
    letter-spacing: 1px;
`;

export const RefreshBtn = styled.button`
    background: transparent;
    border: 1px solid #1e293b;
    border-radius: 3px;
    color: #64748b;
    font-family: 'Courier New', monospace;
    font-size: 10px;
    letter-spacing: 2px;
    padding: 7px 16px;
    cursor: pointer;
    transition: all 0.15s;
    text-transform: uppercase;

    &:hover {
        border-color: #4ade80;
        color: #4ade80;
        background: rgba(74, 222, 128, 0.05);
    }

    &:active {
        transform: scale(0.97);
    }
`;

export const Main = styled.div`
    position: relative;
    z-index: 1;
    flex: 1;
    flex-direction: column;
    display: flex;
    gap: 40px;
    padding: 40px;
    align-items: flex-start;
    justify-content: center;
    animation: ${fadeIn} 0.4s ease both;
`;

export const CrossroadBox = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 16px;
`;

export const MockHint = styled.div`
    font-size: 9px;
    letter-spacing: 2px;
    color: #4b5365;
    text-transform: uppercase;
    text-align: center;
`;

export const DataPanel = styled.div`
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 12px;
    width: 100%;
`;

export const PanelTitle = styled.div`
    font-size: 11px;
    grid-column: 1 / -1;
    letter-spacing: 3px;
    color: #ffffff;
    text-transform: uppercase;
    padding-bottom: 8px;
    border-bottom: 1px solid #253968;
`;

export const StepBar = styled.div`
    display: flex;
    align-items: center;
    gap: 12px;
`;

export const StepBtn = styled.button<{ $primary?: boolean }>`
    background: transparent;
    border: 1px solid ${({$primary}) => ($primary ? '#4ade80' : '#1e293b')};
    border-radius: 3px;
    color: ${({$primary}) => ($primary ? '#4ade80' : '#475569')};
    font-family: 'Courier New', monospace;
    font-size: 10px;
    letter-spacing: 2px;
    padding: 6px 14px;
    cursor: pointer;
    transition: all 0.15s;

    &:disabled {
        opacity: 0.25;
        cursor: default;
    }

    &:not(:disabled):hover {
        background: ${({$primary}) =>
                $primary ? 'rgba(74,222,128,0.08)' : 'rgba(255,255,255,0.03)'};
    }
`;

export const StepCounter = styled.span`
    font-size: 10px;
    letter-spacing: 2px;
    color: #334155;
    min-width: 64px;
    text-align: center;
`;

export const RoadCard = styled.div`
    border: 1px solid #253968;
    border-radius: 4px;
    padding: 10px 12px;
    margin-bottom: 8px;
`;

export const RoadCardHeader = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 6px;
`;

export const RoadName = styled.span`
    font-size: 10px;
    letter-spacing: 2px;
    color: #64748b;
`;

export const LightBadge = styled.span<{ $state: string }>`
    font-size: 9px;
    letter-spacing: 1px;
    padding: 2px 6px;
    border-radius: 2px;
    color: ${({$state}) => {
        if ($state === 'GREEN' || $state === 'GREEN_ARROW') return '#4ade80';
        if ($state === 'YELLOW' || $state === 'RED_YELLOW') return '#facc15';
        return '#f87171';
    }};
    border: 1px solid currentColor;
`;

export const EmptyNote = styled.span`
    font-size: 10px;
    color: #334155;
`;

export const VehicleRow = styled.div`
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 10px;
    color: #475569;
    margin-top: 4px;
`;

export const VehicleId = styled.span`color: #94a3b8;`;
export const VehicleArrow = styled.span`color: #334155;`;
export const VehicleEnd = styled.span`color: #64748b;`;
export const MoveBadge = styled.span`color: #475569;
    letter-spacing: 1px;`;

export const SourceBox = styled.div`
    margin-top: 12px;
    grid-column: 1 / -1;
    border: 1px solid #1e293b;
    border-radius: 4px;
    padding: 10px 12px;
`;

export const SourceTitle = styled.div`
    font-size: 9px;
    letter-spacing: 2px;
    color: #334155;
    text-transform: uppercase;
    margin-bottom: 8px;
`;

export const SourceCode = styled.pre`
    font-family: 'Courier New', monospace;
    font-size: 9px;
    color: #475569;
    margin: 0;
    white-space: pre-wrap;
    word-break: break-all;
    line-height: 1.5;
    max-height: 140px;
    overflow-y: auto;
`;