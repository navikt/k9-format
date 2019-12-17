package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import no.nav.k9.soknad.felles.Opphold;
import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.skyscreamer.jsonassert.JSONAssert;

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
    public void sjekkAtKomplettSoknadKanReserialiseres() throws JSONException {
        final String json = TestUtils.jsonForKomplettSoknad();
        final PleiepengerBarnSoknad soknad = JsonUtils.fromString(json, PleiepengerBarnSoknad.class);
        final String jsonReserialisert = JsonUtils.toString(soknad);
        JSONAssert.assertEquals(jsonReserialisert, json, true);
        //assertThat(jsonReserialisert, is(json));
    }

    @Test
    public void verifiserAtMinimalSoknadKanReserialiseres() {
        final PleiepengerBarnSoknad soknad = new PleiepengerBarnSoknad();
        final String json = JsonUtils.toString(soknad);

        final PleiepengerBarnSoknad soknadDeserialisert = JsonUtils.fromString(json, PleiepengerBarnSoknad.class);
        final String reserialisertJson = JsonUtils.toString(soknadDeserialisert);

        assertThat(reserialisertJson, is(json));
    }

    @Test
    public void sjekkAtOppholdIkkeKanVaereNull() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();
        try {
            final List<Opphold> opphold = new ArrayList<>();
            opphold.add(null);
            soknad.getMedlemskap().setOpphold(opphold);
            fail("Forventet IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }


}
