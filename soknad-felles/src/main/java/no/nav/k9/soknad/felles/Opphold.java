package no.nav.k9.soknad.felles;

public class Opphold {

    private Periode periode;
    private Landkode land;


    public Opphold() {

    }

    public Opphold(Periode periode, Landkode land) {
        this.periode = periode;
        this.land = land;
    }


    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public Periode getPeriode() {
        return periode;
    }

    public void setLand(Landkode land) {
        this.land = land;
    }

    public Landkode getLand() {
        return land;
    }
}
