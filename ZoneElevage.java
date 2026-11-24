import java.util.ArrayList;

public class ZoneElevage extends Zone {
    private ArrayList<Animal> animaux = new ArrayList<Animal>();
    private ProgrammeAlimentation programme;
    private LimitesGeographiques limites;
    private TypeElevage typeElevage;
    
    public ZoneElevage(int code, String name) {
        this(code, name, TypeElevage.RUMINANT);
    }

    public ZoneElevage(int code, String name, TypeElevage typeElevage) {
        super(code, name);
        this.typeElevage = typeElevage == null ? TypeElevage.RUMINANT : typeElevage;
    }
    
    public void ajouterAnimal(Animal animal) {
    if (animal == null) return;
     if (getStatut() == StatutZone.SUSPENDUE) {// verifier zone n'est pas suspendue
        StringBuilder sb = new StringBuilder();
        sb.append("Erreur : zone suspendue !");
        System.out.println(sb.toString());
        return;
    }
    // verifier cide unique
    for (Animal a : animaux) {
        if (a.getCode() == animal.getCode()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Erreur : animal ").append(animal.getCode())
              .append(" déjà existant !");
            System.out.println(sb.toString());
            return;
        }
    }
    animaux.add(animal);
}
    
    public void retirerAnimal(Animal animal) {
        animaux.remove(animal);
    }
    
    public ArrayList<Animal> getAnimaux() {
        return animaux;
    }
    
    public void afficherAnimaux() {
        if (animaux.isEmpty()) {
            System.out.println("Aucun animal.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Animal a : animaux) {
            sb.append("Code: ").append(a.getCode())
              .append(", Age: ").append(a.getAge())
              .append(", Poids: ").append(a.getPoids())
              .append(", Etat de santé: ").append(a.getEtat_sante())
              .append("\n");
        }
        System.out.print(sb.toString());
    }
    public String afficherVueEnsemble() {
        StringBuilder sb = new StringBuilder(super.afficherVueEnsemble());
        sb.append(" | Type: ").append(typeElevage);
        sb.append(" | Animaux: ").append(animaux.size());
        return sb.toString();
}

public void definirProgramme(ProgrammeAlimentation p) {
        this.programme = p;
    StringBuilder sb = new StringBuilder();
    sb.append("Programme défini : ").append(p.getDetails());
    System.out.println(sb.toString());
    }
    public void afficherProgramme() {
        if (programme == null) {
            System.out.println("Aucun programme défini.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Programme : ").append(programme.getDetails());
        System.out.println(sb.toString());
    }
    public ProgrammeAlimentation getProgramme() { return programme; }
    public TypeElevage getTypeElevage() { return typeElevage; }
    public void setTypeElevage(TypeElevage typeElevage) { this.typeElevage = typeElevage == null ? TypeElevage.RUMINANT : typeElevage; }
     public void setLimites(LimitesGeographiques l) { this.limites = l; }
     public LimitesGeographiques getLimites()        { return limites; }
}
