package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class PleiepengerBarnSoknadTest {

    @Test
    public void nyePaakrevdeFeltSkalHaStandardverdi() {
        final PleiepengerBarnSoknad soknadFraTomJson = JsonUtils.fromString("{}", PleiepengerBarnSoknad.class);
        final String reserialisertJson = JsonUtils.toString(soknadFraTomJson);

        final PleiepengerBarnSoknad soknad = new PleiepengerBarnSoknad();
        final String json = JsonUtils.toString(soknad);

        assertThat("Nye påkrevde felt kan kun legges til hvis en standardverdi er definert. Dette for å sikre bakoverkompatibilitet ved innlesing av gamle søknader.",
                reserialisertJson,
                is(json));
    }

    @Test
    public void sjekkAtKomplettSoknadKanReserialiseres() {
        final String json = TestUtils.jsonForKomplettSoknad();
        final PleiepengerBarnSoknad soknad = JsonUtils.fromString(json, PleiepengerBarnSoknad.class);
        final String jsonReserialisert = JsonUtils.toString(soknad);
        assertThat(jsonReserialisert, is(json));
    }

    @Test
    public void verifiserAtMinimalSoknadKanReserialiseres() {
        final PleiepengerBarnSoknad soknad = new PleiepengerBarnSoknad();
        final String json = JsonUtils.toString(soknad);

        final PleiepengerBarnSoknad soknadDeserialisert = JsonUtils.fromString(json, PleiepengerBarnSoknad.class);
        final String reserialisertJson = JsonUtils.toString(soknadDeserialisert);

        assertThat(reserialisertJson, is(json));
    }



}
