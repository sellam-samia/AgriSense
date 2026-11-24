public class ExigencesPedologiques {
    private double phMin;
    private double phMax;
    private double humiditeMin;
    private double humiditeMax;

    public ExigencesPedologiques(double phMin, double phMax,
                                  double humiditeMin, double humiditeMax) {
        if (phMin < 0 || phMax > 14 || phMin >= phMax)
            throw new IllegalArgumentException("pH doit etre entre 0 et 14 et min < max !");
        if (humiditeMin < 0 || humiditeMax > 100 || humiditeMin >= humiditeMax)
            throw new IllegalArgumentException("Humidite invalide !");
        this.phMin      = phMin;
        this.phMax      = phMax;
        this.humiditeMin = humiditeMin;
        this.humiditeMax = humiditeMax;
    }

    public boolean estSolAdapte(double pH, double humidite) {
        return pH >= phMin && pH <= phMax
            && humidite >= humiditeMin && humidite <= humiditeMax;
    }

    public double getPhMin()       { return phMin; }
    public double getPhMax()       { return phMax; }
    public double getHumiditeMin() { return humiditeMin; }
    public double getHumiditeMax() { return humiditeMax; }

    @Override
    public String toString() {
        return "pH[" + phMin + "-" + phMax + "] Humidite[" + humiditeMin + "-" + humiditeMax + "%]";
    }
}