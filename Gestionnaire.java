import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class Gestionnaire {
    private List<Zone> zones = new ArrayList<>();

    public List<Zone> getZones() {
        return zones;
    }

    public void ajouterZone(Zone z) {

    if (chercherzone(z.getCode()) != null) {
        System.out.println("Erreur : code " + z.getCode() + " déjà utilisé !");
        return;
    }
    zones.add(z);
    System.out.println("Zone ajoutée : " + z.afficherVueEnsemble());
}



    public void ajouterEtConfigurerCapteur(String type, Zone zone,
                                        double valeurMin, double valeurMax) {

    Capteur capteur;
    switch (type) {
        case "SOL":
            capteur = new CapteurSol();
            break;
        case "ENVIRONNEMENTAL":
            capteur = new CapteurEnvironnemental();
            break;
        case "GPS":
            capteur = new CapteurGPS();
            break;
        case "BIOMETRIQUE":
            capteur = new CapteurBiometrique();
            break;
        case "EAU":
            capteur = new CapteurEau();
            break;
        default:
            System.out.println("Type de capteur inconnu !");
            return;
    }

    capteur.setPlageSeuils(new PlageSeuils(valeurMin, valeurMax));
    capteur.setStatut(StatutCapteur.ACTIF);
    zone.ajouterCapteur(capteur);

    System.out.println("Capteur " + type + " ajoute dans " + zone.getNom()
                     + " | Seuils [" + valeurMin + ", " + valeurMax + "]");
}
 public void afficherTableauDeBordReleves(Zone zone) {

    System.out.println("=== Tableau de bord : " + zone.getNom() + " ===");
    System.out.println("Type   : " + zone.getClass().getSimpleName());
    System.out.println("Statut : " + zone.getStatut());

    if (zone.getCapteurs().isEmpty()) {
        System.out.println("Aucun capteur dans cette zone.");
        return;
    }

    for (Capteur c : zone.getCapteurs()) {
        System.out.println("  Capteur : " + c.getCode()
                         + " [" + c.getClass().getSimpleName() + "]"
                         + " statut=" + c.getStatut());

        if (c instanceof CapteurNumerique) {
            System.out.println("  Seuils : ["
                + c.getPlageSeuils().getValeurMin()
                + " - "
                + c.getPlageSeuils().getValeurMax() + "]");
        }

        if (c.getReleves().isEmpty()) {
            System.out.println("    Aucun releve.");
        } else {
            int i = 1;
            for (Releve r : c.getReleves()) {
                if (r instanceof ReleveGPS) {
                    ReleveGPS rg = (ReleveGPS) r;
                    System.out.println("    " + i++
                        + " | GPS(" + rg.getLatitude()
                        + ", " + rg.getLongitude() + ")"
                        +  " | " + r.getNiveauReleve());
                } else {
                    ReleveNumerique rn = (ReleveNumerique) r;
                    System.out.println("    " + i++
                        + " | " + rn.getValeur()
                        + " " + rn.getUnite()
                        + " | " + r.getNiveauReleve());
                }
            }
        }
    }
}
    public void consulterHistoriqueReleves(Capteur capteur,
                                        Date dateDebut,
                                        Date dateFin) {

    System.out.println("=== Historique : " + capteur.getCode()
                     + " [" + capteur.getClass().getSimpleName() + "] ===");
    System.out.println("Periode : " + dateDebut + " -> " + dateFin);

    List<Releve> filtres = new ArrayList<>();
    for (Releve r : capteur.getReleves()) {
        if (!r.getDate().before(dateDebut)
         && !r.getDate().after(dateFin)) {
            filtres.add(r);
        }
    }

    if (filtres.isEmpty()) {
        System.out.println("Aucun releve dans cette periode.");
        return;
    }

    System.out.println(filtres.size() + " releve(s) trouve(s).");
    int i = 1;
    for (Releve r : filtres) {
        if (r instanceof ReleveGPS) {
            ReleveGPS rg = (ReleveGPS) r;
            System.out.println("  " + i++
                + " | " + r.getDate()
                + " | GPS(" + rg.getLatitude()
                + ", " + rg.getLongitude() + ")"
                + " | " + r.getNiveauReleve());
        } else {
            ReleveNumerique rn = (ReleveNumerique) r;
            System.out.println("  " + i++
                + " | " + r.getDate()
                + " | " + rn.getValeur()
                + " " + rn.getUnite()
                + " | " + r.getNiveauReleve());
        }
    }
}
public boolean changerStatutCapteur(Capteur capteur, StatutCapteur nouveauStatut) {

    StatutCapteur ancienStatut = capteur.getStatut();

    switch (nouveauStatut) {
        case ACTIF:
            if (ancienStatut == StatutCapteur.DEFAILLANT) {
                System.out.println("Impossible de réactiver un capteur défaillant");
                return false;
            }
            capteur.reactiver();
            break;
        case SUSPENDU:
             if (ancienStatut == StatutCapteur.DEFAILLANT) {
                System.out.println("Impossible de réactiver un capteur défaillant");
                return false;
            }
            capteur.suspendre();
            break;
        case DEFAILLANT:
            capteur.setStatut(StatutCapteur.DEFAILLANT);
            break;
    }

    System.out.println("Capteur : " + capteur.getCode()
                     + " [" + capteur.getClass().getSimpleName() + "]");
    System.out.println("Statut : " + ancienStatut + " -> " + capteur.getStatut());
    return true;
}
public void acquitterAlerte(Alerte alerte) {
    if (alerte.getStatut() == StatutAlerte.SUPPRIMEE) {
        System.out.println("Alerte deja supprimee, impossible d'acquitter.");
        return;
    }
    alerte.acquitter();
    System.out.println("Alerte acquittee : capteur=" + alerte.getCapteur().getCode()
                     + " niveau=" + alerte.getNiveauGravite());
}

public void supprimerAlerte(Alerte alerte) {
    if (alerte.getStatut() == StatutAlerte.SUPPRIMEE) {
        System.out.println("Alerte deja supprimee.");
        return;
    }
    alerte.supprimer();
    System.out.println("Alerte supprimee : capteur=" + alerte.getCapteur().getCode()
                     + " niveau=" + alerte.getNiveauGravite());
}

private List<Alerte> toutesLesAlertes() {
    List<Alerte> resultats = new ArrayList<>();
    for (Zone z : zones)
        resultats.addAll(z.getAlertes());
    return resultats;
}

public void consulterHistoriqueAlertes(Zone zone) {
    System.out.println("=== Alertes | Zone : " + zone.getNom() + " ===");
    List<Alerte> resultats = zone.getAlertes();
    afficherAlertes(resultats);
}

public void consulterHistoriqueAlertes(String typeCapteur) {
    System.out.println("=== Alertes | Capteur : " + typeCapteur + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : toutesLesAlertes())
        if (a.getCapteur().getClass().getSimpleName().equalsIgnoreCase(typeCapteur))
            resultats.add(a);
    afficherAlertes(resultats);
}

public void consulterHistoriqueAlertes(NiveauGravite niveau) {
    System.out.println("=== Alertes | Niveau : " + niveau + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : toutesLesAlertes())
        if (a.getNiveauGravite() == niveau)
            resultats.add(a);
    afficherAlertes(resultats);
}


public void consulterHistoriqueAlertes(Date dateDebut, Date dateFin) {
    System.out.println("=== Alertes | Periode : " + dateDebut + " -> " + dateFin + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : toutesLesAlertes())
        if (!a.getDate().before(dateDebut) && !a.getDate().after(dateFin))
            resultats.add(a);
    afficherAlertes(resultats);
}


public void consulterHistoriqueAlertes(Zone zone, String typeCapteur) {
    System.out.println("=== Alertes | Zone : " + zone.getNom()
                     + " | Capteur : " + typeCapteur + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : zone.getAlertes())
        if (a.getCapteur().getClass().getSimpleName().equalsIgnoreCase(typeCapteur))
            resultats.add(a);
    afficherAlertes(resultats);
}


public void consulterHistoriqueAlertes(Zone zone, NiveauGravite niveau) {
    System.out.println("=== Alertes | Zone : " + zone.getNom()
                     + " | Niveau : " + niveau + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : zone.getAlertes())
        if (a.getNiveauGravite() == niveau)
            resultats.add(a);
    afficherAlertes(resultats);
}


public void consulterHistoriqueAlertes(Zone zone, Date dateDebut, Date dateFin) {
    System.out.println("=== Alertes | Zone : " + zone.getNom()
                     + " | Periode : " + dateDebut + " -> " + dateFin + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : zone.getAlertes())
        if (!a.getDate().before(dateDebut) && !a.getDate().after(dateFin))
            resultats.add(a);
    afficherAlertes(resultats);
}


public void consulterHistoriqueAlertes(String typeCapteur, NiveauGravite niveau) {
    System.out.println("=== Alertes | Capteur : " + typeCapteur
                     + " | Niveau : " + niveau + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : toutesLesAlertes())
        if (a.getCapteur().getClass().getSimpleName().equalsIgnoreCase(typeCapteur)
         && a.getNiveauGravite() == niveau)
            resultats.add(a);
    afficherAlertes(resultats);
}


public void consulterHistoriqueAlertes(String typeCapteur, Date dateDebut, Date dateFin) {
    System.out.println("=== Alertes | Capteur : " + typeCapteur
                     + " | Periode : " + dateDebut + " -> " + dateFin + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : toutesLesAlertes())
        if (a.getCapteur().getClass().getSimpleName().equalsIgnoreCase(typeCapteur)
         && !a.getDate().before(dateDebut) && !a.getDate().after(dateFin))
            resultats.add(a);
    afficherAlertes(resultats);
}

public void consulterHistoriqueAlertes(NiveauGravite niveau, Date dateDebut, Date dateFin) {
    System.out.println("=== Alertes | Niveau : " + niveau
                     + " | Periode : " + dateDebut + " -> " + dateFin + " ===");
    List<Alerte> resultats = new ArrayList<>();
    for (Alerte a : toutesLesAlertes())
        if (a.getNiveauGravite() == niveau
         && !a.getDate().before(dateDebut) && !a.getDate().after(dateFin))
            resultats.add(a);
    afficherAlertes(resultats);
}

private void afficherAlertes(List<Alerte> alertes) {
    if (alertes.isEmpty()) {
        System.out.println("Aucune alerte trouvee.");
        return;
    }
    System.out.println(alertes.size() + " alerte(s) trouvee(s).");
    int i = 1;
    for (Alerte a : alertes) {
        System.out.println(i++ + " | " + a.getDate()
                         + " | capteur=" + a.getCapteur().getCode()
                         + " [" + a.getCapteur().getClass().getSimpleName() + "]"
                         + " | niveau=" + a.getNiveauGravite()
                         + " | statut=" + a.getStatut());
    }
}
public void afficherPanneauAlertes() {

    System.out.println("=== PANNEAU DES ALERTES ACTIVES ===");

    //collecte les alertes
    List<Alerte> alertesActives = new ArrayList<>();
    for (Zone z : zones)
        for (Alerte a : z.getAlertes())
            if (a.getStatut() == StatutAlerte.ACTIVE)
                alertesActives.add(a);

    if (alertesActives.isEmpty()) {
        System.out.println("Aucune alerte active.");
        return;
    }

    // trier
    List<Alerte> critique     = new ArrayList<>();
    List<Alerte> avertissement = new ArrayList<>();

    for (Alerte a : alertesActives) {
        if (a.getNiveauGravite() == NiveauGravite.CRITIQUE)
            critique.add(a);
        else
            avertissement.add(a);
    }

    // combiner : critique d'abord puis avertissement
    List<Alerte> alertesTrie = new ArrayList<>();
    alertesTrie.addAll(critique);
    alertesTrie.addAll(avertissement);

    // afficher
    System.out.println(alertesTrie.size() + " alerte(s) active(s).");
    int i = 1;
    for (Alerte a : alertesTrie) {
        System.out.println(i++ + " | " + a.getNiveauGravite()
                         + " | capteur=" + a.getCapteur().getCode()
                         + " [" + a.getCapteur().getClass().getSimpleName() + "]"
                         + " | zone=" + a.getCapteur().getZone().getNom()
                         + " | date=" + a.getDate());
    }
}

public void modifierZone(int code, int newCode, String newNom) {
    Zone z = chercherzone(code);
    if (z == null) {
        System.out.println("Zone introuvable.");
        return;
    }
    z.setCode(newCode);
    z.setNom(newNom);
    System.out.println("Zone modifiée : " + z.afficherVueEnsemble());
}
public void desactiverZone(int code) {
    Zone z = chercherzone(code);
    if (z == null) {
        System.out.println("Zone introuvable.");
        return;
    }
    z.suspendre(); 
    System.out.println("Zone suspendue : " + z.afficherVueEnsemble());
}
public void affficherZones(){
     if (zones.isEmpty()) {
            System.out.println("Aucune zone.");
            return;
        }
    for (Zone z : zones) {
        System.out.println(z);  
    }
  }
  public Zone chercherzone(int code){
    for(Zone z : zones){
      if(z.getCode()==code){
        return z;
      }
    }
    return null;
  }
  public Zone chercherZone(int code) {
    return chercherzone(code);
  }

    public Capteur chercherCapteur(String codeCapteur) {
        if (codeCapteur == null) {
                return null;
        }
        for (Zone zone : zones) {
                for (Capteur capteur : zone.getCapteurs()) {
                        if (codeCapteur.equals(capteur.getCode())) {
                                return capteur;
                        }
                }
        }
        return null;
    }

      public String enregistrerReleveNumerique(String codeCapteur, double valeur, String unite) {
        Capteur capteur = chercherCapteur(codeCapteur);
        if (capteur == null) {
        String message = "Capteur introuvable !";
        System.out.println(message);
        return message;
        }
        if (!(capteur instanceof CapteurNumerique) || capteur instanceof CapteurGPS) {
        String message = "Type de capteur non compatible avec un relevé numérique !";
        System.out.println(message);
        return message;
        }
        return ((CapteurNumerique) capteur).ajouterReleve(valeur, unite);
}

        public String enregistrerReleveGPS(String codeCapteur, double latitude, double longitude) {
        Capteur capteur = chercherCapteur(codeCapteur);
        if (capteur == null) {
            String message = "Capteur introuvable !";
            System.out.println(message);
            return message;
        }
        if (!(capteur instanceof CapteurGPS)) {
            String message = "Type de capteur non compatible avec un relevé GPS !";
            System.out.println(message);
            return message;
        }
        return ((CapteurGPS) capteur).ajouterReleveGPS(latitude, longitude);
}
  //Q2
  public void ajouterAnimal(int code , Animal a) {
    Zone zone = chercherzone(code);
    if (zone instanceof ZoneElevage) {
        ((ZoneElevage) zone).ajouterAnimal(a);
        System.out.println("Animal ajouté : " + a);
    } else {
        System.out.println("Pas une zone d'élevage !");
    }
}
public void ajouterCulture(int code , Culture c) {
    Zone zone = chercherzone(code);
    if (zone instanceof ZoneCulture) {
        ((ZoneCulture) zone).ajouterCulture(c);
        System.out.println("Culture ajoutée : " + c);
    } else {
        System.out.println("Pas une zone de culture !");
    }
}
// production 
public void enregistrerProduction(int codeZone, TypeProduction type, double valeur, UniteMesure unite) {
    Zone z = chercherzone(codeZone);
    if (z == null) { 
        System.out.println("Zone introuvable !"); 
        return; 
    }
    // Vérification du type de production autorisé
    if (!productionAutorisee(z, type)) {
        System.out.println("Erreur : type de production '"
            + type + "' non autorisé pour "
            + z.getClass().getSimpleName()
            + (z instanceof ZoneElevage ? 
               " (" + ((ZoneElevage)z).getTypeElevage() + ")" : "")
            + " !");
        System.out.println("Type autorisé : " + typeAutoriseMessage(z));
        return;
    }
    try {
        ProductionRecord p = new ProductionRecord(type, valeur, unite);
        z.enregistrerProduction(p);
        System.out.println("Production enregistrée : " + p);
    } catch (IllegalArgumentException e) {
        System.out.println("Erreur : " + e.getMessage());
    }
}

// Message d'aide : indique le type autorisé pour une zone
private String typeAutoriseMessage(Zone zone) {
    if (zone instanceof ZoneCulture)  return "RENDEMENT";
    if (zone instanceof ZoneAquacole) return "POIDS_RECOLTE";
    if (zone instanceof ZoneElevage) {
        return ((ZoneElevage)zone).getTypeElevage() == TypeElevage.VOLAILLE 
               ? "OEUFS" : "LAITIER";
    }
    return "inconnu";
}
public void afficherProduction(int codeZone) {
    Zone z = chercherzone(codeZone);
    if (z == null) {
        System.out.println("Zone introuvable.");
        return;
    }
    z.afficherHistoriqueProduction();
}

// part2

public void mettreAJourStade(int codeZone, String nomCulture, StadeDeCroissance stade) {
    Zone z = chercherzone(codeZone);
    if (!(z instanceof ZoneCulture)) {
        System.out.println("Pas une zone de culture !");
        return;
    }
    ZoneCulture zc = (ZoneCulture) z;
    for (Culture c : zc.getCultures()) {
        if (c.getNom().equals(nomCulture)) {
            c.mettreAJourStade(stade);
            return;
        }
    }
    System.out.println("Culture introuvable.");
}
public void rapportCultures(int codeZone) {
    Zone z = chercherzone(codeZone);
    if (!(z instanceof ZoneCulture)) {
        System.out.println("Pas une zone de culture !");
        return;
    }
    System.out.println(((ZoneCulture) z).getRapportCultures());
}

// Consigner un événement sanitaire
public void consignerEvenement(int codeZone, String numeroAnimal,
                                EvenementSanitaire e) throws IllegalArgumentException {
    Zone z = chercherzone(codeZone);
    if (!(z instanceof ZoneElevage)) {
        throw new IllegalArgumentException("Pas une zone d'élevage !");
    }
    int codeAnimal;
    try {
        codeAnimal = Integer.parseInt(numeroAnimal);
    } catch (NumberFormatException exception) {
        throw new IllegalArgumentException("Numéro d'animal invalide.");
    }
    for (Animal a : ((ZoneElevage) z).getAnimaux()) {
        if (a.getCode() == codeAnimal) {
            a.enregistrerEvenement(e);
            System.out.println("Événement consigné : " + e);
            return;
        }
    }
    throw new IllegalArgumentException("Animal introuvable.");
}

// Définir programme alimentation
public void definirProgramme(int codeZone, ProgrammeAlimentation p) throws IllegalArgumentException {
    Zone z = chercherzone(codeZone);
    if (z instanceof ZoneElevage) {
        ((ZoneElevage) z).definirProgramme(p);
    } else if (z instanceof ZoneAquacole) {
        ((ZoneAquacole) z).definirProgramme(p);
    } else {
        throw new IllegalArgumentException("Pas une zone d'élevage ou aquacole !");
    }
}

public void definirProgramme(int codeZone, String nomEspece, ProgrammeAlimentation p) throws IllegalArgumentException {
    Zone z = chercherzone(codeZone);
    if (!(z instanceof ZoneAquacole)) {
        throw new IllegalArgumentException("Pas une zone aquacole !");
    }
    ((ZoneAquacole) z).definirProgramme(nomEspece, p);
}

// Afficher programme
public void afficherProgramme(int codeZone) {
    Zone z = chercherzone(codeZone);
    if (z instanceof ZoneElevage) {
        ((ZoneElevage) z).afficherProgramme();
    } else if (z instanceof ZoneAquacole) {
        ((ZoneAquacole) z).afficherProgramme();
    }
}

public void afficherProgramme(int codeZone, String nomEspece) {
    Zone z = chercherzone(codeZone);
    if (!(z instanceof ZoneAquacole)) {
        System.out.println("Pas une zone aquacole !");
        return;
    }
    EspeceAquacole espece = ((ZoneAquacole) z).trouverEspece(nomEspece);
    if (espece == null) {
        System.out.println("Espèce introuvable.");
        return;
    }
    if (espece.getProgramme() == null) {
        System.out.println("Aucun programme défini pour cette espèce.");
        return;
    }
    System.out.println("Programme espèce : " + espece.getNom() + " -> " + espece.getProgramme().getDetails());
}

private UniteMesure determinerUniteProduction(TypeProduction type) {
    switch (type) {
        case LAITIER:
        case RENDEMENT:
        case POIDS_RECOLTE:
            return UniteMesure.KG;
        case OEUFS:
            return UniteMesure.UNITE;
        default:
            return UniteMesure.KG;
    }
}

private boolean productionAutorisee(Zone zone, TypeProduction type) {
    if (zone instanceof ZoneCulture) {
        return type == TypeProduction.RENDEMENT;
    }
    if (zone instanceof ZoneAquacole) {
        return type == TypeProduction.POIDS_RECOLTE;
    }
    if (zone instanceof ZoneElevage) {
        ZoneElevage elevage = (ZoneElevage) zone;
        if (elevage.getTypeElevage() == TypeElevage.VOLAILLE) {
            return type == TypeProduction.OEUFS;
        }
        return type == TypeProduction.LAITIER;
    }
    return false;
}
}



