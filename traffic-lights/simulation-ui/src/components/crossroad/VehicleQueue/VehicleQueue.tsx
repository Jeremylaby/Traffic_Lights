import { AnimatePresence, motion } from 'framer-motion';
import type { VehicleDto } from '@/types/simulation.ts';
import { CAR_GAP, CAR_H, CAR_W, MAX_VISIBLE } from '@/components/crossroad/VehicleQueue/config.ts';
import Car from '@/components/crossroad/VehicleQueue/Car/Car.tsx';
import OverflowBadge from '@/components/crossroad/VehicleQueue/OverflowBadge.tsx';

interface VehicleQueueProps {
    vehicles: VehicleDto[];
}

const step = CAR_H + CAR_GAP;

export function VehicleQueue({ vehicles }: VehicleQueueProps) {
    const visible  = vehicles.slice(0, MAX_VISIBLE);
    const overflow = vehicles.length - visible.length;

    return (
        <g>
            <AnimatePresence>
                {visible.map((vehicle, i) => {
                    const targetY = -i * step;

                    return (
                        <motion.g
                            key={vehicle.vehicleId}
                            initial={{ opacity: 0, y: -step * (i+1 )}}
                            animate={{ opacity: 1, y: targetY }}
                            exit={{ opacity: 0, y: CAR_H * 2 }}
                            transition={{
                                duration: 0.45,
                                ease: 'easeInOut',
                            }}
                        >
                            <Car
                                x={0}
                                y={0}
                                vehicleId={vehicle.vehicleId}
                                movement={vehicle.movement}
                            />
                        </motion.g>
                    );
                })}
            </AnimatePresence>

            {overflow > 0 && (
                <g transform={`translate(${CAR_W / 2}, ${-visible.length * step + step / 2})`}>
                    <OverflowBadge count={overflow} />
                </g>
            )}
        </g>
    );
}