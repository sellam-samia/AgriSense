public class CapteurGPS extends Capteur {
    private double latitude;
    private double longitude;

    public CapteurGPS() { super(); }

    public double getLatitude()  { return latitude; }
    public double getLongitude() { return longitude; }

    public void setLatitude(double v)  { this.latitude  = v; }
    public void setLongitude(double v) { this.longitude = v; }

    public String ajouterReleveGPS(double latitude, double longitude) {
        this.latitude  = latitude;
        this.longitude = longitude;

        ReleveGPS r = new ReleveGPS(latitude, longitude);
        getReleves().add(r);
        String message = "Relevé GPS ajouté pour le capteur " + getCode() + " : ("
            + UniteMesure.DEGRE.getSymbole() + latitude + ", "
            + UniteMesure.DEGRE.getSymbole() + longitude + ")";
        System.out.println(message);

        if (getZone() instanceof ZoneElevage) {
            ZoneElevage ze = (ZoneElevage) getZone();
            if (ze.getLimites() != null
             && !ze.getLimites().contient(latitude, longitude)) {
                Alerte alerte = new Alerte(r, this, NiveauGravite.CRITIQUE);
                getZone().ajouterAlerte(alerte);
                System.out.println("ALERTE GPS : animal hors zone !"
                    + " (" + latitude + ", " + longitude + ")");
            }
        }
        return message;
    }
}