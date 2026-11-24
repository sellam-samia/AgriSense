import java.util.ArrayList;
import java.util.List;

public abstract class Capteur {
    private static int compteur = 0;
    private String code;
    private StatutCapteur statut;
    private PlageSeuils plageSeuils;
    private Zone zone;
    private List<Releve> releves = new ArrayList<>();

    public Capteur() {
        compteur++;
        this.code   = "CAP-" + compteur;
        this.statut = StatutCapteur.ACTIF;
    }

    public String getCode()             { return code; }
    public StatutCapteur getStatut()    { return statut; }
    public PlageSeuils getPlageSeuils() { return plageSeuils; }
    public Zone getZone()               { return zone; }
    public List<Releve> getReleves()    { return releves; }

    public void setPlageSeuils(PlageSeuils p) { this.plageSeuils = p; }
    public void setStatut(StatutCapteur s)    { this.statut = s; }
    public void setZone(Zone z)               { this.zone = z; }

    public void suspendre() { this.statut = StatutCapteur.SUSPENDU; }
    public void reactiver() { this.statut = StatutCapteur.ACTIF; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Capteur[").append(code).append("] type=")
          .append(getClass().getSimpleName())
          .append(" statut=").append(statut)
          .append(" seuils=[");
        if (plageSeuils != null) {
            sb.append(plageSeuils.getValeurMin())
              .append(", ")
              .append(plageSeuils.getValeurMax());
        } else {
            sb.append("aucun");
        }
        sb.append("] zone=")
          .append(zone != null ? zone.getCode() : "aucune");
        return sb.toString();
    }
}