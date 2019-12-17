package no.nav.k9.soknad.felles;

public class Soker {

    private NorskIdentitetsnummer norskIdentitetsnummer;
    private Spraak spraakValg;

    public Soker() {

    }


    public void setNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
    }

    public NorskIdentitetsnummer getNorskIdentitetsnummer() {
        return norskIdentitetsnummer;
    }

    public void setSpraakValg(Spraak spraakValg) {
        this.spraakValg = spraakValg;
    }

    public Spraak getSpraakValg() {
        return spraakValg;
    }
}
