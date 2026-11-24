public class Production {
    private String nom;
    private double quantite;
    private UniteMesure unite;

    public Production(String nom, double quantite, UniteMesure unite) {
        this.nom = nom;
        this.quantite = quantite;
        this.unite = unite;
    }

    public String getNom() {
        return nom;
    }

    public double getQuantite() {
        return quantite;
    }

    public UniteMesure getUnite() {
        return unite;
    }
}