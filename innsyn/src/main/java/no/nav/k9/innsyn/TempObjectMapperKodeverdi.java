package no.nav.k9.innsyn;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import no.nav.k9.kodeverk.api.Kodeverdi;
import no.nav.k9.søknad.JsonUtils;

/**
 * nødvendig for å kunne serializere k9-sak kodeverdi objekter til string
 * kan fjernes når k9-sak har tatt i bruk @JsonValue på sine kodeobjekter
 */
public class TempObjectMapperKodeverdi {

    public static ObjectMapper getObjectMapper() {
        var om = JsonUtils.getObjectMapper().copy();
        var m = new SimpleModule();
        m.addSerializer(Kodeverdi.class, new TempKodeverdiSerializer());
        om.registerModule(m);
        return om;
    }

    private static class TempKodeverdiSerializer extends StdSerializer<Kodeverdi> {

        public TempKodeverdiSerializer(){
            super(Kodeverdi.class);
        }

        @Override
        public void serialize(Kodeverdi value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.getKode());
        }
    }

}

