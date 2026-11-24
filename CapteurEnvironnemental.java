public class CapteurEnvironnemental extends CapteurNumerique {
    private double temperature;
    private double humidite;
    private double pluviometrie;

    public CapteurEnvironnemental() { super(); }

    public double getTemperature()  { return temperature; }
    public double getHumidite()     { return humidite; }
    public double getPluviometrie() { return pluviometrie; }

    public void setTemperature(double v)  { this.temperature  = v; }
    public void setHumidite(double v)     { this.humidite     = v; }
    public void setPluviometrie(double v) { this.pluviometrie = v; }

    public void ajouterReleveTemperature(double valeur) {
        ajouterReleve(valeur, UniteMesure.CELSIUS.getSymbole());
    }

    public void ajouterReleveHumidite(double valeur) {
        ajouterReleve(valeur, UniteMesure.POURCENTAGE.getSymbole());
    }

    public void ajouterRelevePluviometrie(double valeur) {
        ajouterReleve(valeur, UniteMesure.MILLIMETRE.getSymbole());
    }
}