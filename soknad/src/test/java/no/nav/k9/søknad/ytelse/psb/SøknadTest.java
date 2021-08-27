package no.nav.k9.søknad.ytelse.psb;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilListInneholderFeil;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnSøknadValidator;

class SøknadTest {
    private static final PleiepengerSyktBarnSøknadValidator validator = new PleiepengerSyktBarnSøknadValidator();
    public static final Søknad KOMPLETT_SØKNAD = SøknadEksempel.komplettSøknad(new Periode(LocalDate.now(), LocalDate.now().plusMonths(2)));
    protected static final String PÅKREVD = "påkrevd";
    protected static final String UGYLDIG_ARGUMENT = "ugyldig argument";

    @Test
    public void komplettSøknadHarIngenValideringFeil() {
        var feil = valider(KOMPLETT_SØKNAD);
        assertThat(feil).isEmpty();
    }

    @Test
    public void barnKanIkkeVæreSøker() {
        var søknad = KOMPLETT_SØKNAD;
        søknad.medSøker(new Søker(NorskIdentitetsnummer.of(søknad.getBerørtePersoner().get(0).getPersonIdent().getVerdi())));

        var feil = valider(søknad);
        assertThat(feil).isNotEmpty();
        feilListInneholderFeil(feil, new Feil("søker", "søkerSammeSomBarn", "Søker kan ikke være barn." ));

    }

    @Test
    public void søkerKanIkkeVæreNull() {
        var søknad = KOMPLETT_SØKNAD;
        søknad.medSøker(null);

        var feil = valider(søknad);
        assertThat(feil).isNotEmpty();
        feilListInneholderFeil(feil, new Feil("søker", PÅKREVD, "must not be null"));
    }


    private List<Feil> valider(Søknad søknad) {
        try {
            return validator.valider(søknad);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }


}
