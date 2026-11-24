public class PlageSeuils {
    private double valeurMin;
    private double valeurMax;

    public PlageSeuils(double valeurMin, double valeurMax) {
        if (valeurMin > valeurMax) // ← > au lieu de >= pour autoriser GPS (0,0)
            throw new IllegalArgumentException(
                "Erreur : min (" + valeurMin + ") doit être <= max (" + valeurMax + ") !");
        this.valeurMin = valeurMin;
        this.valeurMax = valeurMax;
    }

    public double  getValeurMin()         { return valeurMin; }
    public double  getValeurMax()         { return valeurMax; }
    public boolean estDansPlage(double v) { return v >= valeurMin && v <= valeurMax; }
    public boolean estHorsPlage(double v) { return !estDansPlage(v); }

    @Override
    public String toString() {
        return "[" + valeurMin + " - " + valeurMax + "]";
    }
}