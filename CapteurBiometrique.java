public class CapteurBiometrique extends CapteurNumerique {
    private double temperatureCorporelle;
    private int    niveauActivite;

    public CapteurBiometrique() { super(); }

    public double getTemperatureCorporelle() { return temperatureCorporelle; }
    public int    getNiveauActivite()        { return niveauActivite; }

    public void setTemperatureCorporelle(double v) { this.temperatureCorporelle = v; }
    public void setNiveauActivite(int v)           { this.niveauActivite        = v; }

    public void ajouterReleveTemperature(double valeur) {
        ajouterReleve(valeur, UniteMesure.CELSIUS.getSymbole());
    }

    public void ajouterReleveActivite(double valeur) {
        ajouterReleve(valeur, UniteMesure.BATTEMENT_PAR_MINUTE.getSymbole());
    }
}