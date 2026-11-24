import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Zone implements Susponsion_interface {
    private int code;
    private String nom;
    private StatutZone statut;
    private List<Capteur> capteurs  = new ArrayList<>();
    private List<Alerte>  alertes   = new ArrayList<>(); 
    private List<ProductionRecord> historique = new ArrayList<>();
   

    public Zone(int code, String nom) {
        this.code   = code;
        this.nom    = nom;
        this.statut = StatutZone.ACTIVE;
    }

    
    public void ajouterAlerte(Alerte a) {
        alertes.add(a);
    }

    public List<Alerte> getAlertes() {
        return alertes;
    }

    public int getCode()             { return code; }
    public String getNom()           { return nom; }
    public StatutZone getStatut()    { return statut; }
    public List<Capteur> getCapteurs(){ return capteurs; }

    public void setCode(int code)    { this.code = code; }
    public void setNom(String nom)   { this.nom = nom; }

   public void suspendre(){
       this.statut = StatutZone.SUSPENDUE;
     
       for (Capteur c : capteurs) {
         if (c.getStatut() == StatutCapteur.DEFAILLANT) {
            continue;
       }
       c.suspendre();
   }}
  public void activer(){
       this.statut = StatutZone.ACTIVE;
       for (Capteur c : capteurs) {
        if (c.getStatut() == StatutCapteur.DEFAILLANT) {
           continue;
       }
       c.reactiver();
   }
}

    public void ajouterCapteur(Capteur c) {
        c.setZone(this);
        capteurs.add(c);
    }
  public String afficherVueEnsemble() {
    StringBuilder sb = new StringBuilder();
    sb.append("Zone[").append(code).append("] ")
      .append(nom)
      .append(" | Type: ").append(this.getClass().getSimpleName())
      .append(" | Statut: ").append(statut)
      .append(" | Capteurs: ").append(capteurs.size());
    return sb.toString();
}
//production
public void enregistrerProduction(ProductionRecord p) {
    if (p != null) {
        historique.add(p);
        StringBuilder sb = new StringBuilder();
        sb.append("Production enregistrée : ").append(p.getResume());
        System.out.println(sb.toString());
    }
}

public void afficherHistoriqueProduction() {
    if (historique.isEmpty()) {
        System.out.println("Aucune production enregistrée.");
        return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("=== Historique production : ").append(nom).append(" ===\n");
    for (ProductionRecord p : historique) {
        sb.append(p.getResume()).append("\n");
    }
    System.out.print(sb.toString());
}
public List<ProductionRecord> getHistorique() {
    return historique;
}



    @Override
    public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("Code: ").append(code)
                    .append(", Nom: ").append(nom)
                    .append(", Statut: ").append(statut);
                return sb.toString();
    }
}