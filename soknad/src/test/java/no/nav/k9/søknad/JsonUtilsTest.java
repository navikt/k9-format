package no.nav.k9.søknad;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtilsTest {

    @Test
    public void reserialiseringSkalAlltidGiSammeJson() {
        final String testdataverdi = "foobar" ;

        final Testdata testdata = new Testdata();
        testdata.setFelt(testdataverdi);

        final String json = JsonUtils.toString(testdata);
        assertThat(json).contains(testdataverdi);

        final Testdata testdataDeserialisert = JsonUtils.fromString(json, Testdata.class);
        assertThat(testdataDeserialisert.getFelt()).isEqualTo(testdataverdi);

        final String reserialisertJson = JsonUtils.toString(testdata);
        assertThat(reserialisertJson).isEqualTo(json);
    }

    @Test
    public void ukjentePropertiesSkalGiException() {
        final Testdata testdata = new Testdata();
        final ObjectNode objectNode = JsonUtils.toObjectNode(testdata);

        final String feltnavn = "nyttFeltnavn" ;
        final String feltverdi = "nyFeltverdi" ;
        objectNode.put(feltnavn, feltverdi);

        final String json = JsonUtils.toString(objectNode);
        assertThat(json).contains(feltnavn);
        assertThat(json).contains(feltverdi);

        try {
            JsonUtils.fromString(json, Testdata.class);
            Assertions.fail("Forventer UnrecognizedPropertyException. Bakgrunnen for dette er at k9-sak skal få feil ved behandling av ukjente properties, som igjen medfører at behandlingen forsøkes på nytt ved et senere tidspunkt (når k9-sak har blitt utvidet med støtte for det nye feltet).");
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
