package no.nav.k9.søknad.ytelse;

import no.nav.k9.søknad.JsonUtils;

public class DataBruktTilUtledningJsonSerializerJackson2 implements DataBruktTilUtledningJsonSerializer {

    @Override
    public String toJsonString(DataBruktTilUtledning data) {
        try {
            return JsonUtils.toString(data);
        } catch (Exception e) {
            throw new IllegalArgumentException("Serialisering til json feilet", e);
        }
    }
}
