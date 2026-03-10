import styled from "styled-components";

export const AddVehiclePanelWrapper = styled.div`
    padding: 20px 16px;
    border-bottom: 1px solid #141418;
    display: flex;
    flex-direction: column;
    gap: 10px;
`;

export const RoadSelect = styled.select`
    background: #0C0C10;
    border: 1px solid #1A1A22;
    border-radius: 2px;
    color: #6A6A7E;
    font-family: 'IBM Plex Mono', monospace;
    font-size: 10px;
    letter-spacing: 0.08em;
    padding: 6px 8px;
    width: 100%;
    cursor: pointer;
    appearance: none;
    transition: border-color 0.12s, color 0.12s;

    &:focus {
        outline: none;
        border-color: #2A2A38;
        color: #D4D4DC;
    }

    option { background: #0C0C10; }
`;

export const AddBtn = styled.button<{ $disabled?: boolean }>`
    background: transparent;
    border: 1px solid ${({ $disabled }) => $disabled ? '#1A1A22' : '#2A2A38'};
    border-radius: 2px;
    color: ${({ $disabled }) => $disabled ? '#1E1E2A' : '#6A6A7E'};
    font-family: 'IBM Plex Mono', monospace;
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 0.1em;
    padding: 7px 0;
    width: 100%;
    cursor: ${({ $disabled }) => $disabled ? 'not-allowed' : 'pointer'};
    text-transform: uppercase;
    transition: all 0.12s;

    &:hover:not(:disabled) {
        border-color: #E8FF47;
        color: #E8FF47;
    }
`;

export const SelectRow = styled.div`
    display: flex;
    flex-direction: column;
    gap: 4px;
`;

export const SelectLabel = styled.span`
    font-size: 9px;
    letter-spacing: 0.15em;
    color: #2A2A38;
    text-transform: uppercase;
`;