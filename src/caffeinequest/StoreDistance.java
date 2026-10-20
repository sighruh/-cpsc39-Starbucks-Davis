package caffeinequest;

import java.util.Comparator;

/** Cache the distance once, rather than recalculating it during comparisons. */
public final class StoreDistance {
    public final StarbucksStore store;
    public final double miles;

    public StoreDistance(StarbucksStore store, double miles) {
        this.store = store;
        this.miles = miles;
    }

    public static final Comparator<StoreDistance> NEAREST_FIRST = (a, b) -> {
        int distanceOrder = Double.compare(a.miles, b.miles);
        return distanceOrder != 0 ? distanceOrder : Integer.compare(a.store.id, b.store.id);
    };

    /** Haversine: great-circle distance, NOT road distance or travel time. */
    public static double milesBetween(double lat1, double lon1, double lat2, double lon2) {
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(deltaLon / 2), 2);
        a = Math.max(0, Math.min(1, a)); // Protect against floating-point roundoff.
        return 3958.7613 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
