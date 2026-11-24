import java.time.LocalDate;

public class Culture {
    private String nom;
    private FamilleCulture famille;        
    private LocalDate date_plantation;
    private LocalDate date_recolte;
    private StadeDeCroissance stade_croissance;
    private ExigencesPedologiques exigencesPedologiques;

    public Culture(String nom, FamilleCulture famille,
                   LocalDate date_plantation, LocalDate date_recolte,
                   ExigencesPedologiques exigences) {
        // Validate name
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Nom de culture invalide !");
        }
        // Validate dates
        if (date_plantation == null || date_recolte == null) {
            throw new IllegalArgumentException("Dates de plantation et de récolte requises !");
        }
        if (!date_recolte.isAfter(date_plantation)) {
            throw new IllegalArgumentException("Date récolte doit être après date plantation !");
        }

        this.nom = nom;
        this.famille = famille;
        this.date_plantation = date_plantation;
        this.date_recolte = date_recolte;
        this.stade_croissance = StadeDeCroissance.SEMIS;
        this.exigencesPedologiques = exigences;
    }

    // Getters
    public String getNom() { return nom; }
    public FamilleCulture getFamille() { return famille; }
    public LocalDate getDatePlantation() { return date_plantation; }
    public LocalDate getDateRecolte() { return date_recolte; }
    public StadeDeCroissance getStadeCroissance() { return stade_croissance; }
    public ExigencesPedologiques getExigences() { return exigencesPedologiques; }


    public void mettreAJourStade(StadeDeCroissance s) {
        if (s == null) {
            System.out.println("Stade invalide.");
            return;
        }
        if (s.ordinal() <= this.stade_croissance.ordinal()) {
            System.out.println("Mise à jour refusée : le stade de croissance ne peut pas diminuer (actuel="
                               + this.stade_croissance + ", demandé=" + s + ").");
            return;
        }
        this.stade_croissance = s;
        StringBuilder sb = new StringBuilder();
        sb.append("Stade mis à jour : ").append(nom)
          .append(" --> ").append(s);
        System.out.println(sb.toString());
    }

   
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom).append(" (").append(famille).append(")")
          .append(" | Planté: ").append(date_plantation)
          .append(" | Récolte prévue: ").append(date_recolte)
          .append(" | Stade: ").append(stade_croissance)
          .append(" | Exigences: ").append(exigencesPedologiques);
        return sb.toString();
    }
}