package no.nav.k9.søknad.ytelse.olp.v1;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kurs;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.KursPeriode;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kursholder;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Reise;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;

class OpplæringspengerYtelseValidatorTest {

    private final OpplæringspengerYtelseValidator ytelseValidator = new OpplæringspengerYtelseValidator();

    private Opplæringspenger lagYtelse() {
        Periode søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(1));
        KursPeriode kursPeriode = new KursPeriode(søknadsperiode);
        Reise reise = new Reise(true, List.of(LocalDate.now()), "Langt å kjøre");
        Kurs kurs = new Kurs(new Kursholder(UUID.randomUUID()), List.of(kursPeriode), reise);
        return new Opplæringspenger().medBarn(YtelseEksempel.lagBarn()).medSøknadsperiode(List.of(søknadsperiode)).medUttak(YtelseEksempel.lagUttak(søknadsperiode)).medKurs(kurs);
    }

    @Test
    void skalValidereOk() {
        Opplæringspenger olpYtelse = lagYtelse();

        List<Feil> feil = ytelseValidator.valider(olpYtelse);
        assertThat(feil).isEmpty();
    }
}
