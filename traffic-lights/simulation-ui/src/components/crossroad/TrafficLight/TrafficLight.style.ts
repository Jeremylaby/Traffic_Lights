import styled, {keyframes} from 'styled-components';

export const Housing = styled.rect`
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.6));
`;
const blink = keyframes`
    0%, 100% {
        opacity: 1;
    }
    50% {
        opacity: 0;
    }
`;
export const Bulb = styled.circle<{ $color: string; $active: boolean; $blink?: boolean }>`
    fill: ${({$color, $blink}) => $blink ? '#ffd700' : $color};
    filter: ${({$active, $color, $blink}) =>
            $blink
                    ? 'drop-shadow(0 0 6px #ffd700)'
                    : $active
                            ? 'none'
                            : `drop-shadow(0 0 6px ${$color})`
    };
    transition: fill 0.3s ease, filter 0.3s ease;
    animation: ${({$blink}) => $blink ? blink : 'none'} 0.8s ease-in-out infinite;
`;