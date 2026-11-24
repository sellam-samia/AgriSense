public class ProgrammeAlimentation {
    private String      typeAliment;
    private double      quantiteParRepas;
    private UniteMesure unite;

    public ProgrammeAlimentation(String typeAliment, double quantiteParRepas) {
        this(typeAliment, quantiteParRepas, UniteMesure.KG);
    }

    public ProgrammeAlimentation(String typeAliment, double quantiteParRepas,
                                   UniteMesure unite) {
        if (typeAliment == null || typeAliment.trim().isEmpty())
            throw new IllegalArgumentException("Type aliment invalide !");
        if (quantiteParRepas <= 0)
            throw new IllegalArgumentException("Quantite doit etre positive !");
        this.typeAliment      = typeAliment;
        this.quantiteParRepas = quantiteParRepas;
        this.unite            = unite;
    }

    public String      getTypeAliment()      { return typeAliment; }
    public double      getQuantiteParRepas() { return quantiteParRepas; }
    public UniteMesure getUnite()            { return unite; }

    public String getDetails() {
        return typeAliment + " | " + quantiteParRepas
             + " " + unite.getSymbole() + " par repas";
    }

    @Override
    public String toString() { return getDetails(); }
}