package caffeinequest;

/** One store record. Coordinates use decimal degrees. */
public final class StarbucksStore {
    public final int id;
    public final String name, city, address;
    public final double latitude, longitude;
    public final boolean driveThrough;

    public StarbucksStore(int id, String name, String city, String address,
            double latitude, double longitude, boolean driveThrough) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.driveThrough = driveThrough;
    }
}
