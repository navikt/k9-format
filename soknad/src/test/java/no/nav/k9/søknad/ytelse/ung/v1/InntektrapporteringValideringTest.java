package no.nav.k9.søknad.ytelse.ung.v1;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.ung.SøknadEksempel;
import no.nav.k9.søknad.ytelse.ung.v1.inntekt.InntektForPeriode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

class InntektrapporteringValideringTest {

    @Test
    void verifiserInntektrapporteringUtenInntekterFeilerIkke() {
        var inntektrapportering = inntektRapportering(OppgittInntekt.builder().build());
        ValiderUtil.verifyIngenFeil(inntektrapportering);
    }

    @Test
    void verifiserInntektrapporteringMedEnInntektFeilerIkke() {
        final var inntekt = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now(), LocalDate.now()));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt));
        ValiderUtil.verifyIngenFeil(inntektrapportering);
    }

    @Test
    void verifiserInntektrapporteringMedToIkkeOverlappendeInntekterFeilerIkke() {
        final var inntekt = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now(), LocalDate.now()));
        final var inntekt2 = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt, inntekt2));
        ValiderUtil.verifyIngenFeil(inntektrapportering);
    }

    @Test
    void verifiserInntektrapporteringMedToOverlappendeInntekterSomStarterSamtidigFeiler() {
        final var inntekt = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now(), LocalDate.now()));
        final var inntekt2 = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now(), LocalDate.now().plusDays(1)));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt, inntekt2));
        ValiderUtil.verifyHarFeil(inntektrapportering);
    }

    @Test
    void verifiserInntektrapporteringMedToOverlappendeInntekterSomSlutterSamtidigFeiler() {
        final var inntekt = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now(), LocalDate.now().plusDays(1)));
        final var inntekt2 = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt2, inntekt));
        ValiderUtil.verifyHarFeil(inntektrapportering);
    }

    @Test
    void verifiserInntektrapporteringMedToOverlappendeInntekterDerDenEneErInneholdtIDenAndreFeiler() {
        final var inntekt = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now(), LocalDate.now().plusDays(3)));
        final var inntekt2 = new InntektForPeriode(BigDecimal.TEN, new Periode(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt, inntekt2));
        ValiderUtil.verifyHarFeil(inntektrapportering);
    }



    private static OppgittInntekt lagOppgittInntekt(InntektForPeriode... inntekt) {
        return OppgittInntekt.builder().medArbeidstakerOgFrilansinntekter(Set.of(inntekt)).build();
    }


    private static UngdomsytelseInntektrapportering inntektRapportering(OppgittInntekt inntekter) {
        return new UngdomsytelseInntektrapportering(
                new SøknadId("1"),
                new Versjon("6.0.1"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("22222222222")),
                inntekter
        );
    }

}
