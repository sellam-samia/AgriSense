public enum UniteMesure {

    // ── unités de quantité (déjà existantes) ──
    KG("kg"),
    LITRE("L"),
    TONNE("t"),
    MILLILITRE("mL"),
    GRAMME("g"),
    UNITE("unité"),

    // ── unités des capteurs ──
    CELSIUS("°C"),
    POURCENTAGE("%"),
    MILLIMETRE("mm"),
    PH("pH"),
    MILLIGRAMME_PAR_LITRE("mg/L"),
    DEGRE("°"),
    BATTEMENT_PAR_MINUTE("bpm");

    private final String symbole;

    UniteMesure(String symbole) {
        this.symbole = symbole;
    }

    public String getSymbole() { return symbole; }
}