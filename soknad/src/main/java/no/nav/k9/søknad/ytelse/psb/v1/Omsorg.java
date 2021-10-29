package no.nav.k9.søknad.ytelse.psb.v1;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Omsorg {

    @JsonProperty(value = "relasjonTilBarnet", required = false)
    @Valid
    private BarnRelasjon relasjonTilBarnet;

    @JsonProperty(value = "beskrivelseAvOmsorgsrollen", required = false)
    @Valid
    private String beskrivelseAvOmsorgsrollen;

    public Omsorg() {
    }

    public Optional<BarnRelasjon> getRelasjonTilBarnet() {
        return Optional.ofNullable(relasjonTilBarnet);
    }

    public Omsorg medRelasjonTilBarnet(BarnRelasjon relasjonTilBarnet) {
        this.relasjonTilBarnet = relasjonTilBarnet;
        return this;
    }

    public Optional<String> getBeskrivelseAvOmsorgsrollen() {
        return Optional.ofNullable(beskrivelseAvOmsorgsrollen);
    }

    public Omsorg medBeskrivelseAvOmsorgsrollen(String beskrivelseAvOmsorgsrollen) {
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
        return this;
    }

    @AssertTrue(message = "[påkrevd] Kan ikke være tom når relasjon er Annet")
    private boolean isNotEmptyBeskrivesleWhenAnnet() {
        if (relasjonTilBarnet == null || relasjonTilBarnet != BarnRelasjon.ANNET) {
            return true;
        }
        return beskrivelseAvOmsorgsrollen != null && !beskrivelseAvOmsorgsrollen.isEmpty();
    }

    public enum BarnRelasjon {
        MOR("Mor"),
        MEDMOR("Medmor"),
        FAR("Far"),
        FOSTERFORELDER("Fosterforelder"),
        ANNET("Annet");

        private final String rolle;

        BarnRelasjon(String rolle) {
            this.rolle = rolle;
        }

        public String getRolle() {
            return rolle;
        }

    }
}
