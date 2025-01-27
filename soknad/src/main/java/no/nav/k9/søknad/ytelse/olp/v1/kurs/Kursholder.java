package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Kursholder {

    @JsonProperty(value = "navn")
    @Valid
    @Size(max = 100)
    @Pattern(regexp = "^[\\p{Pd}\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}§]*$", message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String navn;

    @JsonProperty(value = "institusjonsidentifikator", required = true)
    @Valid
    private UUID institusjonUuid;

    public Kursholder() {
    }

    public Kursholder(UUID institusjonUuid) {
        this.institusjonUuid = institusjonUuid;
    }

    @Deprecated(forRemoval = true)
    public Kursholder(String navn, UUID institusjonUuid) {
        this.navn = navn;
        this.institusjonUuid = institusjonUuid;
    }

    @Deprecated(forRemoval = true)
    public String getNavn() {
        return navn;
    }

    public UUID getInstitusjonUuid() {
        return institusjonUuid;
    }
}
