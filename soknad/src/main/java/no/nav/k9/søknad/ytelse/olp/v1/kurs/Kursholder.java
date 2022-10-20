package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Kursholder {

    @JsonProperty(value = "holder", required = true)
    @Valid
    @Size(max = 100)
    @Pattern(regexp = "^[\\p{Pd}\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}§]*$", message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String holder;

    @JsonProperty(value = "institusjonUuid", required = true)
    @Valid
    private UUID institusjonUuid;

    public Kursholder() {
    }

    public Kursholder(String holder, UUID institusjonUuid) {
        this.holder = holder;
        this.institusjonUuid = institusjonUuid;
    }

    public String getHolder() {
        return holder;
    }

    public UUID getInstitusjonUuid() {
        return institusjonUuid;
    }
}
