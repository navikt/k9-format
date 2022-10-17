package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Kurs {

    @JsonProperty(value = "holder", required = true)
    @NotNull
    @Valid
    private String holder;

    @JsonProperty(value = "formål", required = true)
    @NotNull
    @Valid
    private String formål;

    @JsonProperty(value = "kursperioder", required = true)
    @NotNull
    @Size(min = 1)
    @Valid
    private List<KursPeriodeMedReisetid> kursperioder;

    @JsonProperty(value = "institusjonUuid")
    @Valid
    private UUID institusjonUuid;

    public Kurs() {
    }

    public Kurs(String kursholder, String formålMedKurset, List<KursPeriodeMedReisetid> kursperioder, UUID institusjonUuid) {
        this.holder = kursholder;
        this.formål = formålMedKurset;
        this.kursperioder = kursperioder;
        this.institusjonUuid = institusjonUuid;
    }

    public String getHolder() {
        return holder;
    }

    public String getFormål() {
        return formål;
    }

    public List<KursPeriodeMedReisetid> getKursperioder() {
        return new ArrayList<>(kursperioder);
    }

    public UUID getInstitusjonUuid() {
        return institusjonUuid;
    }
}
