import {AddBtn, AddVehiclePanelWrapper, RoadSelect, SelectLabel, SelectRow,} from "./AddVehiclePanel.style.ts";
import {useCallback, useState} from "react";
import type {RoadDirection} from "@/types/simulation.ts";
import {SidebarLabel} from "@/pages/SimulationPage/SimulationPage.style.ts";
import type {AddVehicleRequest} from "@/api/types.ts";

interface AddVehiclePanelProps {
    isActive: boolean;
    loading: boolean;
    addVehicle: (vehicle: AddVehicleRequest) => Promise<void>;
}

const AddVehiclePanel = ({isActive, loading, addVehicle}: AddVehiclePanelProps) => {

    const [startRoad, setStartRoad] = useState<RoadDirection>('NORTH');
    const [endRoad, setEndRoad] = useState<RoadDirection>('SOUTH');
    const [carIndex, setCarIndex] = useState(1);

    const handleAddVehicle = useCallback(async () => {
        await addVehicle({
            vehicleId: `car${carIndex}`,
            startRoad,
            endRoad,
        });
        setCarIndex(i => i + 1);
    }, [addVehicle, startRoad, endRoad, carIndex]);
    return (
        <AddVehiclePanelWrapper>
            <SidebarLabel>Add Vehicle</SidebarLabel>

            <SelectRow>
                <SelectLabel>From</SelectLabel>
                <RoadSelect
                    value={startRoad}
                    onChange={e => setStartRoad(e.target.value as RoadDirection)}
                >
                    <option value="NORTH">NORTH</option>
                    <option value="SOUTH">SOUTH</option>
                    <option value="EAST">EAST</option>
                    <option value="WEST">WEST</option>
                </RoadSelect>
            </SelectRow>

            <SelectRow>
                <SelectLabel>To</SelectLabel>
                <RoadSelect
                    value={endRoad}
                    onChange={e => setEndRoad(e.target.value as RoadDirection)}
                >
                    <option value="NORTH">NORTH</option>
                    <option value="SOUTH">SOUTH</option>
                    <option value="EAST">EAST</option>
                    <option value="WEST">WEST</option>
                </RoadSelect>
            </SelectRow>

            <AddBtn
                onClick={handleAddVehicle}
                $disabled={!isActive || loading}
                disabled={!isActive || loading}
            >
                + Add
            </AddBtn>
        </AddVehiclePanelWrapper>
    )
};
export default AddVehiclePanel;