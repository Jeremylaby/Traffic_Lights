package pl.jeremy.simulation.factory;

public enum TrafficLightMode {
    SIMPLE,
    BROKEN,
    ADAPTIVE,
    PROPORTIONAL,
    FEEDBACK;

    public static TrafficLightMode from(String value) {
        if (value == null || value.isBlank()) {
            return SIMPLE;
        }

        try {
            return TrafficLightMode.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown traffic light mode: " + value + ". Allowed values: SIMPLE, BROKEN");
        }
    }
}
