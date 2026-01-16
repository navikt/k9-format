package no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OmsorgspengerMidlertidigAlene implements OmsorgspengerUtvidetRett {

    @Valid
    @JsonProperty(value = "annenForelder", required = true)
    @NotNull
    private AnnenForelder annenForelder;

    @JsonProperty(value = "barn")
    @NotNull
    private List<@Valid Barn> barn;

    @JsonProperty(value = "begrunnelse")
    @Size(max = 30000)
    private String begrunnelse;

    @JsonProperty(value = "dataBruktTilUtledning")
    @Valid
    private DataBruktTilUtledning dataBruktTilUtledning;

    public OmsorgspengerMidlertidigAlene() {
    }

    @JsonCreator
    public OmsorgspengerMidlertidigAlene(@JsonProperty(value = "barn", required = true) @Valid @NotNull Collection<Barn> barn,
                                         @JsonProperty(value = "annenForelder", required = true) @Valid @NotNull AnnenForelder annenForelder,
                                         @JsonProperty(value = "begrunnelse") String begrunnelse) {
        this.begrunnelse = begrunnelse;
        this.annenForelder = Objects.requireNonNull(annenForelder, "annenForelder");
        this.barn = barn == null ? null : new ArrayList<>(barn);
    }

    public OmsorgspengerMidlertidigAlene medBarn(Barn... barn) {
        if (barn == null || barn.length == 0) {
            this.barn = null;
            return this;
        }

        if (this.barn == null) {
            this.barn = new ArrayList<>();
        }
        this.barn.addAll(Arrays.asList(Objects.requireNonNull(barn, "barn")));
        return this;
    }

    public OmsorgspengerMidlertidigAlene medAnnenForelder(AnnenForelder annenForelder) {
        this.annenForelder = annenForelder;
        return this;
    }

    @Override
    public Type getType() {
        return Type.OMSORGSPENGER_UTVIDETRETT_MIDLERTIDIG_ALENE;
    }

    @Override
    public YtelseValidator getValidator(Versjon versjon) {
        return new MinValidator();
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return this.dataBruktTilUtledning;
    }

    @Override
    public Ytelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        this.dataBruktTilUtledning = dataBruktTilUtledning;
        return this;
    }

    public String getBegrunnelse() {
        return begrunnelse;
    }

    @Override
    public Person getAnnenPart() {
        return getAnnenForelder();
    }

    public AnnenForelder getAnnenForelder() {
        return annenForelder;
    }

    public List<Barn> getBarn() {
        return barn == null ? barn : Collections.unmodifiableList(barn);
    }

    @Override
    public Person getPleietrengende() {
        return null; // ignorerer her
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return Stream.of(barn, List.of(annenForelder)).flatMap(List::stream).collect(Collectors.toList());
    }

    /** NB: kan returnere null dersom ikke oppgitt i søknad */
    @Override
    public Periode getSøknadsperiode() {
        var annenForelderPeriode = getAnnenForelder().getPeriode();
        return annenForelderPeriode;
    }

    /** @deprecated bruk istedet {@link OmsorgspengerMidlertidigAleneSøknadValidator} */
    @Deprecated(forRemoval = true, since = "6.1.1")
    public static class MinValidator extends YtelseValidator {

        @Override
        public List<Feil> valider(Ytelse søknad) {
            return List.of();
        }
    }
}
