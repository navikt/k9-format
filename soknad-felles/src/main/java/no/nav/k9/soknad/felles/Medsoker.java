package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Medsoker {

    private NorskIdentitetsnummer norskIdentitetsnummer;


    public Medsoker(NorskIdentitetsnummer norskIdentitetsnummer) {
        setNorskIdentitetsnummer(norskIdentitetsnummer);
    }

    private Medsoker() {

    }


    public void setNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        if (norskIdentitetsnummer == null) {
            throw new IllegalArgumentException("norskIdentitetsnummer == null");
        }
        this.norskIdentitetsnummer = norskIdentitetsnummer;
    }

    public NorskIdentitetsnummer getNorskIdentitetsnummer() {
        return norskIdentitetsnummer;
    }
}
