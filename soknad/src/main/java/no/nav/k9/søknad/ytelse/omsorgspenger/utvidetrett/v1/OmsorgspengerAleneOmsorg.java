package no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OmsorgspengerAleneOmsorg implements OmsorgspengerUtvidetRett {

    /** Barn søkt om alene om omsorg for. */
    @Valid
    @JsonProperty(value = "barn")
    @NotNull
    private Barn barn;

    /** Periode alene om omsorgen.  Evt. <code>[startdato, )</code> eller <code>( ,sluttdato]</code> */
    @Valid
    @JsonProperty(value = "periode")
    @NotNull
    private Periode periode;

    @JsonProperty(value = "begrunnelse")
    @Size(max = 30000)
    private String begrunnelse;

    public OmsorgspengerAleneOmsorg() {
    }

    @JsonCreator
    public OmsorgspengerAleneOmsorg(@JsonProperty(value = "barn", required = true) @Valid @NotNull Barn barn,
                                    @Valid @JsonProperty(value = "periode") @NotNull Periode periode,
                                    @JsonProperty(value = "begrunnelse") String begrunnelse) {
        this.begrunnelse = begrunnelse;
        this.periode = Objects.requireNonNull(periode);
        this.barn = Objects.requireNonNull(barn);
    }

    @Override
    public Type getType() {
        return Type.OMSORGSPENGER_UTVIDETRETT_ALENE_OMSORG;
    }

    @Override
    public YtelseValidator getValidator() {
        return new MinValidator();
    }

    public String getBegrunnelse() {
        return begrunnelse;
    }

    @Override
    public Person getAnnenPart() {
        return null;
    }

    public Barn getBarn() {
        return barn;
    }

    @Override
    public Person getPleietrengende() {
        return barn;
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return List.of(barn);
    }

    /** NB: kan returnere null dersom ikke oppgitt i søknad */
    @Override
    public Periode getSøknadsperiode() {
        return periode;
    }

    public static class MinValidator extends YtelseValidator {

        @Override
        public List<Feil> valider(Ytelse søknad) {
            return List.of();
        }
    }
}
