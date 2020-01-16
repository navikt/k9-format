package no.nav.k9.søknad;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class JsonUtilsTest {

    @Test
    public void reserialiseringSkalAlltidGiSammeJson() {
        final String testdataverdi = "foobar" ;

        final Testdata testdata = new Testdata();
        testdata.setFelt(testdataverdi);

        final String json = JsonUtils.toString(testdata);
        assertThat(json, containsString(testdataverdi));

        final Testdata testdataDeserialisert = JsonUtils.fromString(json, Testdata.class);
        assertThat(testdataDeserialisert.getFelt(), is(testdataverdi));

        final String reserialisertJson = JsonUtils.toString(testdata);
        assertThat(reserialisertJson, is(json));
    }

    @Test
    public void ukjentePropertiesSkalGiException() {
        final Testdata testdata = new Testdata();
        final ObjectNode objectNode = JsonUtils.toObjectNode(testdata);

        final String feltnavn = "nyttFeltnavn" ;
        final String feltverdi = "nyFeltverdi" ;
        objectNode.put(feltnavn, feltverdi);

        final String json = JsonUtils.toString(objectNode);
        assertThat(json, containsString(feltnavn));
        assertThat(json, containsString(feltverdi));

        try {
            JsonUtils.fromString(json, Testdata.class);
            fail("Forventer UnrecognizedPropertyException. Bakgrunnen for dette er at k9-sak skal få feil ved behandling av ukjente properties, som igjen medfører at behandlingen forsøkes på nytt ved et senere tidspunkt (når k9-sak har blitt utvidet med støtte for det nye feltet).");
        } catch (RuntimeException e) {
            // Ignore.
        }
    }


    private static class Testdata {

        private String felt;

        public void setFelt(String felt) {
            this.felt = felt;
        }

        public String getFelt() {
            return felt;
        }
    }
}
