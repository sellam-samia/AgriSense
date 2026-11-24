import java.util.Date;

public abstract class Releve {
    protected Date date;
    protected NiveauReleve niveauReleve;

    public Releve() {
        this.date = new Date();
    }

    public Date getDate()                   { return date; }
    public NiveauReleve getNiveauReleve()   { return niveauReleve; }
}