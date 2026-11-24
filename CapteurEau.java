public class CapteurEau extends CapteurNumerique {
    private double temperature;
    private double oxygeneDissouss;

    public CapteurEau() { super(); }

    public double getTemperature()     { return temperature; }
    public double getOxygeneDissouss() { return oxygeneDissouss; }

    public void setTemperature(double v)     { this.temperature     = v; }
    public void setOxygeneDissouss(double v) { this.oxygeneDissouss = v; }

    public void ajouterReleveTemperature(double valeur) {
        ajouterReleve(valeur, UniteMesure.CELSIUS.getSymbole());
    }

    public void ajouterReleveOxygene(double valeur) {
        ajouterReleve(valeur, UniteMesure.MILLIGRAMME_PAR_LITRE.getSymbole());
    }
}