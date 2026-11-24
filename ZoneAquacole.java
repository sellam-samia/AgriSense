import java.util.ArrayList;

public class ZoneAquacole extends Zone {
    private ArrayList<EspeceAquacole> especesAquacoles = new ArrayList<EspeceAquacole>();
    private ProgrammeAlimentation programme;
    
    public ZoneAquacole(int code, String name) {
        super(code, name);
    }

    public void ajouterEspece(EspeceAquacole espece) {
        if (espece != null && !especesAquacoles.contains(espece)) {
            especesAquacoles.add(espece);
        }
    }

    public void retirerEspece(EspeceAquacole espece) {
        especesAquacoles.remove(espece);
    }

    public ArrayList<EspeceAquacole> getEspecesAquacoles() {
        return especesAquacoles;
    }

    public EspeceAquacole trouverEspece(String nom) {
        if (nom == null) return null;
        for (EspeceAquacole espece : especesAquacoles) {
            if (nom.equalsIgnoreCase(espece.getNom())) {
                return espece;
            }
        }
        return null;
    }

    public ProgrammeAlimentation getProgramme() {
        return programme;
    }

    public void definirProgramme(String nomEspece, ProgrammeAlimentation p) {
        EspeceAquacole espece = trouverEspece(nomEspece);
        if (espece == null) {
            System.out.println("Espèce aquacole introuvable : " + nomEspece);
            return;
        }
        espece.definirProgramme(p);
        StringBuilder sb = new StringBuilder();
        sb.append("Programme défini pour ").append(espece.getNom())
          .append(" : ").append(p.getDetails());
        System.out.println(sb.toString());
    }

    public int getNombreTotalIndividus() {
        int total = 0;
        for (EspeceAquacole e : especesAquacoles) {
            total += e.getNombreIndividus();
        }
        return total;
    }
    public void definirProgramme(ProgrammeAlimentation p) {
        this.programme = p;
        StringBuilder sb = new StringBuilder();
        sb.append("Programme défini : ").append(p.getDetails());
        System.out.println(sb.toString());
    }

    public void afficherProgramme() {
        boolean any = false;
        if (programme != null) {
            System.out.println("Programme de la zone : " + programme.getDetails());
            any = true;
        }
        if (!especesAquacoles.isEmpty()) {
            System.out.println("Programmes par espèce :");
            for (EspeceAquacole e : especesAquacoles) {
                if (e.getProgramme() != null) {
                    System.out.println("  - " + e.getNom() + " : " + e.getProgramme().getDetails());
                    any = true;
                } else {
                    System.out.println("  - " + e.getNom() + " : Aucun programme défini.");
                }
            }
        }
        if (!any) {
            System.out.println("Aucun programme défini pour la zone ni pour les espèces.");
        }
    }

    
    public String afficherVueEnsemble() {
        StringBuilder sb = new StringBuilder(super.afficherVueEnsemble());
        sb.append(" | Espèces: ").append(especesAquacoles.size());
        long programmes = especesAquacoles.stream().filter(e -> e.getProgramme() != null).count();
        sb.append(" | Programmes espèce: ").append(programmes);
        return sb.toString();
}
}
