package no.nav.k9.oppgave.util;

import no.nav.k9.oppgave.OppgaveBekreftelse;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.inntekt.InntektBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretProgramperiodeBekreftelse;
import no.nav.k9.søknad.felles.Kildesystem;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;

import java.time.ZonedDateTime;
import java.util.UUID;

public class OppgaveUtil {

    public static OppgaveBekreftelse lagOppgaveBekreftelse(Bekreftelse bekreftelse) {
        return new OppgaveBekreftelse()
                .medSøknadId(SøknadId.of(UUID.randomUUID().toString()))
                .medVersjon("1.0.0")
                .medMottattDato(ZonedDateTime.now())
                .medSøker(new Søker(NorskIdentitetsnummer.of("02119970078")))
                .medBekreftelse(bekreftelse)
                .medKildesystem(Kildesystem.SØKNADSDIALOG);
    }

    public static EndretProgramperiodeBekreftelse bekreftelseEndretProgramperiode(UUID oppgaveReferanse, Periode nyPeriode, boolean harBrukerGodtattEndringen) {
        return new EndretProgramperiodeBekreftelse(oppgaveReferanse, nyPeriode, harBrukerGodtattEndringen);
    }

    public static InntektBekreftelse bekreftelseAvvikRegisterinntekt(boolean harBrukerGodtattEndringen, String uttalelseFraBruker) {
        return new InntektBekreftelse(UUID.randomUUID(), harBrukerGodtattEndringen, uttalelseFraBruker);
    }

}
