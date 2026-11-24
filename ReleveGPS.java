public class ReleveGPS extends Releve {
    private double latitude;
    private double longitude;

    public ReleveGPS(double latitude, double longitude) {
        super();
        this.latitude     = latitude;
        this.longitude    = longitude;
        this.niveauReleve = NiveauReleve.NORMAL;
    }

    public double getLatitude()  { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReleveGPS[lat=").append(latitude)
          .append(" lon=").append(longitude)
          .append("]");
        return sb.toString();
    }
}