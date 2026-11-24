import java.util.Date;

public class Alerte {
    private Date date;
    private Releve releveDeclenche;
    private Capteur capteur;
    private NiveauGravite niveauGravite;
    private StatutAlerte statut;

    public Alerte(Releve releveDeclenche, Capteur capteur, NiveauGravite niveauGravite) {
        this.date             = new Date();
        this.releveDeclenche  = releveDeclenche;
        this.capteur          = capteur;
        this.niveauGravite    = niveauGravite;
        this.statut           = StatutAlerte.ACTIVE;
    }

    public void acquitter()  { this.statut = StatutAlerte.ACQUITTEE; }
    public void supprimer()  { this.statut = StatutAlerte.SUPPRIMEE; }

    public Date getDate()                  { return date; }
    public Releve getReleveDeclenche()     { return releveDeclenche; }
    public Capteur getCapteur()            { return capteur; }
    public NiveauGravite getNiveauGravite(){ return niveauGravite; }
    public StatutAlerte getStatut()        { return statut; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alerte[")
          .append("date=").append(date)
          .append(" capteur=").append(capteur.getCode())
          .append(" niveau=").append(niveauGravite)
          .append(" statut=").append(statut)
          .append("]");
        return sb.toString();
    }
}