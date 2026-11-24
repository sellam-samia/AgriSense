public class CapteurSol extends CapteurNumerique {
    private double pH;
    private double tauxHumidite;
    private double teneurAzote;

    public CapteurSol() { super(); }

    public double getPH()           { return pH; }
    public double getTauxHumidite() { return tauxHumidite; }
    public double getTeneurAzote()  { return teneurAzote; }

    public void setPH(double v)           { this.pH           = v; }
    public void setTauxHumidite(double v) { this.tauxHumidite = v; }
    public void setTeneurAzote(double v)  { this.teneurAzote  = v; }

    public void ajouterRelevePH(double valeur) {
        ajouterReleve(valeur, UniteMesure.PH.getSymbole());
    }

    public void ajouterReleveHumidite(double valeur) {
        ajouterReleve(valeur, UniteMesure.POURCENTAGE.getSymbole());
    }

    public void ajouterReleveAzote(double valeur) {
        ajouterReleve(valeur, UniteMesure.MILLIGRAMME_PAR_LITRE.getSymbole());
    }
}