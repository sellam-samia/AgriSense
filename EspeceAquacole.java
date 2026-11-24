public class EspeceAquacole {
    private String nom;
    private int nombreIndividus;
    private ProgrammeAlimentation programme;
    

    public EspeceAquacole(String nom, int nombreIndividus) {
        if (nom == null )
        throw new IllegalArgumentException("Nom espece invalide !");
        if (nombreIndividus <= 0)
        throw new IllegalArgumentException("Nombre individus doit être positif !");
        this.nom = nom;
        this.nombreIndividus = nombreIndividus;
    }

    public String getNom() { return nom; }
    public int getNombreIndividus() { return nombreIndividus; }

    public ProgrammeAlimentation getProgramme() { return programme; }

    public void definirProgramme(ProgrammeAlimentation programme) {
        this.programme = programme;
    }
    
    

  
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom).append(" (").append(nombreIndividus).append(" individus)");
        if (programme != null) {
            sb.append(" | Programme: ").append(programme.getDetails());
        }
        return sb.toString();
    }
}