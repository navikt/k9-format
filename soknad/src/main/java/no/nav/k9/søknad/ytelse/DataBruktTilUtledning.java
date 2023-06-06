package no.nav.k9.søknad.ytelse;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.JsonUtils;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Klassen representerer tilleggsdata som har verdi som dokumentasjon.
 * Disse dataene blir bevart for nåværende og fremtidige formål og er ment for sikker oppbevaring i NAVs arkivsystem.</p>
 *
 * <p>Disse feltene spiller en avgjørende rolle i å sikre at dataene er tilgjengelige, gjenfinnbare og pålitelige, samtidig som de beholder sin bevisverdi.
 * Det brukes primært til å lagre data som genereres eller håndteres gjennom Dialog- og samhandlingssystemer.</p>
 *
 * <p>Vær oppmerksom på at all data som lagres her, skal behandles som arkivdokumentasjon, og bør derfor håndteres med tilsvarende forsiktighet og sikkerhetsprosedyrer.</p>
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class DataBruktTilUtledning {

    /**
     * Brukes for å bekrefte at bruker har forstått rettigheter og plikter.
     */
    @JsonProperty(value = "harForståttRettigheterOgPlikter", required = true)
    @Valid
    @AssertTrue(message = "Må ha forstått rettigheter og plikter må være true")
    private Boolean harForståttRettigheterOgPlikter;

    /**
     * Brukes for å bekrefte at bruker har bekreftet opplysninger.
     */
    @JsonProperty(value = "harBekreftetOpplysninger", required = true)
    @Valid
    @AssertTrue(message = "Må ha bekreftet opplysninger må være true")
    private Boolean harBekreftetOpplysninger;


    /**
     * Brukes for å kunne spore opprinnelig commit SHA fra søknadsdialog.
     */
    @JsonProperty(value = "soknadDialogCommitSha")
    @Valid
    private String soknadDialogCommitSha;


    /**
     * <p>Feltet "annetData" representerer tilleggsdata som ikke er direkte spesifisert
     * av de andre feltene i denne klassen. Dette kan inkludere alle supplerende
     * eller hjelpedata som er knyttet til en instans av denne klassen.</p>
     *
     * <p>Det brukes primært til å lagre avledet data generert under søknadsdialogprosessen.
     * Formålet med dette feltet er ikke å påvirke klassens primære funksjon, men å assistere
     * i å gi ekstra kontekst eller metadata der det er nødvendig.</p>
     *
     * <p>Merk at denne dataen skal brukes med forsiktighet, ettersom den ikke er direkte kontrollert
     * av klassens struktur og ikke er underlagt de samme valideringene eller sjekkene som andre felt.</p>
     */
    @JsonProperty("annetData")
    private Map<String, Object> annetData;

    public DataBruktTilUtledning() {
        this.annetData = new HashMap<>();
    }

    public Boolean getHarForståttRettigheterOgPlikter() {
        return harForståttRettigheterOgPlikter;
    }

    public DataBruktTilUtledning medHarForståttRettigheterOgPlikter(Boolean harForståttRettigheterOgPlikter) {
        this.harForståttRettigheterOgPlikter = harForståttRettigheterOgPlikter;
        return this;
    }

    public Boolean getHarBekreftetOpplysninger() {
        return harBekreftetOpplysninger;
    }

    public DataBruktTilUtledning medHarBekreftetOpplysninger(Boolean harBekreftetOpplysninger) {
        this.harBekreftetOpplysninger = harBekreftetOpplysninger;
        return this;
    }

    public String getSoknadDialogCommitSha() {
        return this.soknadDialogCommitSha;
    }

    public DataBruktTilUtledning medSoknadDialogCommitSha(String soknadDialogCommitSha) {
        this.soknadDialogCommitSha = soknadDialogCommitSha;
        return this;
    }

    public Map<String, Object> getAnnetData() {
        return this.annetData;
    }

    public DataBruktTilUtledning setAnnetData(Map<String, Object> annetData) {
        this.annetData = annetData;
        return this;
    }

    public DataBruktTilUtledning medData(String key, Object value) {
        this.annetData.put(key, value);
        return this;
    }

    /**
     * Returnerer en JSON-representasjon av denne klassen eller toString fra superklassen.
     * Hvis serialisering feiler, returneres toString fra superklassen.
     * @return JSON-representasjon av denne klassen.
     */
    @Override
    public String toString() {
        try {
            return JsonUtils.toString(this);
        } catch (Exception e) {
            // hvis serialisering feiler, returner toString fra superklassen.
            return super.toString();
        }
    }
}
