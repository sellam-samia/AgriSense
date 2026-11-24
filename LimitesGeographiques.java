public class LimitesGeographiques {
    private double latMin;
    private double latMax;
    private double lonMin;
    private double lonMax;

    public LimitesGeographiques(double latMin, double latMax,
                                 double lonMin, double lonMax) {
        this.latMin = latMin;
        this.latMax = latMax;
        this.lonMin = lonMin;
        this.lonMax = lonMax;
    }

    public boolean contient(double lat, double lon) {
        return lat >= latMin && lat <= latMax
            && lon >= lonMin && lon <= lonMax;
    }

    public double getLatMin() { return latMin; }
    public double getLatMax() { return latMax; }
    public double getLonMin() { return lonMin; }
    public double getLonMax() { return lonMax; }

    @Override
    public String toString() {
        return "Limites["
            + "lat(" + latMin + " - " + latMax + ")"
            + " lon(" + lonMin + " - " + lonMax + ")]";
    }
}