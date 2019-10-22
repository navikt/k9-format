package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class PleiepengerBarnSoknadTest {

    @Test
    public void nyePaakrevdeFeltSkalHaStandardverdi() throws Exception {
        final PleiepengerBarnSoknad soknadFraTomJson = JsonUtils.fromString("{}", PleiepengerBarnSoknad.class);
        final String reserialisertJson = JsonUtils.toString(soknadFraTomJson);

        final PleiepengerBarnSoknad soknad = new PleiepengerBarnSoknad();
        final String json = JsonUtils.toString(soknad);

        assertThat("Nye påkrevde felt kan kun legges til hvis en standardverdi er definert. Dette for å sikre bakoverkompatibilitet ved innlesing av gamle søknader.",
                reserialisertJson,
                is(json));
    }

    @Test
    public void sjekkAtKomplettSoknadKanReserialiseres() throws Exception {
        final String json = jsonFromFile("komplett-soknad.json");
        final PleiepengerBarnSoknad soknad = JsonUtils.fromString(json, PleiepengerBarnSoknad.class);
        final String jsonReserialisert = JsonUtils.toString(soknad);
        assertThat(jsonReserialisert, is(json));
    }

    @Test
    public void verifiserAtMinimalSoknadKanReserialiseres() throws Exception {
        final PleiepengerBarnSoknad soknad = new PleiepengerBarnSoknad();
        final String json = JsonUtils.toString(soknad);

        final PleiepengerBarnSoknad soknadDeserialisert = JsonUtils.fromString(json, PleiepengerBarnSoknad.class);
        final String reserialisertJson = JsonUtils.toString(soknadDeserialisert);

        assertThat(reserialisertJson, is(json));
    }


    private static final String jsonFromFile(String filename) throws IOException {
        return Files.readString(Path.of("src/test/resources/" + filename));
    }
}
