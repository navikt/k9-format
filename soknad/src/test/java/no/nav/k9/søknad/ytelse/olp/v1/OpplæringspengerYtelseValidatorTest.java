package no.nav.k9.søknad.ytelse.olp.v1;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kurs;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.KursPeriodeMedReisetid;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kursholder;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;

class OpplæringspengerYtelseValidatorTest {

    private final OpplæringspengerYtelseValidator ytelseValidator = new OpplæringspengerYtelseValidator();

    private Opplæringspenger lagYtelse(String holder, UUID institusjonUuid) {
        Periode søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(1));
        KursPeriodeMedReisetid kursPeriode = new KursPeriodeMedReisetid(søknadsperiode, søknadsperiode.getFraOgMed(), søknadsperiode.getTilOgMed());
        Kurs kurs = new Kurs(new Kursholder(holder, institusjonUuid), List.of(kursPeriode));
        return new Opplæringspenger().medBarn(YtelseEksempel.lagBarn()).medSøknadsperiode(List.of(søknadsperiode)).medUttak(YtelseEksempel.lagUttak(søknadsperiode)).medKurs(kurs);
    }

    @Test
    void skalValidereOk() {
        Opplæringspenger olpYtelse = lagYtelse(null, UUID.randomUUID());

        List<Feil> feil = ytelseValidator.valider(olpYtelse);
        assertThat(feil).isEmpty();
    }

    @Test
    void skalGiFeilHvisBådeHolderOgUuidErNull() {
        Opplæringspenger olpYtelse = lagYtelse(null, null);

        List<Feil> feil = ytelseValidator.valider(olpYtelse);
        TestUtils.feilInneholder(feil, "ytelse.kurs.kursholder", "ugyldigHolderEllerInstitusjonUuid", "Enten holder eller institusjonUuid må være satt.");
    }

    @Test
    void skalGiFeilHvisBådeHolderOgUuidErSatt() {
        Opplæringspenger olpYtelse = lagYtelse("Franz Holder", UUID.randomUUID());

        List<Feil> feil = ytelseValidator.valider(olpYtelse);
        TestUtils.feilInneholder(feil, "ytelse.kurs.kursholder", "ugyldigHolderEllerInstitusjonUuid", "Kan ikke ha både holder og institusjonUuid satt samtidig.");
    }
}
