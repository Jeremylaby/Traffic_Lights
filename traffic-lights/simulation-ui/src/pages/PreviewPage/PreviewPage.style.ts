import styled from "styled-components";

export const Page = styled.div`
    background: #111;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40px;
    gap: 40px;
    font-family: monospace;
    color: #fff;
`;

export const Title = styled.h1`
    color: #aaa;
    font-size: 14px;
    letter-spacing: 2px;
    text-transform: uppercase;
    margin: 0;
`;

export const Row = styled.div`
    display: flex;
    gap: 40px;
    align-items: flex-end;
`;

export const Cell = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
`;

export const Label = styled.span`
    font-size: 11px;
    color: #666;
    text-transform: uppercase;
    letter-spacing: 1px;
`;
