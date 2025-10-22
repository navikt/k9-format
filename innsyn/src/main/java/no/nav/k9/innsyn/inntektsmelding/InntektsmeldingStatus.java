package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;


public enum InntektsmeldingStatus {
    I_BRUK("I_BRUK"),
    ERSTATTET_AV_NYERE("ERSTATTET_AV_NYERE"),
    IKKE_RELEVANT("IKKE_RELEVANT"),
    MANGLER_DATO("MANGLER_DATO");

    private static final Map<String, InntektsmeldingStatus> KODER = new LinkedHashMap<>();

    static {
        for (var v : values()) {
            if (KODER.putIfAbsent(v.kode, v) != null) {
                throw new IllegalArgumentException("Duplikat : " + v.kode);
            }
        }
    }

    @JsonValue
    private final String kode;

    InntektsmeldingStatus(String kode) {
        this.kode = kode;
    }

    @JsonCreator
    public static InntektsmeldingStatus fraKode(String kode) {
        return KODER.get(kode);
    }

}
