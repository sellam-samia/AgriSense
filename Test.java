import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) {

        // ══════════════════════════════════════════
        // 1. INITIALISATION — ZONES
        // ══════════════════════════════════════════
        System.out.println("=== 1. INITIALISATION DES ZONES ===");

        Gestionnaire g = new Gestionnaire();

        ZoneCulture  zc = new ZoneCulture(1,  "Zone Ble");
        ZoneElevage  ze = new ZoneElevage(2,  "Zone Vaches");
        ZoneAquacole za = new ZoneAquacole(3, "Zone Poissons");

        g.ajouterZone(zc);
        g.ajouterZone(ze);
        g.ajouterZone(za);

        System.out.println(">>> Test code deja existant :");
        g.ajouterZone(new ZoneCulture(1, "Doublon"));

        g.affficherZones();

        System.out.println("\n=== MODIFIER ZONE ===");
        g.modifierZone(1, 1, "Zone Mais");
        g.affficherZones();

        System.out.println("\n=== SUSPENDRE / REACTIVER ===");
        g.desactiverZone(1);
        System.out.println("Statut : " + zc.getStatut());
        zc.activer();
        System.out.println("Statut : " + zc.getStatut());

        System.out.println("\n=== PRODUCTION PAR ZONE ===");
        System.out.println("\n=== TEST PRODUCTION PAR ZONE ===");

        // ✅ ZoneCulture → RENDEMENT seulement
        System.out.println(">>> ZoneCulture — RENDEMENT (autorisé)");
        g.enregistrerProduction(1, TypeProduction.RENDEMENT, 1250.0, UniteMesure.KG);

        System.out.println(">>> ZoneCulture — LAITIER (interdit)");
        g.enregistrerProduction(1, TypeProduction.LAITIER, 100.0, UniteMesure.LITRE); // → Erreur

        // ✅ ZoneElevage RUMINANT → LAITIER seulement
        ZoneElevage zeRuminant = new ZoneElevage(2, "Zone Vaches", TypeElevage.RUMINANT);
        g.ajouterZone(zeRuminant);
        System.out.println(">>> ZoneElevage RUMINANT — LAITIER (autorisé)");
        g.enregistrerProduction(2, TypeProduction.LAITIER, 320.5, UniteMesure.LITRE);

        System.out.println(">>> ZoneElevage RUMINANT — OEUFS (interdit)");
        g.enregistrerProduction(2, TypeProduction.OEUFS, 100.0, UniteMesure.UNITE); // → Erreur

        // ✅ ZoneElevage VOLAILLE → OEUFS seulement
        ZoneElevage zeVolaille = new ZoneElevage(5, "Zone Poulets", TypeElevage.VOLAILLE);
        g.ajouterZone(zeVolaille);
        System.out.println(">>> ZoneElevage VOLAILLE — OEUFS (autorisé)");
        g.enregistrerProduction(5, TypeProduction.OEUFS, 500.0, UniteMesure.UNITE);

        System.out.println(">>> ZoneElevage VOLAILLE — LAITIER (interdit)");
        g.enregistrerProduction(5, TypeProduction.LAITIER, 100.0, UniteMesure.LITRE); // → Erreur

        // ✅ ZoneAquacole → POIDS_RECOLTE seulement
        System.out.println(">>> ZoneAquacole — POIDS_RECOLTE (autorisé)");
        g.enregistrerProduction(3, TypeProduction.POIDS_RECOLTE, 780.0, UniteMesure.KG);

        System.out.println(">>> ZoneAquacole — RENDEMENT (interdit)");
        g.enregistrerProduction(3, TypeProduction.RENDEMENT, 100.0, UniteMesure.UNITE); // → Erreur

        g.afficherProduction(1);
        g.afficherProduction(2);
        g.afficherProduction(3);

        // ══════════════════════════════════════════
        // 2. CULTURES
        // ══════════════════════════════════════════
        System.out.println("\n=== 2. GESTION DES CULTURES ===");

        try { new Culture("", FamilleCulture.CEREALE,
            LocalDate.of(2026,3,1), LocalDate.of(2026,8,15),
            new ExigencesPedologiques(5.5,8.0,30.0,70.0));
        } catch (IllegalArgumentException e) { System.out.println("Erreur nom vide : " + e.getMessage()); }

        try { new Culture("Ble", FamilleCulture.CEREALE,
            LocalDate.of(2026,8,1), LocalDate.of(2026,3,1),
            new ExigencesPedologiques(5.5,8.0,30.0,70.0));
        } catch (IllegalArgumentException e) { System.out.println("Erreur date : " + e.getMessage()); }

        try { new ExigencesPedologiques(-1, 15, 30, 70);
        } catch (IllegalArgumentException e) { System.out.println("Erreur pH : " + e.getMessage()); }

        ExigencesPedologiques exigences = new ExigencesPedologiques(5.5,8.0,30.0,70.0);
        Culture ble = new Culture("Ble", FamilleCulture.CEREALE,
            LocalDate.of(2026,3,1), LocalDate.of(2026,8,15), exigences);
        g.ajouterCulture(1, ble);
        System.out.println("Stade initial : " + ble.getStadeCroissance());

        g.mettreAJourStade(1, "Ble", StadeDeCroissance.GERMINATION);
        g.mettreAJourStade(1, "Ble", StadeDeCroissance.CROISSANCE);
        g.mettreAJourStade(1, "Ble", StadeDeCroissance.SEMIS);     // erreur
        g.mettreAJourStade(1, "Ble", StadeDeCroissance.CROISSANCE); // erreur
        System.out.println("Stade actuel : " + ble.getStadeCroissance());
        g.rapportCultures(1);

        g.desactiverZone(1);
        g.ajouterCulture(1, new Culture("Mais", FamilleCulture.CEREALE,
            LocalDate.of(2026,4,1), LocalDate.of(2026,9,1), exigences)); // erreur
        zc.activer();

        // ══════════════════════════════════════════
        // 3. ANIMAUX
        // ══════════════════════════════════════════
        System.out.println("\n=== 3. GESTION DES ANIMAUX ===");

        try { new Animal(9999, "Vache", -1, 400, EtatDesante.SAIN); }
        catch (IllegalArgumentException e) { System.out.println("Erreur age : " + e.getMessage()); }

        try { new Animal(9999, "Vache", 3, -50, EtatDesante.SAIN); }
        catch (IllegalArgumentException e) { System.out.println("Erreur poids : " + e.getMessage()); }

        try { new Animal(9999, "   ", 3, 400, EtatDesante.SAIN); }
        catch (IllegalArgumentException e) { System.out.println("Erreur espece : " + e.getMessage()); }

        Animal vache  = new Animal(1001, "Vache",  4, 450, EtatDesante.SAIN);
        Animal mouton = new Animal(1002, "Mouton", 2,  80, EtatDesante.SAIN);
        g.ajouterAnimal(2, vache);
        g.ajouterAnimal(2, mouton);
        g.ajouterAnimal(2, new Animal(1001, "Chevre", 3, 60, EtatDesante.SAIN)); // doublon
        g.ajouterAnimal(1, vache); // pas zone elevage

        g.consignerEvenement(2, "1001", new EvenementSanitaire(
            LocalDate.now(), TypeEvenementSanitaire.MALADIE, "Fievre legere"));
        g.consignerEvenement(2, "1001", new EvenementSanitaire(
            LocalDate.now(), TypeEvenementSanitaire.EVOLUTION_POIDS,
            "Controle mensuel", 450.0, 465.0));
        System.out.println("Nouveau poids vache : " + vache.getPoids() + "kg");

        try { new ProgrammeAlimentation("Herbe", -3.5); }
        catch (IllegalArgumentException e) { System.out.println("Erreur qte : " + e.getMessage()); }

        g.definirProgramme(2, new ProgrammeAlimentation("Herbe et concentre", 3.5));
        g.afficherProgramme(2);

        // ══════════════════════════════════════════
        // ZONE AQUACOLE
        // ══════════════════════════════════════════
        System.out.println("\n=== ZONE AQUACOLE ===");

        try { new EspeceAquacole("Poisson", 0); }
        catch (IllegalArgumentException e) { System.out.println("Erreur 0 individus : " + e.getMessage()); }

        try { new EspeceAquacole("  ", 100); }
        catch (IllegalArgumentException e) { System.out.println("Erreur nom vide : " + e.getMessage()); }

        za.ajouterEspece(new EspeceAquacole("Poisson",  150));
        za.ajouterEspece(new EspeceAquacole("Crevette", 300));
        System.out.println("Total individus : " + za.getNombreTotalIndividus());
        za.definirProgramme(new ProgrammeAlimentation("Granules marins", 2.0));
        za.afficherProgramme();

        // ══════════════════════════════════════════
        // 4. CAPTEURS
        // ══════════════════════════════════════════
        System.out.println("\n=== 4. AJOUT ET CONFIGURATION CAPTEURS ===");

        try { new PlageSeuils(10.0, 5.0); }
        catch (IllegalArgumentException e) { System.out.println("Erreur seuils : " + e.getMessage()); }

        g.ajouterEtConfigurerCapteur("SOL",             zc, 5.5,  8.0);
        g.ajouterEtConfigurerCapteur("ENVIRONNEMENTAL", zc, 10.0, 40.0);
        g.ajouterEtConfigurerCapteur("GPS",             ze, 0.0,  0.0); // min==max OK maintenant
        g.ajouterEtConfigurerCapteur("BIOMETRIQUE",     ze, 36.0, 42.0);
        g.ajouterEtConfigurerCapteur("EAU",             za, 6.0,  12.0);
        g.ajouterEtConfigurerCapteur("INCONNU",         zc, 0.0,  100.0);

        System.out.println("-- Capteurs Zone Culture --");
        for (Capteur c : zc.getCapteurs()) System.out.println(c);
        System.out.println("-- Capteurs Zone Elevage --");
        for (Capteur c : ze.getCapteurs()) System.out.println(c);
        System.out.println("-- Capteurs Zone Aquacole --");
        for (Capteur c : za.getCapteurs()) System.out.println(c);

        // ══════════════════════════════════════════
        // 5. STATUT CAPTEUR
        // ══════════════════════════════════════════
        System.out.println("\n=== 5. CHANGER STATUT CAPTEUR ===");

        Capteur cap1 = zc.getCapteurs().get(0); // CapteurSol
        Capteur cap2 = zc.getCapteurs().get(1); // CapteurEnvironnemental
        Capteur cap3 = ze.getCapteurs().get(0); // CapteurGPS
        Capteur cap4 = ze.getCapteurs().get(1); // CapteurBiometrique
        Capteur cap5 = za.getCapteurs().get(0); // CapteurEau

        g.changerStatutCapteur(cap1, StatutCapteur.SUSPENDU);
        g.changerStatutCapteur(cap1, StatutCapteur.ACTIF);
        g.changerStatutCapteur(cap2, StatutCapteur.DEFAILLANT);
        g.changerStatutCapteur(cap2, StatutCapteur.ACTIF);

        // ══════════════════════════════════════════
        // 6. RELEVES + ALERTES
        // ══════════════════════════════════════════
        System.out.println("\n=== 6. RELEVES + ALERTES AUTOMATIQUES ===");

        ze.setLimites(new LimitesGeographiques(36.0, 36.8, 2.5, 3.2));

        ((CapteurSol)cap1).ajouterRelevePH(6.5);
        ((CapteurSol)cap1).ajouterRelevePH(7.8);
        ((CapteurSol)cap1).ajouterRelevePH(9.2);

        ((CapteurEnvironnemental)cap2).ajouterReleveTemperature(25.0);
        ((CapteurEnvironnemental)cap2).ajouterReleveTemperature(38.5);
        ((CapteurEnvironnemental)cap2).ajouterReleveTemperature(55.0);

        ((CapteurGPS)cap3).ajouterReleveGPS(36.7372, 3.0865);
        ((CapteurGPS)cap3).ajouterReleveGPS(36.9000, 3.5000);

        ((CapteurBiometrique)cap4).ajouterReleveTemperature(38.5);
        ((CapteurBiometrique)cap4).ajouterReleveTemperature(41.8);
        ((CapteurBiometrique)cap4).ajouterReleveTemperature(43.0);

        ((CapteurEau)cap5).ajouterReleveOxygene(9.0);
        ((CapteurEau)cap5).ajouterReleveOxygene(11.5);
        ((CapteurEau)cap5).ajouterReleveOxygene(3.0);

        // ══════════════════════════════════════════
        // 7. TABLEAU DE BORD
        // ══════════════════════════════════════════
        System.out.println("\n=== 7. TABLEAU DE BORD DES RELEVES ===");
        g.afficherTableauDeBordReleves(zc);
        g.afficherTableauDeBordReleves(ze);
        g.afficherTableauDeBordReleves(za);

        // ══════════════════════════════════════════
        // 8. HISTORIQUE DATES
        // ══════════════════════════════════════════
        System.out.println("\n=== 8. HISTORIQUE RELEVES FILTRE DATES ===");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        Date dateDebut = cal.getTime();
        Date dateFin   = new Date();
        g.consulterHistoriqueReleves(cap1, dateDebut, dateFin);
        g.consulterHistoriqueReleves(cap4, dateDebut, dateFin);

        // ══════════════════════════════════════════
        // 9. ALERTES
        // ══════════════════════════════════════════
        System.out.println("\n=== 9. ALERTES DECLENCHEES ===");
        System.out.println("-- Alertes Zone Culture --");
        for (Alerte a : zc.getAlertes()) System.out.println(a);
        System.out.println("-- Alertes Zone Elevage --");
        for (Alerte a : ze.getAlertes()) System.out.println(a);
        System.out.println("-- Alertes Zone Aquacole --");
        for (Alerte a : za.getAlertes()) System.out.println(a);

        // ══════════════════════════════════════════
        // 10. PANNEAU ALERTES
        // ══════════════════════════════════════════
        System.out.println("\n=== 10. PANNEAU ALERTES ACTIVES ===");
        g.afficherPanneauAlertes();

        // ══════════════════════════════════════════
        // 11. ACQUITTER / SUPPRIMER
        // ══════════════════════════════════════════
        System.out.println("\n=== 11. ACQUITTER / SUPPRIMER ===");
        System.out.println("Avant :");
        for (Alerte a : zc.getAlertes()) System.out.println(a);
        g.acquitterAlerte(zc.getAlertes().get(0));
        g.supprimerAlerte(zc.getAlertes().get(1));
        System.out.println("Apres :");
        for (Alerte a : zc.getAlertes()) System.out.println(a);

        // ══════════════════════════════════════════
        // 12. HISTORIQUE ALERTES FILTRE
        // ══════════════════════════════════════════
        System.out.println("\n=== 12. HISTORIQUE ALERTES FILTRE ===");
        g.consulterHistoriqueAlertes(zc);
        g.consulterHistoriqueAlertes("CapteurSol");
        g.consulterHistoriqueAlertes(NiveauGravite.CRITIQUE);
        g.consulterHistoriqueAlertes(dateDebut, dateFin);
        g.consulterHistoriqueAlertes(zc, NiveauGravite.CRITIQUE);
        g.consulterHistoriqueAlertes("CapteurSol", NiveauGravite.CRITIQUE);
        g.consulterHistoriqueAlertes(NiveauGravite.CRITIQUE, dateDebut, dateFin);

        System.out.println("\n=== VUE FINALE ===");
        g.affficherZones();
    }
}