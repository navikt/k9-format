package no.nav.k9.innsyn;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

public class PsbSøknaddataHendelseTest {

    @Test
    public void kanLageOgLeseHendelse() {
        final var søknad = lagGyldigSøknad();
        final PsbSøknadsinnhold data = new PsbSøknadsinnhold("123", "1111", "2222", søknad, null);
        final InnsynHendelse<PsbSøknadsinnhold> hendelse = new InnsynHendelse<>(ZonedDateTime.now(), data);
        final String json = JsonUtils.toString(hendelse);
        
        final InnsynHendelse<?> deserialisertHendelse = JsonUtils.fromString(json, InnsynHendelse.class);
        final String reserialisertJson = JsonUtils.toString(deserialisertHendelse);
        
        assertThat(json).isEqualTo(reserialisertJson);
    }

    private Søknad lagGyldigSøknad() {
        var ytelse = new PleiepengerSyktBarn().medBarn(new Barn().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("22211111111")));
        var søknad = new Søknad(
                SøknadId.of("lala"),
                Versjon.of("1.0.0"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("22222222222")),
                ytelse);
        return søknad;
    }
}
