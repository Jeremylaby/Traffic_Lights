import styled, {createGlobalStyle, keyframes} from "styled-components";

const fadeUp = keyframes`
    from { opacity: 0; transform: translateY(16px); }
    to   { opacity: 1; transform: translateY(0); }
`;

const blink = keyframes`
    0%, 100% { opacity: 1; }
    50%       { opacity: 0; }
`;

const scanline = keyframes`
    0%   { transform: translateY(-100%); }
    100% { transform: translateY(100vh); }
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
    min-height: 100vh;
    display: flex;
    position: relative;
    overflow: hidden;
`;

export const GridBg = styled.div`
    position: fixed;
    inset: 0;
    background-image:
        linear-gradient(#141418 1px, transparent 1px),
        linear-gradient(90deg, #141418 1px, transparent 1px);
    background-size: 52px 52px;
    pointer-events: none;
    z-index: 0;

    &::after {
        content: '';
        position: absolute;
        inset: 0;
        background: radial-gradient(ellipse 80% 60% at 70% 50%, transparent 40%, #080809 100%);
    }
`;

export const Scanline = styled.div`
    position: fixed;
    inset: 0;
    pointer-events: none;
    z-index: 0;
    overflow: hidden;

    &::after {
        content: '';
        position: absolute;
        left: 0; right: 0;
        height: 120px;
        background: linear-gradient(transparent, rgba(232, 255, 71, 0.015), transparent);
        animation: ${scanline} 8s linear infinite;
    }
`;

export const Left = styled.div`
    position: relative;
    z-index: 1;
    width: 420px;
    flex-shrink: 0;
    padding: 64px 48px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    border-right: 1px solid #1A1A22;
`;

export const Right = styled.div`
    position: relative;
    z-index: 1;
    flex: 1;
    padding: 48px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 12px;
    overflow-y: auto;
`;

export const Eyebrow = styled.div`
    font-size: 10px;
    letter-spacing: 0.25em;
    color: #E8FF47;
    font-weight: 700;
    margin-bottom: 24px;

    &::before {
        content: '▶ ';
    }
`;

export const Title = styled.h1`
    font-family: 'Bebas Neue', sans-serif;
    font-size: clamp(52px, 6vw, 88px);
    line-height: 0.92;
    letter-spacing: 0.01em;
    color: #ECECF4;
    margin-bottom: 32px;

    span {
        color: #E8FF47;
        display: block;
    }
    p{
        font-size: 40px;
    }
`;

export const Desc = styled.p`
    font-size: 13px;
    line-height: 1.75;
    color: #5A5A6E;
    max-width: 300px;
    margin-bottom: 48px;
`;

export const Meta = styled.div`
    display: flex;
    flex-direction: column;
    gap: 10px;
`;

export const MetaRow = styled.div`
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 11px;
    color: #E8FF47;
    letter-spacing: 0.05em;

    &::before {
        content: '';
        display: block;
        width: 20px;
        height: 1px;
        background: #E8FF47;
    }
`;

export const SectionLabel = styled.div`
    font-size: 10px;
    letter-spacing: 0.2em;
    color: #9999b3;
    font-weight: 700;
    padding-bottom: 16px;
    border-bottom: 1px solid #253968;
    margin-bottom: 4px;
`;

export const Card = styled.button<{ $active: boolean; $color: string, $index: number }>`
    position: relative;
    background: ${({$active}) => $active ? '#0E0E14' : 'transparent'};
    border: 1px solid ${({$active}) => $active ? '#2A2A38' : '#141418'};
    border-left: 3px solid ${({$active, $color}) => $active ? $color : '#1A1A22'};

    border-radius: 2px;
    padding: 20px 24px;
    cursor: pointer;
    text-align: left;
    transition: border-color 0.12s, background 0.12s;
    display: grid;
    grid-template-columns: 1fr auto;
    gap: 8px;
    align-items: start;

    &:hover {
        border-color: #2A2A38;
        border-left-color: ${({ $color }) => $color};
        background: #0C0C12;
    }

    animation: ${fadeUp} 0.4s ease both;
    animation-delay: ${({$index}) => `${$index * 0.06}s`};
`;

export const CardBody = styled.div`
    display: flex;
    flex-direction: column;
    gap: 6px;
`;

export const CardHeader = styled.div`
    display: flex;
    align-items: baseline;
    gap: 12px;
`;

export const CardName = styled.span<{ $active: boolean }>`
    font-family: 'Bebas Neue', sans-serif;
    font-size: 22px;
    letter-spacing: 0.04em;
    color: ${({ $active }) => $active ? '#ECECF4' : '#4A4A5E'};
    transition: color 0.12s;
    line-height: 1;
`;

export const CardTag = styled.span<{ $color: string; $active: boolean }>`
    font-size: 9px;
    font-weight: 700;
    letter-spacing: 0.15em;
    color: ${({ $color, $active }) => $active ? $color : '#2E2E3C'};
    text-transform: uppercase;
    transition: color 0.12s;
`;

export const CardDesc = styled.p<{ $active: boolean }>`
    font-size: 12px;
    line-height: 1.65;
    color: ${({ $active }) => $active ? '#5A5A72' : '#2E2E3C'};
    transition: color 0.12s;
    max-width: 480px;
`;

export const RadioDot = styled.div<{ $active: boolean; $color: string }>`
    width: 10px;
    height: 10px;
    border-radius: 50%;
    border: 2px solid ${({ $active, $color }) => $active ? $color : '#2A2A38'};
    background: ${({ $active, $color }) => $active ? $color : 'transparent'};
    margin-top: 6px;
    flex-shrink: 0;
    transition: all 0.12s;

    ${({ $active }) => $active && `
        box-shadow: 0 0 8px currentColor;
    `}
`;

export const RunBtn = styled.button<{ $enabled: boolean }>`
    margin-top: 24px;
    align-self: flex-end;
    background: ${({ $enabled }) => $enabled ? '#E8FF47' : 'transparent'};
    border: 1px solid ${({ $enabled }) => $enabled ? '#E8FF47' : '#1E1E2A'};
    border-radius: 2px;
    padding: 14px 40px;
    cursor: ${({ $enabled }) => $enabled ? 'pointer' : 'not-allowed'};
    opacity: ${({ $enabled }) => $enabled ? 1 : 0.35};
    transition: all 0.15s;
    font-family: 'IBM Plex Mono', monospace;
    font-size: 12px;
    font-weight: 700;
    letter-spacing: 0.1em;
    color: #080809;
    text-transform: uppercase;

    &:hover {
        ${({ $enabled }) => $enabled && `
            background: #F4FF80;
            box-shadow: 0 0 24px rgba(232, 255, 71, 0.3);
        `}
    }
`;

export const Cursor = styled.span`
    display: inline-block;
    width: 2px;
    height: 1em;
    background: #E8FF47;
    margin-left: 4px;
    vertical-align: middle;
    animation: ${blink} 1s step-end infinite;
`;
