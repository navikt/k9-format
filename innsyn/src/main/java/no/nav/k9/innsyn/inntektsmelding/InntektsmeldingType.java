package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public enum InntektsmeldingType {
    ORDINÆR("ORDINÆR", "Ordinær"),
    OMSORGSPENGER_REFUSJON("OMSORGSPENGER_REFUSJON", "Omsorgspenger refusjon"),
    ARBEIDSGIVERINITIERT_NYANSATT("ARBEIDSGIVERINITIERT_NYANSATT", "Arbeidsgiverinitiert nyansatt"),
    ARBEIDSGIVERINITIERT_UREGISTRERT("ARBEIDSGIVERINITIERT_UREGISTRERT", "Arbeidsgiverinitiert uregistrert");

    private static final Map<String, InntektsmeldingType> KODER = new LinkedHashMap<>();

    static {
        for (var v : values()) {
            if (KODER.putIfAbsent(v.kode, v) != null) {
                throw new IllegalArgumentException("Duplikat : " + v.kode);
            }
        }
    }

    @JsonValue
    private final String kode;

    @JsonIgnore
    private final String navn;

    InntektsmeldingType(String kode, String navn) {
        this.kode = kode;
        this.navn = navn;
    }

    public static InntektsmeldingType fraKode(String kode) {
        if (kode == null) {
            return null;
        }
        return Optional.ofNullable(KODER.get(kode)).orElseThrow(() -> new IllegalArgumentException("Ukjent InntektsmeldingType: " + kode));
    }

    public String getKode() {
        return kode;
    }

    public String getNavn() {
        return navn;
    }
}
