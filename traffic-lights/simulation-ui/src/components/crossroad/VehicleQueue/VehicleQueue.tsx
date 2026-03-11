import {AnimatePresence, motion} from 'framer-motion';
import type {VehicleDto} from '@/types/simulation.ts';
import {CAR_GAP, CAR_H, CAR_W, EXIT_DURATION, MAX_VISIBLE} from './config.ts';
import Car from './Car/Car.tsx';
import OverflowBadge from './OverflowBadge.tsx';
import {ANIMATION} from "./util.ts";

interface VehicleQueueProps {
    vehicles: VehicleDto[];
}

const step = CAR_H + CAR_GAP;

export function VehicleQueue({vehicles}: VehicleQueueProps) {
    const visible = vehicles.slice(0, MAX_VISIBLE);
    const overflow = vehicles.length - visible.length;

    return (
        <g>
            <AnimatePresence>
                {visible.map((vehicle, i) => {

                    const movement = vehicle.movement;
                    const anim = ANIMATION[movement];
                    const duration = EXIT_DURATION[movement];
                    const targetY = -i * step;

                    return (
                        <motion.g
                            key={vehicle.vehicleId}
                            initial={{opacity: 0, y: -step * (i + 1)}}
                            animate={{opacity: 1, y: targetY}}
                            transition={{
                                duration: 0.45,
                                ease: 'easeInOut',
                            }}
                            exit="exit"
                            variants={{
                                exit: {
                                    x: anim.x,
                                    y: anim.y,
                                    rotate: anim.rotate,
                                    opacity: anim.opacity,
                                    transition: {
                                        duration,
                                        ease: 'easeInOut',
                                    },
                                },
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
                    <OverflowBadge count={overflow}/>
                </g>
            )}
        </g>
    );
}