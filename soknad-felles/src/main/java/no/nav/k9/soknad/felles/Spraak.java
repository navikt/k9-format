package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Spraak {

    private static final Map<String, Spraak> values = new HashMap<>();

    public static final Spraak NORSK_BOKMAAL = new Spraak("nb");
    public static final Spraak NORSK_NYNORSK = new Spraak("nn");

    static {
        values.put(NORSK_BOKMAAL.getKode(), NORSK_BOKMAAL);
        values.put(NORSK_NYNORSK.getKode(), NORSK_NYNORSK);
    }


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

    @JsonCreator
    public static Spraak from(String spraakKode) {
        if (spraakKode == null) {
            return null;
        }

        final Spraak spraak = values.get(spraakKode);
        if (spraak == null) {
            throw new IllegalArgumentException("spraakKode '" + spraakKode + "' er ikke støttet.");
        }
        return spraak;
    }
}
