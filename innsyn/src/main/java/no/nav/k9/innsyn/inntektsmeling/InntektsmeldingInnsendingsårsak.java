package no.nav.k9.innsyn.inntektsmeling;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonFormat(shape = Shape.OBJECT)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
public enum InntektsmeldingInnsendingsårsak {

    NY("NY", "NY"),
    ENDRING("ENDRING", "ENDRING"),
    UDEFINERT("-", "UDEFINERT"),
    ;

    private static final Map<String, InntektsmeldingInnsendingsårsak> KODER = new LinkedHashMap<>();

    static {
        for (var v : values()) {
            if (KODER.putIfAbsent(v.kode, v) != null) {
                throw new IllegalArgumentException("Duplikat : " + v.kode);
            }
        }
    }

    @JsonIgnore
    private String navn;

    @JsonValue
    private String kode;

    InntektsmeldingInnsendingsårsak(String kode, String navn) {
        this.kode = kode;
        this.navn = navn;
    }

    @JsonCreator
    public static InntektsmeldingInnsendingsårsak fraKode(String kode) {
        if (kode == null) {
            return null;
        }
        var ad = KODER.get(kode);
        if (ad == null) {
            throw new IllegalArgumentException("Ukjent InntektsmeldingInnsendingsårsak: " + kode);
        }
        return ad;
    }

    public String getNavn() {
        return navn;
    }

    public String getKode() {
        return kode;
    }
}
