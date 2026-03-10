import styled, {createGlobalStyle, keyframes} from "styled-components";

const fadeIn = keyframes`
    from { opacity: 0; }
    to   { opacity: 1; }
`;

const pulse = keyframes`
    0%, 100% { opacity: 1; }
    50%       { opacity: 0.5; }
`;

const slideUp = keyframes`
    from { opacity: 0; transform: translateY(6px); }
    to   { opacity: 1; transform: translateY(0); }
`;

export const GlobalStyle = createGlobalStyle`
    @import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Mono:wght@400;500;700&family=Bebas+Neue&display=swap');
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
    body {
        background: #080809;
        color: #D4D4DC;
        font-family: 'IBM Plex Mono', 'Courier New', monospace;
        -webkit-font-smoothing: antialiased;
    }
`;

export const Root = styled.div`
    height: 100vh;
    display: flex;
    flex-direction: column;
    background: #080809;
    overflow: hidden;
    animation: ${fadeIn} 0.3s ease;
`;

export const GridBg = styled.div`
    position: fixed;
    inset: 0;
    background-image:
        linear-gradient(#111116 1px, transparent 1px),
        linear-gradient(90deg, #111116 1px, transparent 1px);
    background-size: 52px 52px;
    pointer-events: none;
    z-index: 0;
`;

export const Header = styled.header`
    position: relative;
    z-index: 10;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 32px;
    height: 56px;
    border-bottom: 1px solid #141418;
    background: rgba(8, 8, 9, 0.9);
    backdrop-filter: blur(8px);
    flex-shrink: 0;
`;

export const HeaderLeft = styled.div`
    display: flex;
    align-items: center;
    gap: 24px;
`;

export const BackBtn = styled.button`
    background: transparent;
    border: 1px solid #1A1A22;
    border-radius: 2px;
    color: #3A3A4E;
    font-family: 'IBM Plex Mono', monospace;
    font-size: 11px;
    letter-spacing: 0.08em;
    padding: 6px 14px;
    cursor: pointer;
    transition: color 0.15s, border-color 0.15s;

    &:hover {
        color: #D4D4DC;
        border-color: #2A2A38;
    }
`;

export const HeaderTitle = styled.div`
    display: flex;
    flex-direction: column;
    gap: 1px;
`;

export const HeaderLabel = styled.span`
    font-size: 9px;
    letter-spacing: 0.2em;
    color: #2A2A38;
    text-transform: uppercase;
`;

export const HeaderMode = styled.span`
    font-family: 'Bebas Neue', sans-serif;
    font-size: 18px;
    letter-spacing: 0.08em;
    color: #ECECF4;
    line-height: 1;
`;

export const ModeTag = styled.span`
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 0.12em;
    color: #E8FF47;
    text-transform: uppercase;
    border: 1px solid #E8FF4733;
    padding: 2px 8px;
    border-radius: 2px;
`;

export const StepCounter = styled.div`
    display: flex;
    align-items: baseline;
    gap: 6px;
`;

export const StepNum = styled.span`
    font-family: 'Bebas Neue', sans-serif;
    font-size: 32px;
    color: #E8FF47;
    line-height: 1;
`;

export const StepOf = styled.span`
    font-size: 13px;
    color: #2A2A38;
`;

export const ErrorMsg = styled.div`
    font-size: 10px;
    color: #F87171;
    letter-spacing: 0.05em;
    animation: ${slideUp} 0.2s ease;
`;

export const Main = styled.main`
    position: relative;
    z-index: 1;
    flex: 1;
    display: flex;
    overflow: hidden;
`;



export const Sidebar = styled.aside`
    width: 200px;
    flex-shrink: 0;
    border-right: 1px solid #141418;
    display: flex;
    flex-direction: column;
    background: rgba(8, 8, 9, 0.6);
    overflow-y: auto;
`;

export const SidebarSection = styled.div`
    padding: 20px 16px;
    border-bottom: 1px solid #141418;
`;

export const SidebarLabel = styled.div`
    font-size: 9px;
    letter-spacing: 0.2em;
    color: #2A2A38;
    font-weight: 700;
    margin-bottom: 14px;
    text-transform: uppercase;
`;

export const QueueItem = styled.div`
    display: flex;
    flex-direction: column;
    gap: 6px;
    margin-bottom: 14px;
    animation: ${slideUp} 0.3s ease;

    &:last-child { margin-bottom: 0; }
`;

export const QueueTop = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
`;

export const QueueDir = styled.span`
    font-size: 11px;
    font-weight: 700;
    color: #6A6A7E;
    letter-spacing: 0.1em;
`;



export const QueueBar = styled.div`
    height: 2px;
    background: #141418;
    border-radius: 1px;
    overflow: hidden;
`;

export const QueueFill = styled.div<{ $pct: number; $high: boolean }>`
    height: 100%;
    width: ${({ $pct }) => $pct}%;
    background: ${({ $high }) => $high ? '#F87171' : '#E8FF47'};
    border-radius: 1px;
    transition: width 0.4s ease;
`;

export const QueueCount = styled.span<{ $high: boolean }>`
    font-size: 10px;
    color: ${({ $high }) => $high ? '#F87171' : '#3A3A4E'};
    align-self: flex-end;
`;



export const LogEntry = styled.div`
    font-size: 10px;
    color: #3A3A4E;
    line-height: 1.6;
    letter-spacing: 0.03em;
    animation: ${slideUp} 0.2s ease;

    span { color: #6A6A7E; }
`;


export const CanvasArea = styled.div`
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 32px;
    overflow: auto;
`;

export const CanvasFrame = styled.div`
    background: #0C0C10;
    border: 1px solid #1A1A22;
    border-radius: 2px;
    padding: 32px;
    box-shadow:
        0 0 0 1px #141418,
        0 32px 80px rgba(0, 0, 0, 0.6);
    position: relative;

    &::before {
        content: 'LIVE';
        position: absolute;
        top: 10px;
        right: 12px;
        font-size: 9px;
        letter-spacing: 0.2em;
        color: #4ADE80;
        font-weight: 700;
    }
`;

export const EmptyState = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
    color: #2A2A38;
    font-size: 12px;
    letter-spacing: 0.05em;
`;



export const Footer = styled.footer`
    position: relative;
    z-index: 10;
    flex-shrink: 0;
    border-top: 1px solid #141418;
    background: rgba(8, 8, 9, 0.9);
    backdrop-filter: blur(8px);
    padding: 16px 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
`;

export const CtrlBtn = styled.button<{ $disabled?: boolean }>`
    background: transparent;
    border: 1px solid #1A1A22;
    border-radius: 2px;
    color: ${({ $disabled }) => $disabled ? '#1E1E2A' : '#6A6A7E'};
    font-family: 'IBM Plex Mono', monospace;
    font-size: 16px;
    width: 40px;
    height: 40px;
    cursor: ${({ $disabled }) => $disabled ? 'not-allowed' : 'pointer'};
    display: flex;
    align-items: center;
    justify-content: center;
    transition: color 0.15s, border-color 0.15s;

    &:hover:not(:disabled) {
        color: #D4D4DC;
        border-color: #2A2A38;
    }
`;

export const StepBtn = styled.button<{ $disabled?: boolean }>`
    background: ${({ $disabled }) => $disabled ? 'transparent' : '#E8FF47'};
    border: 1px solid ${({ $disabled }) => $disabled ? '#1A1A22' : '#E8FF47'};
    border-radius: 2px;
    color: #080809;
    font-family: 'IBM Plex Mono', monospace;
    font-size: 11px;
    font-weight: 700;
    letter-spacing: 0.08em;
    padding: 0 24px;
    height: 52px;
    cursor: ${({ $disabled }) => $disabled ? 'not-allowed' : 'pointer'};
    opacity: ${({ $disabled }) => $disabled ? 0.25 : 1};
    transition: all 0.15s;
    white-space: nowrap;

    &:hover:not(:disabled) {
        background: #F4FF80;
        box-shadow: 0 0 20px rgba(232, 255, 71, 0.25);
    }
`;

export const CtrlSep = styled.div`
    width: 1px;
    height: 24px;
    background: #1A1A22;
    margin: 0 4px;
`;

export const LoadingDot = styled.span`
    display: inline-block;
    animation: ${pulse} 1s ease-in-out infinite;
    color: #E8FF47;
`;

