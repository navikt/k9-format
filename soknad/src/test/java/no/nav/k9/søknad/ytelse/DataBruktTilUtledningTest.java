package no.nav.k9.søknad.ytelse;

import no.nav.k9.søknad.JsonUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DataBruktTilUtledningTest {

    private DataBruktTilUtledning dataBruktTilUtledning;

    @BeforeEach
    void setUp() {
        this.dataBruktTilUtledning = new DataBruktTilUtledning();
    }

    @Test
    public void testSerialiseringMedSetter() throws JSONException {
        Map<String, Object> data = new HashMap<>();
        data.put("string", "tekst");
        data.put("tall", 1000);
        data.put("boolean", true);
        data.put("objekt", Map.of("string", "tekst"));
        data.put("liste", List.of(Map.of("string", "tekst")));
        dataBruktTilUtledning
                .medHarBekreftetOpplysninger(true)
                .medHarForståttRettigheterOgPlikter(true)
                .medSoknadDialogCommitSha("1234567890")
                .setAnnetData(data);

        String serialisertData = JsonUtils.toString(dataBruktTilUtledning);
        JSONAssert.assertEquals("""
                {
                  "harBekreftetOpplysninger": true,
                  "harForståttRettigheterOgPlikter": true,
                  "soknadDialogCommitSha": "1234567890",
                  "annetData" : {
                    "string" : "tekst",
                    "tall" : 1000,
                    "boolean" : true,
                    "objekt" : {
                      "string" : "tekst"
                    },
                    "liste" : [ {
                      "string" : "tekst"
                    } ]
                  }
                }
                """, serialisertData, true);
    }

    @Test
    public void testSerialiseringMedData() throws JSONException {
        dataBruktTilUtledning
                .medHarBekreftetOpplysninger(true)
                .medHarForståttRettigheterOgPlikter(true)
                .medSoknadDialogCommitSha("1234567890")
                .medData("string", "tekst")
                .medData("tall", 1000)
                .medData("boolean", true)
                .medData("objekt", Map.of("string", "tekst"))
                .medData("liste", List.of(Map.of("string", "tekst")));

        String serialisertData = JsonUtils.toString(dataBruktTilUtledning);
        JSONAssert.assertEquals("""
                {
                  "harBekreftetOpplysninger": true,
                  "harForståttRettigheterOgPlikter": true,
                  "soknadDialogCommitSha": "1234567890",
                  "annetData" : {
                    "string" : "tekst",
                    "tall" : 1000,
                    "boolean" : true,
                    "objekt" : {
                      "string" : "tekst"
                    },
                    "liste" : [ {
                      "string" : "tekst"
                    } ]
                  }
                }
                """, serialisertData, true);
    }

    @Test
    public void testDeserialisering() throws JSONException {
        String jsonString = """
                {
                  "harBekreftetOpplysninger": true,
                  "harForståttRettigheterOgPlikter": true,
                  "soknadDialogCommitSha": "1234567890",
                  "annetData" : {
                    "string" : "tekst",
                    "tall" : 1000,
                    "boolean" : true,
                    "objekt" : {
                      "string" : "tekst"
                    },
                    "liste" : [ {
                      "string" : "tekst"
                    } ]
                  }
                }
                """;

        DataBruktTilUtledning deserialisertData = JsonUtils.fromString(jsonString, DataBruktTilUtledning.class);
        DataBruktTilUtledning forventetDataBruktTilUtledning = new DataBruktTilUtledning()
                .medHarBekreftetOpplysninger(true)
                .medHarForståttRettigheterOgPlikter(true)
                .medSoknadDialogCommitSha("1234567890")
                .setAnnetData(Map.of(
                "string", "tekst",
                "tall", 1000,
                "boolean", true,
                "objekt", Map.of("string", "tekst"),
                "liste", List.of(Map.of("string", "tekst"))
        ));
        JSONAssert.assertEquals(forventetDataBruktTilUtledning.toString(), deserialisertData.toString(), true);
    }
}
