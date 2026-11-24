public enum StadeDeCroissance {
    SEMIS(0), GERMINATION(1), CROISSANCE(2), MATURITE(3), RECOLTE(4);

    private final int ordre;

    StadeDeCroissance(int ordre) { this.ordre = ordre; }

    public int getOrdre() { return ordre; }

    public boolean estSuperieurA(StadeDeCroissance autre) {
        return this.ordre > autre.ordre;
    }
}