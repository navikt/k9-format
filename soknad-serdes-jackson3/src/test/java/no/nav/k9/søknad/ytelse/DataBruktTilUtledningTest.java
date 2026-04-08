package no.nav.k9.søknad.ytelse;

import no.nav.k9.søknad.JsonUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class DataBruktTilUtledningTest {

    private DataBruktTilUtledning dataBruktTilUtledning;

    @BeforeEach
    void setUp() {
        this.dataBruktTilUtledning = new DataBruktTilUtledning();
    }

    @Test
    public void testSerialiseringMedSetter() throws JSONException {

        // language=JSON
        String annetData = """
                {
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
                """.translateEscapes();


        dataBruktTilUtledning
                .medHarBekreftetOpplysninger(true)
                .medHarForståttRettigheterOgPlikter(true)
                .medSoknadDialogCommitSha("1234567890")
                .medAnnetData(annetData);

        String serialisertData = JsonUtils.toString(dataBruktTilUtledning);
        JSONAssert.assertEquals("""
                {
                  "harBekreftetOpplysninger": true,
                  "harForståttRettigheterOgPlikter": true,
                  "soknadDialogCommitSha": "1234567890",
                    "annetData" : "{\\n  \\"string\\" : \\"tekst\\",\\n  \\"tall\\" : 1000,\\n  \\"boolean\\" : true,\\n  \\"objekt\\" : {\\n    \\"string\\" : \\"tekst\\"\\n  },\\n  \\"liste\\" : [ {\\n    \\"string\\" : \\"tekst\\"\\n  } ]\\n}\\n",
                    "harBekreftetOpplysninger" : true,
                    "harForståttRettigheterOgPlikter" : true,
                    "soknadDialogCommitSha" : "1234567890"
                }
                """, serialisertData, true);
    }

    @Test
    public void testDeserialisering() throws JSONException {
        // language=JSON
        String jsonString = """
                {
                  "harBekreftetOpplysninger": true,
                  "harForståttRettigheterOgPlikter": true,
                  "soknadDialogCommitSha": "1234567890",
                  "annetData" : "{\\n  \\"string\\" : \\"tekst\\",\\n  \\"tall\\" : 1000,\\n  \\"boolean\\" : true,\\n  \\"objekt\\" : {\\n    \\"string\\" : \\"tekst\\"\\n  },\\n  \\"liste\\" : [ {\\n    \\"string\\" : \\"tekst\\"\\n  } ]\\n}\\n"
                }
                """;

        DataBruktTilUtledning deserialisertData = JsonUtils.fromString(jsonString, DataBruktTilUtledning.class);
        DataBruktTilUtledning forventetDataBruktTilUtledning = new DataBruktTilUtledning()
                .medHarBekreftetOpplysninger(true)
                .medHarForståttRettigheterOgPlikter(true)
                .medSoknadDialogCommitSha("1234567890")
                .medAnnetData(
                        // language=JSON
                        """
                        {
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
                        """
                );
        JSONAssert.assertEquals(forventetDataBruktTilUtledning.toString(), deserialisertData.toString(), true);
    }
}
