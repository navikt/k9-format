package no.nav.k9.søknad.ytelse.ung.v1;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.ung.v1.inntekt.OppgittInntekt;
import no.nav.k9.søknad.ytelse.ung.v1.inntekt.OppgittInntektForPeriode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

class InntektrapporteringValideringTest {

    @Test
    void verifiserInntektrapporteringMedEnInntektFeilerIkke() {
        final var inntekt = lagInntekt(new Periode(LocalDate.now(), LocalDate.now()));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt));
        ValiderUtil.verifyIngenFeil(inntektrapportering);
    }

    @Test
    void verifiserInntektrapporteringMedToIkkeOverlappendeInntekterFeilerIkke() {
        final var inntekt = lagInntekt(new Periode(LocalDate.now(), LocalDate.now()));
        final var inntekt2 = lagInntekt(new Periode(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt, inntekt2));
        ValiderUtil.verifyIngenFeil(inntektrapportering);
    }

    @Test
    void verifiserInntektrapporteringMedToOverlappendeInntekterSomStarterSamtidigFeiler() {
        final var inntekt = lagInntekt(new Periode(LocalDate.now(), LocalDate.now()));
        final var inntekt2 = lagInntekt(new Periode(LocalDate.now(), LocalDate.now().plusDays(1)));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt, inntekt2));
        ValiderUtil.verifyHarFeil(inntektrapportering);
    }

    @Test
    void verifiserInntektrapporteringMedToOverlappendeInntekterSomSlutterSamtidigFeiler() {
        final var inntekt = lagInntekt(new Periode(LocalDate.now(), LocalDate.now().plusDays(1)));
        final var inntekt2 = lagInntekt(new Periode(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt2, inntekt));
        ValiderUtil.verifyHarFeil(inntektrapportering);
    }

    private static OppgittInntektForPeriode lagInntekt(Periode periode) {
        return OppgittInntektForPeriode.builder(periode).build();
    }

    @Test
    void verifiserInntektrapporteringMedToOverlappendeInntekterDerDenEneErInneholdtIDenAndreFeiler() {
        final var inntekt = lagInntekt(new Periode(LocalDate.now(), LocalDate.now().plusDays(3)));
        final var inntekt2 = lagInntekt(new Periode(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)));
        var inntektrapportering = inntektRapportering(lagOppgittInntekt(inntekt, inntekt2));
        ValiderUtil.verifyHarFeil(inntektrapportering);
    }



    private static OppgittInntekt lagOppgittInntekt(OppgittInntektForPeriode... inntekt) {
        return OppgittInntekt.builder().medOppgittePeriodeinntekter(Set.of(inntekt)).build();
    }


    private static Søknad inntektRapportering(OppgittInntekt inntekter) {
        return new Søknad(
                new SøknadId("1"),
                new Versjon("6.0.1"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("22222222222")),
                        new Ungdomsytelse()
                                .medInntekter(inntekter)
                                .medDeltakelseId(UUID.randomUUID())
                                .medSøknadType(UngSøknadstype.RAPPORTERING_SØKNAD)
        );
    }

}
