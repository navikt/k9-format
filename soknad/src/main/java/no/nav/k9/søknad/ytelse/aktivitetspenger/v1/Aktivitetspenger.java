package no.nav.k9.søknad.ytelse.aktivitetspenger.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.TidUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.validering.periode.LukketPeriode;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.ung.v1.inntekt.OppgittInntekt;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Aktivitetspenger implements Ytelse {


    @Valid
    @LukketPeriode
    @JsonProperty("søknadsperiode")
    private Periode søknadsperiode;

    @Valid
    @JsonProperty("søknadsperiodeFom")
    private LocalDate søknadsperiodeFom;

    @Valid
    @JsonProperty(value = "forutgåendeBosteder", required = true)
    private Bosteder forutgåendeBosteder = new Bosteder();

    @Valid
    @JsonProperty(value = "inntekter", required = false)
    private OppgittInntekt inntekter;

    @JsonProperty(value = "erBosattITrondheim", required = false)
    private Boolean erBosattITrondheim;

    @Override
    public Type getType() {
        return Type.AKTIVITETSPENGER;
    }

    @Override
    public YtelseValidator getValidator(Versjon versjon) {
        return new YtelseValidator() {
            @Override
            public List<Feil> valider(Ytelse søknad) {
                return List.of();
            }
        };
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return new DataBruktTilUtledning();
    }

    @Override
    public Ytelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        return this;
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return List.of();
    }

    @Override
    public Person getPleietrengende() {
        return null;
    }

    @Override
    public Person getAnnenPart() {
        return null;
    }

    @Override
    public Periode getSøknadsperiode() {
        return søknadsperiode != null ? søknadsperiode : new Periode(søknadsperiodeFom, TidUtils.TIDENES_ENDE);
    }

    public LocalDate getSøknadsperiodeFom() {
        return søknadsperiodeFom;
    }

    public Bosteder getForutgåendeBosteder() {
        return forutgåendeBosteder;
    }

    public OppgittInntekt getInntekter() {
        return inntekter;
    }

    public Aktivitetspenger medSøknadsperiode(Periode søknadsperiode) {
        this.søknadsperiode = Objects.requireNonNull(søknadsperiode, "søknadsperiode");
        return this;
    }

    public Aktivitetspenger medSøknadsperiodeFom(LocalDate fom) {
        this.søknadsperiodeFom = Objects.requireNonNull(fom, "søknadsperiodeFom");
        return this;
    }


    public Aktivitetspenger medForutgåendeBosteder(Bosteder bosteder) {
        this.forutgåendeBosteder = Objects.requireNonNull(bosteder, "bosteder");
        return this;
    }

    public Aktivitetspenger medInntekter(OppgittInntekt inntekter) {
        this.inntekter = Objects.requireNonNull(inntekter, "inntekter");
        return this;
    }

    public Boolean getErBosattITrondheim() {
        return erBosattITrondheim;
    }

    public Aktivitetspenger medErBosattITrondheim(Boolean erBosattITrondheim) {
        this.erBosattITrondheim = erBosattITrondheim;
        return this;
    }

}
