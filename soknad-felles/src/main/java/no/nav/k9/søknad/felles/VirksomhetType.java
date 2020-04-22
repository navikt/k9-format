package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
public enum VirksomhetType {

    DAGMAMMA("DAGMAMMA", "Dagmamma i eget hjem/familiebarnehage"),
    FISKE("FISKE", "Fiske"),
    JORDBRUK_SKOGBRUK("JORDBRUK_SKOGBRUK", "Jordbruk"),
    ANNEN("ANNEN", "Annen næringsvirksomhet"),
    UDEFINERT("-", "Ikke definert"),
    ;

    private static final Map<String, VirksomhetType> KODER = new LinkedHashMap<>();

    public static final String KODEVERK = "VIRKSOMHET_TYPE";

    static {
        for (var v : values()) {
            if (KODER.putIfAbsent(v.kode, v) != null) {
                throw new IllegalArgumentException("Duplikat : " + v.kode);
            }
        }
    }

    @JsonIgnore
    private String navn;

    @JsonProperty(value = "kode")
    @JsonValue
    private String kode;

    VirksomhetType(String kode, String navn) {
        this.kode = kode;
        this.navn = navn;
    }

    @JsonCreator
    public static VirksomhetType fraKode(@JsonProperty("kode") String kode) {
        if (kode == null) {
            return null;
        }
        var ad = KODER.get(kode);
        if (ad == null) {
            throw new IllegalArgumentException("Ukjent VirksomhetType: " + kode);
        }
        return ad;
    }

    public static Map<String, VirksomhetType> kodeMap() {
        return Collections.unmodifiableMap(KODER);
    }

    public String getNavn() {
        return navn;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getKodeverk() {
        return KODEVERK;
    }

    public String getKode() {
        return kode;
    }

}
