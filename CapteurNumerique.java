public abstract class CapteurNumerique extends Capteur {

    public CapteurNumerique() { super(); }

    // methode principale avec UniteMesure
    public String ajouterReleve(double valeur, UniteMesure unite) {
        if (this.getStatut() != StatutCapteur.ACTIF) {
            String message = "Capteur " + getCode() + " non actif, relevé ignoré.";
            System.out.println(message);
            return message;
        }
        ReleveNumerique r = new ReleveNumerique(valeur, unite, this.getPlageSeuils());
        getReleves().add(r);

        if (r.getNiveauReleve() != NiveauReleve.NORMAL) {
            NiveauGravite niveau = r.getNiveauReleve() == NiveauReleve.CRITIQUE ?
                NiveauGravite.CRITIQUE : NiveauGravite.AVERTISSEMENT;
            Alerte alerte = new Alerte(r, this, niveau);
            getZone().ajouterAlerte(alerte);
            System.out.println("ALERTE : capteur=" + getCode()
                             + " valeur=" + valeur
                             + " " + unite.getSymbole()
                             + " niveau=" + niveau);
        }
        return "Relevé ajouté pour le capteur " + getCode() + ".";
    }

    // compatibilite String — convertit en UniteMesure
    public String ajouterReleve(double valeur, String uniteStr) {
        UniteMesure unite = UniteMesure.UNITE; // defaut
        for (UniteMesure u : UniteMesure.values()) {
            if (u.getSymbole().equals(uniteStr) || u.name().equals(uniteStr)) {
                unite = u;
                break;
            }
        }
        return ajouterReleve(valeur, unite);
    }
}