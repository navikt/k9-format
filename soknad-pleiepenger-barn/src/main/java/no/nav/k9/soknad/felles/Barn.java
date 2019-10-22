package no.nav.k9.soknad.felles;

public class Barn {

    private NorskIdentitetsnummer norskIdentitetsnummer;
    private Navn navn;

    public Barn() {

    }


    public void setNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
    }

    public NorskIdentitetsnummer getNorskIdentitetsnummer() {
        return norskIdentitetsnummer;
    }

    public void setNavn(Navn navn) {
        this.navn = navn;
    }

    public Navn getNavn() {
        return navn;
    }
}
