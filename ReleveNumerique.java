import java.util.Date;

public class ReleveNumerique extends Releve {
    private double      valeur;
    private UniteMesure unite; // ← UniteMesure enum

    public ReleveNumerique(double valeur, UniteMesure unite, PlageSeuils seuils) {
        super();
        this.valeur       = valeur;
        this.unite        = unite;
        this.niveauReleve = calculerNiveau(valeur, seuils);
    }

    private NiveauReleve calculerNiveau(double valeur, PlageSeuils seuils) {
        double min   = seuils.getValeurMin();
        double max   = seuils.getValeurMax();
        double marge = (max - min) * 0.10;
        if (valeur < min || valeur > max)    return NiveauReleve.CRITIQUE;
        if (valeur < (min+marge) || valeur > (max-marge)) return NiveauReleve.AVERTISSEMENT;
        return NiveauReleve.NORMAL;
    }

    public double      getValeur() { return valeur; }
    public UniteMesure getUnite()  { return unite; }

    @Override
    public String toString() {
        return "ReleveNumerique[" + valeur + " " + unite.getSymbole()
             + " | " + niveauReleve + " | " + date + "]";
    }
}