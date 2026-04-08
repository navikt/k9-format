package no.nav.k9.søknad.ytelse;

import no.nav.k9.søknad.JsonUtils;

public class DateBruktTilUtledningJackson2 extends DataBruktTilUtledning {

    @Override
    protected String toJsonString() {
        try {
            return JsonUtils.toString(this);
        } catch (Exception e) {
            // hvis serialisering feiler, returner toString fra superklassen.
            return super.toString();
        }
    }
}
