import java.time.LocalDate;

public class EvenementSanitaire {
    private LocalDate date;
    private TypeEvenementSanitaire type;
    private String description;
    private double ancienPoids;
    private double nouveauPoids;

    // Constructeur pour MALADIE
    public EvenementSanitaire(LocalDate date,
                               TypeEvenementSanitaire type,
                               String description) {
        this.date = date;
        this.type = type;
        this.description = description;
        this.ancienPoids = 0;
        this.nouveauPoids = 0;
    }
    public EvenementSanitaire(LocalDate date,
                               TypeEvenementSanitaire type,
                               String description,
                               double ancienPoids,
                               double nouveauPoids) {
        this.date = date;
        this.type = type;
        this.description = description;
        this.ancienPoids = ancienPoids;
        this.nouveauPoids = nouveauPoids;
    }
    public LocalDate getDate() { return date; }
    public TypeEvenementSanitaire getType() { return type; }
    public String getDescription() { return description; }
    public double getAncienPoids() { return ancienPoids; }
    public double getNouveauPoids() { return nouveauPoids; }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(date).append(" | ").append(type).append(" | ");
        if (type == TypeEvenementSanitaire.EVOLUTION_POIDS) {
            sb.append(ancienPoids).append("kg → ").append(nouveauPoids).append("kg");
        } else {
            sb.append(description);
        }
        return sb.toString();
    }

    
}