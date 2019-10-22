package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class Spraak {

    public static final Spraak NORSK_BOKMAAL = new Spraak("nb");
    public static final Spraak NORSK_NYNORSK = new Spraak("nn");

    @JsonValue
    private final String kode;

    private Spraak(final String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spraak spraak = (Spraak) o;
        return Objects.equals(kode, spraak.kode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kode);
    }
}
