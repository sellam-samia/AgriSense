import java.time.LocalDate;

public class ProductionRecord {
    private LocalDate      date;
    private TypeProduction type;
    private double         valeur;
    private UniteMesure    unite;

    public ProductionRecord(TypeProduction type, double valeur) {
        this(type, valeur, determinerUnite(type));
    }

    public ProductionRecord(TypeProduction type, double valeur, UniteMesure unite) {
        if (valeur <= 0)
            throw new IllegalArgumentException("La valeur de production doit etre positive !");
        this.date   = LocalDate.now();
        this.type   = type;
        this.valeur = valeur;
        this.unite  = unite;
    }

    public LocalDate      getDate()    { return date; }
    public TypeProduction getType()    { return type; }
    public double         getValeur()  { return valeur; }
    public UniteMesure    getUnite()   { return unite; }

    private static UniteMesure determinerUnite(TypeProduction type) {
        switch (type) {
            case LAITIER:       return UniteMesure.LITRE;
            case OEUFS:         return UniteMesure.UNITE;
            case POIDS_RECOLTE: return UniteMesure.KG;
            case RENDEMENT:     return UniteMesure.KG;
            default:            return UniteMesure.KG;
        }
    }

    public String getResume() {
        return date + " | " + type + " | " + valeur + " " + unite.getSymbole();
    }

    @Override
    public String toString() { return getResume(); }
}