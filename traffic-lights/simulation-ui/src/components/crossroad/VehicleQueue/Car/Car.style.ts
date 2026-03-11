import styled, {css, keyframes} from 'styled-components';

const pulse = keyframes`
    0%, 100% {
        opacity: 1;
        transform: scale(1);
    }
    50% {
        opacity: 0.2;
        transform: scale(0.5);
    }
`;


export const BlinkerDot = styled.circle<{
    $active: boolean,
    $color: string
}>`
    fill: ${({$color}) => $color};
    filter: ${({$active, $color}) => $active ? `drop-shadow(0 0 4px ${$color})` : 'none'};
    transform-box: fill-box;
    transform-origin: center;
    animation: ${({$active}) => $active ? css`${pulse} 0.8s ease-in-out infinite` : 'none'};
`;