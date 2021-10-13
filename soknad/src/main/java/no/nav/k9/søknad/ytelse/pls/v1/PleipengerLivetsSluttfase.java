package no.nav.k9.søknad.ytelse.pls.v1;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.PLEIEPENGER_LIVETS_SLUTTFASE)
public class PleipengerLivetsSluttfase implements Ytelse{

    @Valid
    @NotNull
    @JsonProperty(value = "pleietrengende", required = true)
    private Pleietrengende pleietrengende;

    @Valid
    @NotNull
    @JsonProperty(value = "arbeidstid", required = true)
    private Arbeidstid arbeidstid = new Arbeidstid();

    @Valid
    @JsonProperty(value = "opptjeningAktivitet")
    private OpptjeningAktivitet opptjeningAktivitet = new OpptjeningAktivitet();

    @Valid
    @JsonProperty(value = "bosteder", required = true)
    private Bosteder bosteder = new Bosteder();

    @Valid
    @JsonProperty(value = "utenlandsopphold", required = true)
    private Utenlandsopphold utenlandsopphold = new Utenlandsopphold();

    @Override
    public Type getType() {
        return Type.PLEIEPENGER_LIVETS_SLUTTFASE;
    }

    @Override
    public YtelseValidator getValidator() {
        //TODO lage validator
        return null;
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return List.of(pleietrengende);
    }

    public Pleietrengende getPleietrengende() {
        return pleietrengende;
    }

    @Override
    public Person getAnnenPart() {
        return null;
    }

    @Override
    public Periode getSøknadsperiode() {
        //TODO utlede omsluttende periode
        return null;
    }

    public Arbeidstid getArbeidstid() {
        return arbeidstid;
    }

    public OpptjeningAktivitet getOpptjeningAktivitet() {
        return opptjeningAktivitet;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    public PleipengerLivetsSluttfase medPleietrengende(Pleietrengende pleietrengende) {
        this.pleietrengende = Objects.requireNonNull(pleietrengende, "pleietrengende");
        return this;
    }

    public PleipengerLivetsSluttfase medArbeidstid(Arbeidstid arbeidstid) {
        this.arbeidstid = Objects.requireNonNull(arbeidstid, "arbeidstid");
        return this;
    }

    public PleipengerLivetsSluttfase medOpptjeningAktivitet(OpptjeningAktivitet opptjeningAktivitet) {
        this.opptjeningAktivitet = Objects.requireNonNull(opptjeningAktivitet, "opptjeningAktivitet");
        return this;
    }

    public PleipengerLivetsSluttfase medBosteder(Bosteder bosteder) {
        this.bosteder = Objects.requireNonNull(bosteder, "bosteder");
        return this;
    }

    public PleipengerLivetsSluttfase medUtenlandsopphold(Utenlandsopphold utenlandsopphold) {
        this.utenlandsopphold = Objects.requireNonNull(utenlandsopphold, "utenlandsopphold");
        return this;
    }
}
