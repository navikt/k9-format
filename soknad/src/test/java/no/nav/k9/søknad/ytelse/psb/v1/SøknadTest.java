package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagArbeidstaker;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagBeredskap;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagBosteder;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagLovbestemtFerie;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagNattevåk;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagTilsynsordning;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagUtenlandsopphold;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.lagUttak;
import static no.nav.k9.søknad.ytelse.psb.YtelseEksempel.leggPåKomplettEndringsøknad;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.TestUtils;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

class SøknadTest {
    private static final Periode TEST_PERIODE = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
    private static final Søknad KOMPLETT_SØKNAD = SøknadEksempel.komplettSøknad(TEST_PERIODE);
    private static final Søknad MINIMUM_SØKNAD = SøknadEksempel.minimumSøknad(TEST_PERIODE);
    private static final String PÅKREVD = "påkrevd";

    @Test
    public void komplettSøknadHarIngenValideringFeil() {
        verifyIngenFeil(KOMPLETT_SØKNAD);
    }

    @Test
    public void minimumSøknadHarIngenValideringsFeil() {
        verifyIngenFeil(MINIMUM_SØKNAD);
    }

    @Test
    public void barnKanIkkeVæreSøker() {
        var søknad = KOMPLETT_SØKNAD;
        søknad.medSøker(new Søker(NorskIdentitetsnummer.of(søknad.getBerørtePersoner().get(0).getPersonIdent().getVerdi())));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, new Feil("søker", "søkerSammeSomBarn", "Søker kan ikke være barn." ));

    }

    @Test
    public void søkerKanIkkeVæreNull() {
        var søknad = KOMPLETT_SØKNAD;
        søknad.medSøker(null);

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, new Feil("søker", PÅKREVD, "must not be null"));
    }

    @Test
    public void bostederKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var søknad = SøknadEksempel.komplettSøknad(søknadsperiode);
        ((PleiepengerSyktBarn) søknad.getYtelse()).medBosteder(lagBosteder(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    public void bostederKanIkkeHaInvertertePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(søknadsperiode.getTilOgMed(), LocalDate.now().minusMonths(2));

        var søknad = SøknadEksempel.komplettSøknad(søknadsperiode);
        ((PleiepengerSyktBarn) søknad.getYtelse()).medBosteder(lagBosteder(bostedperiode));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.bosteder.perioder" + TestUtils.periodeString(bostedperiode), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
    }

    @Disabled("Trenger avklaring om dette er ønsket")
    @Test
    public void utenlandsoppholdKanIkkeVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var søknad = SøknadEksempel.komplettSøknad(søknadsperiode);
        ((PleiepengerSyktBarn) søknad.getYtelse()).medUtenlandsopphold(lagUtenlandsopphold(bostedperiode));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.utenlandsopphold.perioder", "ugyldigPeriode");
    }

    @Test
    public void alleFelterISøknadInvertertPeriode() {
        var søknadsperiode = new Periode(LocalDate.now().plusWeeks(2), LocalDate.now().minusWeeks(2));
        var søknad = SøknadEksempel.komplettSøknad(søknadsperiode);
        ((PleiepengerSyktBarn)søknad.getYtelse()).medEndringsperiode(søknadsperiode);
        ((PleiepengerSyktBarn)søknad.getYtelse()).addTrekkKravPeriode(new Periode(LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(3)));

        var feil = verifyHarFeil(søknad, List.of(søknadsperiode));
        var periodeString = "[" + søknadsperiode + "]";
        feilInneholder(feil, "ytelse.søknadsperiode.perioder" + TestUtils.periodeString(0), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.bosteder.perioder" + TestUtils.periodeString(søknadsperiode), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.utenlandsopphold.perioder" + TestUtils.periodeString(søknadsperiode), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        assertThat(feil).size().isEqualTo(3);
    }

    @Test
    public void søknadHarIkkeIntervalForEndring() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var endringsperiode = new Periode(LocalDate.now().minusWeeks(2), søknadsperiode.getFraOgMed().minusDays(1));
        var ytese = ytelseUtenSøknadsperiode(søknadsperiode);
        leggPåKomplettEndringsøknad(endringsperiode, ytese);
        var søknad = SøknadEksempel.søknad(ytese);

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "missingArgument");
    }

    //TODO legge på getSøknadsperioder test

    private PleiepengerSyktBarn ytelseUtenSøknadsperiode(Periode... ytelsePeriode ){
        var barn = new Barn(NorskIdentitetsnummer.of("22211111111"), null);
        var omsorg = new Omsorg().medRelasjonTilBarnet(Omsorg.BarnRelasjon.MOR);
        var søknadInfo = new DataBruktTilUtledning( true, true,
                false, false, true );
        var infoFraPunsj = new InfoFraPunsj()
                .medSøknadenInneholderInfomasjonSomIkkeKanPunsjes(false);
        var uttak = lagUttak(ytelsePeriode);
        var nattevåk = lagNattevåk(ytelsePeriode);
        var beredskap = lagBeredskap(ytelsePeriode);
        var tilsynsordning = lagTilsynsordning(ytelsePeriode);
        var lovbestemtFerie = lagLovbestemtFerie(ytelsePeriode);
        var bosteder = lagBosteder(ytelsePeriode);
        var utenlandsopphold = lagUtenlandsopphold(ytelsePeriode);
        var arbeidstaker = lagArbeidstaker(ytelsePeriode);
        var arbeidstid = new Arbeidstid().medArbeidstaker(List.of(
                arbeidstaker));

        return new PleiepengerSyktBarn()
                .medSøknadInfo(søknadInfo)
                .medInfoFraPunsj(infoFraPunsj)
                .medBarn(barn)
                .medBeredskap(beredskap)
                .medNattevåk(nattevåk)
                .medTilsynsordning(tilsynsordning)
                .medArbeidstid(arbeidstid)
                .medUttak(uttak)
                .medUtenlandsopphold(utenlandsopphold)
                .medOmsorg(omsorg)
                .medLovbestemtFerie(lovbestemtFerie)
                .medBosteder(bosteder);
    }


}
