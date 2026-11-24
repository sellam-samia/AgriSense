import java.util.ArrayList;

public class ZoneCulture extends Zone {
    
    private ArrayList<Culture> cultures = new ArrayList<Culture>();
    
    public ZoneCulture(int code, String name) {
        super(code, name);
    }

    
    public void ajouterCulture(Culture culture) {
    if (culture == null) return;   
    if (getStatut() == StatutZone.SUSPENDUE) {
        System.out.println("Erreur : zone suspendue, impossible d'ajouter !");
        return;
    }
    cultures.add(culture);
}

    public void retirerCulture(Culture culture) {
        cultures.remove(culture);
    }

    public ArrayList<Culture> getCultures() {
        return cultures;
    }

    public void afficherCultures() {
        if (cultures.isEmpty()) {
            System.out.println("Aucune culture.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Culture c : cultures) {
            sb.append(c.toString()).append("\n");
        }
        System.out.print(sb.toString());
    }

   public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(" | Cultures: ").append(cultures.size());
    return sb.toString();
}
public void mettreAJourStade(String nomCulture, StadeDeCroissance stade) {
        for (Culture c : cultures) {
            if (c.getNom().equals(nomCulture)) {
                c.mettreAJourStade(stade);
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Culture introuvable : ").append(nomCulture);
        System.out.println(sb.toString());
    }

    public String getRapportCultures() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Rapport cultures : ").append(getNom()).append(" ===\n");
        if (cultures.isEmpty()) {
            sb.append("Aucune culture.\n");
            return sb.toString();
        }
        for (Culture c : cultures) {
            sb.append(c.toString()).append("\n");
        }
        return sb.toString();
    }
    
}

