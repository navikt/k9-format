package no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1;

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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OmsorgspengerKroniskSyktBarn implements OmsorgspengerUtvidetRett {

    @Valid
    @JsonProperty(value = "barn", required = true)
    @NotNull
    private Barn barn;

    @JsonProperty(value = "kroniskEllerFunksjonshemming")
    @Valid
    @NotNull
    private Boolean kroniskEllerFunksjonshemming;

    @JsonProperty(value = "dataBruktTilUtledning")
    @Valid
    private DataBruktTilUtledning dataBruktTilUtledning;

    public OmsorgspengerKroniskSyktBarn() {
    }

    @JsonCreator
    public OmsorgspengerKroniskSyktBarn(@JsonProperty(value = "barn", required = true) @Valid @NotNull Barn barn,
                                        @JsonProperty(value = "kroniskEllerFunksjonshemming") @Valid @NotNull Boolean kroniskEllerFunksjonshemming) {
        this.barn = Objects.requireNonNull(barn, "barn");
        this.kroniskEllerFunksjonshemming = Objects.requireNonNull(kroniskEllerFunksjonshemming, "kroniskEllerFunksjonshemming");
    }

    public OmsorgspengerKroniskSyktBarn medBarn(Barn barn) {
        this.barn = Objects.requireNonNull(barn, "barn");
        return this;
    }

    public OmsorgspengerKroniskSyktBarn medKroniskEllerFunksjonshemming(Boolean kroniskEllerFunksjonshemming) {
        this.kroniskEllerFunksjonshemming = Objects.requireNonNull(kroniskEllerFunksjonshemming, "kroniskEllerFunksjonshemming");
        return this;
    }

    @Override
    public Type getType() {
        return Type.OMSORGSPENGER_UTVIDETRETT_KRONISK_SYKT_BARN;
    }
    @Override
    public YtelseValidator getValidator() {
        return new MinValidator();
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


    @Override
    public List<Person> getBerørtePersoner() {
        return List.of(barn);
    }
    
    @Override
    public Person getPleietrengende() {
        return barn;
    }

    @Override
    public Person getAnnenPart() {
        return null;
    }
    
    @Override
    public Periode getSøknadsperiode() {
        // bestemmes ut fra [mottattdato, fødseldato+18år]
        return null;
    }

    public Barn getBarn() {
        return barn;
    }

    public Boolean getKroniskEllerFunksjonshemming() {
        return kroniskEllerFunksjonshemming;
    }

    /** @deprecated bruk istedet {@link OmsorgspengerKroniskSyktBarnSøknadValidator} */
    @Deprecated(forRemoval = true, since = "6.1.1")
    public static class MinValidator extends YtelseValidator {

        @Override
        public List<Feil> valider(Ytelse søknad) {
            return List.of();
        }
    }
}
