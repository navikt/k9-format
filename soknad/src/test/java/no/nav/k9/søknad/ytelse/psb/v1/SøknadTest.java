package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagBosteder;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagLovbestemtFerie;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagUtenlandsopphold;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;

class SøknadTest {
    private static final Periode TEST_PERIODE = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
    private static final Søknad MINIMUM_SØKNAD = SøknadEksempel.minimumSøknad(TEST_PERIODE);

    @Test
    public void komplettSøknadHarIngenValideringFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var utelands = new Periode(LocalDate.now().minusYears(1), LocalDate.now());
        var bosteder = new Periode(LocalDate.now().minusYears(1), LocalDate.now().plusWeeks(4));
        var lovbestemtferie = new Periode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(4));

        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(søknadsperiode, lovbestemtferie, utelands, bosteder);
        verifyIngenFeil(SøknadEksempel.søknad(ytelse));
    }

    @Test
    public void minimumSøknadHarIngenValideringsFeil() {
        verifyIngenFeil(MINIMUM_SØKNAD);
    }

    @Test
    public void barnKanIkkeVæreSøker() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));

        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        søknad.medSøker(new Søker(NorskIdentitetsnummer.of(søknad.getBerørtePersoner().get(0).getPersonIdent().getVerdi())));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, new Feil("søker", "søkerSammeSomBarn", "Søker kan ikke være barn." ));

    }

    @Test
    public void søkerKanIkkeVæreNull() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        assertThrows(NullPointerException.class, () -> søknad.medSøker(null));
    }

    @Test
    public void bostederKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        ((PleiepengerSyktBarn) søknad.getYtelse()).medBosteder(lagBosteder(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    public void bostederKanIkkeHaInvertertePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(søknadsperiode.getTilOgMed(), LocalDate.now().minusMonths(2));

        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleiepengerSyktBarn) søknad.getYtelse()).medBosteder(lagBosteder(bostedperiode));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.bosteder.perioder" + TestUtils.periodeString(bostedperiode),
                "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        Søknad.SerDes.serialize(søknad);
    }

    @Test
    public void utenlandsoppholdKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleiepengerSyktBarn) søknad.getYtelse()).medUtenlandsopphold(lagUtenlandsopphold(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    public void alleFelterISøknadInvertertPeriode() {
        var søknadsperiode = new Periode(LocalDate.now().plusWeeks(2), LocalDate.now().minusWeeks(2));
        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode)
                .medBosteder(YtelseEksempel.lagBosteder(søknadsperiode))
                .medUtenlandsopphold(YtelseEksempel.lagUtenlandsopphold(søknadsperiode));
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad, List.of(søknadsperiode));

        feilInneholder(feil, "ytelse.søknadsperiode.perioder" + TestUtils.periodeString(0), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.bosteder.perioder" + TestUtils.periodeString(søknadsperiode), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.utenlandsopphold.perioder" + TestUtils.periodeString(søknadsperiode), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        assertThat(feil).size().isEqualTo(3);
    }

    @Test
    public void alleFelterISøknadInvertertPeriodeKasterException() {
        PleiepengerSyktBarnYtelseValidator pleiepengerSyktBarnYtelseValidator = new PleiepengerSyktBarnYtelseValidator();
        var søknadsperiode = new Periode(LocalDate.now().plusWeeks(2), LocalDate.now().minusWeeks(2));
        var ytelse = ytelseMedSøknadsperideOgArbeidstid(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        assertThrows(PleiepengerSyktBarnYtelseValidator.ValideringsAvbrytendeFeilException.class, () -> pleiepengerSyktBarnYtelseValidator.validerOgLeggTilFeilene(søknad.getYtelse(), List.of())
        );
    }

    @Test
    public void søknadUtenSøknadsperiodeOgGylidgIntervalForEndring() {
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), LocalDate.now().plusWeeks(2));
        var ytese = YtelseEksempel.lagYtelse()
                .medLovbestemtFerie(lagLovbestemtFerie(endringsperiode));
        var søknad = SøknadEksempel.søknad(ytese);

        var feil = verifyHarFeil(søknad, List.of());
        feilInneholder(feil, "missingArgument");
    }

    //TODO legge på getSøknadsperioder test

  


}
