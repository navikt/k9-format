package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.pls.SøknadEksempel;

class SøknadTest {
    private static final Periode TEST_PERIODE = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
    private static final Søknad MINIMUM_SØKNAD = SøknadEksempel.søknadMedArbeidstid(TEST_PERIODE);

    @Test
    public void komplettSøknadHarIngenValideringFeil() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var utelands = new Periode(LocalDate.now().minusYears(1), LocalDate.now());
        var bosteder = new Periode(LocalDate.now().minusYears(1), LocalDate.now().plusWeeks(4));

        var ytelse = YtelseEksempel.ytelseMedUtenlandstilsnitt(søknadsperiode, utelands, bosteder);
        verifyIngenFeil(SøknadEksempel.søknad(ytelse));
    }

    @Test
    public void minimumSøknadHarIngenValideringsFeil() {
        verifyIngenFeil(MINIMUM_SØKNAD);
    }

    @Test
    public void barnKanIkkeVæreSøker() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        søknad.medSøker(new Søker(NorskIdentitetsnummer.of(søknad.getBerørtePersoner().get(0).getPersonIdent().getVerdi())));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, new Feil("søker", "søkerSammeSomPleietrengende", "Søker kan ikke være samme person som er i livets sluttfase."));
    }

    @Test
    public void søkerKanIkkeVæreNull() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        assertThrows(NullPointerException.class, () -> søknad.medSøker(null));
    }

    @Test
    public void bostederKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);

        ((PleipengerLivetsSluttfase) søknad.getYtelse()).medBosteder(YtelseEksempel.lagBosteder(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    public void bostederKanIkkeHaInvertertePerioder() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(søknadsperiode.getTilOgMed(), LocalDate.now().minusMonths(2));
        var utelandsperiode = new Periode(søknadsperiode.getTilOgMed(), LocalDate.now().minusMonths(2));

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleipengerLivetsSluttfase) søknad.getYtelse()).medBosteder(YtelseEksempel.lagBosteder(bostedperiode)).medUtenlandsopphold(YtelseEksempel.lagUtenlandsopphold(Utenlandsopphold.UtenlandsoppholdÅrsak.BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING, utelandsperiode));

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.bosteder.perioder" + TestUtils.periodeString(bostedperiode),
                "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        Søknad.SerDes.serialize(søknad);
    }

    @Test
    public void utenlandsoppholdKanVæreUtenforSøknadsperiode() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusMonths(2));
        var bostedperiode = new Periode(LocalDate.now().minusMonths(2), søknadsperiode.getTilOgMed());

        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode);
        var søknad = SøknadEksempel.søknad(ytelse);
        ((PleipengerLivetsSluttfase) søknad.getYtelse()).medUtenlandsopphold(YtelseEksempel.lagUtenlandsopphold(bostedperiode));

        verifyIngenFeil(søknad);
    }

    @Test
    public void alleFelterISøknadInvertertPeriode() {
        LocalDate startdato = LocalDate.of(2021, 12, 14);
        var søknadsperiode = new Periode(startdato.plusWeeks(2), startdato.minusWeeks(2));
        var ytelse = YtelseEksempel.ytelseForArbeidstaker(søknadsperiode)
                .medBosteder(YtelseEksempel.lagBosteder(søknadsperiode))
                .medUtenlandsopphold(YtelseEksempel.lagUtenlandsopphold(søknadsperiode));
        var søknad = SøknadEksempel.søknad(ytelse);

        var feil = verifyHarFeil(søknad);

        feilInneholder(feil, "ytelse.arbeidstid.arbeidstakerList[0].perioder['2021-12-28/2021-11-30']", "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.bosteder.perioder" + TestUtils.periodeString(søknadsperiode), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        feilInneholder(feil, "ytelse.utenlandsopphold.perioder" + TestUtils.periodeString(søknadsperiode), "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM).");
        assertThat(feil).size().isEqualTo(3);
    }


    @Test
    public void søknadUtenSøknadsperiode() {
        var tomYtelse = YtelseEksempel.lagYtelse();
        var søknad = SøknadEksempel.søknad(tomYtelse);

        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, "ytelse.arbeidstid", "påkrevd", "det finnes ingen søknadsperiode");
    }


}
