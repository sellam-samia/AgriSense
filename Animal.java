
import java.util.ArrayList;
import java.util.List;

public class Animal {
  private int code;
  private String espece;
  private int age;  
  private int poids;
  private EtatDesante etat_sante;  
  private List<EvenementSanitaire> historiqueSanitaire = new ArrayList<>();

  private ArrayList<CapteurBiometrique> cap_bio=new ArrayList<>();
  private ArrayList<CapteurEau> cap_eau=new ArrayList<>();

  public Animal(int code, String espece, int age, int poids, EtatDesante etat_sante) {
    if (espece == null )
        throw new IllegalArgumentException("Espece invalide !");
    if (age <= 0)
        throw new IllegalArgumentException("Age invalide !");
    if (poids <= 0)
        throw new IllegalArgumentException("Poids invalide !");
    this.code = code;
    this.espece = espece;
    this.age = age;
    this.poids = poids;
    this.etat_sante = etat_sante;
  }
  public int getCode() {
    return code;
  }
  public String getEspece() {
    return espece;
  }
  public int getAge() {
    return age;
  }
  public int getPoids() {
    return poids;
   }
   
  public EtatDesante getEtat_sante() {
    return etat_sante;  
  }
  public List<EvenementSanitaire> getHistoriqueSanitaire() {
        return historiqueSanitaire;
    }

  public ArrayList<CapteurBiometrique> getCapteursBio() {
    return cap_bio;
  }

  public ArrayList<CapteurEau> getCapteursEau() {
    return cap_eau;
  }
  
    

  public void ajouterCapteurBio(CapteurBiometrique capteur) {
    if (capteur != null && !cap_bio.contains(capteur)) {
      cap_bio.add(capteur);
    }
  }

  public void ajouterCapteurEau(CapteurEau capteur) {
    if (capteur != null && !cap_eau.contains(capteur)) {
      cap_eau.add(capteur);
    }
  }

  public void retirerCapteurBio(CapteurBiometrique capteur) {
    cap_bio.remove(capteur);
  }

  public void retirerCapteurEau(CapteurEau capteur) {
    cap_eau.remove(capteur);
  }

  public void setCode(int code) {
    this.code = code;
  }

  public void setEspece(String espece) {
    this.espece = espece;
  }

  public void setAge(int age) {
    this.age = age;
  }


 public void setPoids(int poids) {
    if (poids <= 0) {
        System.out.println("Erreur : poids invalide !");
        return;
    }
    this.poids = poids;
}

  public void setEtat_sante(EtatDesante etat_sante) {
    this.etat_sante = etat_sante;
  }

  public void enregistrerEvenement(EvenementSanitaire e) {
    historiqueSanitaire.add(e);
    
    if (e.getType() == TypeEvenementSanitaire.EVOLUTION_POIDS) {
      this.poids = (int) e.getNouveauPoids();
        StringBuilder sb = new StringBuilder();
        sb.append("Poids mis à jour : ")
          .append(e.getAncienPoids()).append("kg -->")
          .append(e.getNouveauPoids()).append("kg");
        System.out.println(sb.toString());
    }
}
public void afficherHistorique() {
        if (historiqueSanitaire.isEmpty()) {
            System.out.println("Aucun événement sanitaire.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== Historique : ").append(espece)
          .append(" [").append(code).append("] ===\n");
        for (EvenementSanitaire e : historiqueSanitaire) {
            sb.append(e).append("\n");
        }
        System.out.print(sb.toString());
    }

   
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[").append(code).append("] ").append(espece)
      .append(" | Age: ").append(age)
      .append(" | Poids: ").append(poids).append("kg")
      .append(" | Santé: ").append(etat_sante);
    return sb.toString();
}

}
