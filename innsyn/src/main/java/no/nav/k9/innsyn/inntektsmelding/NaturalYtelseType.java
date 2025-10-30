package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

public enum NaturalYtelseType {

    ELEKTRISK_KOMMUNIKASJON("ELEKTRISK_KOMMUNIKASJON", "Elektrisk kommunikasjon", "elektroniskKommunikasjon"),
    AKSJER_GRUNNFONDSBEVIS_TIL_UNDERKURS("AKSJER_UNDERKURS", "Aksjer grunnfondsbevis til underkurs", "aksjerGrunnfondsbevisTilUnderkurs"),
    LOSJI("LOSJI", "Losji", "losji"),
    KOST_DØGN("KOST_DOEGN", "Kostpenger døgnsats", "kostDoegn"),
    BESØKSREISER_HJEMMET_ANNET("BESOEKSREISER_HJEM", "Besøksreiser hjemmet annet", "besoeksreiserHjemmetAnnet"),
    KOSTBESPARELSE_I_HJEMMET("KOSTBESPARELSE_HJEM", "Kostbesparelser i hjemmet", "kostbesparelseIHjemmet"),
    RENTEFORDEL_LÅN("RENTEFORDEL_LAAN", "Rentefordel lån", "rentefordelLaan"),
    BIL("BIL", "Bil", "bil"),
    KOST_DAGER("KOST_DAGER", "Kostpenger dager", "kostDager"),
    BOLIG("BOLIG", "Bolig", "bolig"),
    SKATTEPLIKTIG_DEL_FORSIKRINGER("FORSIKRINGER", "Skattepliktig del forsikringer", "skattepliktigDelForsikringer"),
    FRI_TRANSPORT("FRI_TRANSPORT", "Fri transport", "friTransport"),
    OPSJONER("OPSJONER", "Opsjoner", "opsjoner"),
    TILSKUDD_BARNEHAGEPLASS("TILSKUDD_BARNEHAGE", "Tilskudd barnehageplass", "tilskuddBarnehageplass"),
    ANNET("ANNET", "Annet", "annet"),
    BEDRIFTSBARNEHAGEPLASS("BEDRIFTSBARNEHAGE", "Bedriftsbarnehageplass", "bedriftsbarnehageplass"),
    YRKEBIL_TJENESTLIGBEHOV_KILOMETER("YRKESBIL_KILOMETER", "Yrkesbil tjenesteligbehov kilometer", "yrkebilTjenestligbehovKilometer"),
    YRKEBIL_TJENESTLIGBEHOV_LISTEPRIS("YRKESBIL_LISTEPRIS", "Yrkesbil tjenesteligbehov listepris", "yrkebilTjenestligbehovListepris"),
    INNBETALING_TIL_UTENLANDSK_PENSJONSORDNING("UTENLANDSK_PENSJONSORDNING", "Innbetaling utenlandsk pensjonsordning",
            "innbetalingTilUtenlandskPensjonsordning"),
    UDEFINERT("-", "Ikke definert", null),
    ;

    private static final Map<String, NaturalYtelseType> KODER = new LinkedHashMap<>();

    static {
        for (var v : values()) {
            if (KODER.putIfAbsent(v.kode, v) != null) {
                throw new IllegalArgumentException("Duplikat : " + v.kode);
            }
        }
    }

    @JsonIgnore
    private String navn;

    @JsonValue
    private String kode;

    @JsonIgnore
    private String offisiellKode;

    NaturalYtelseType(String kode, String navn, String offisiellKode) {
        this.kode = kode;
        this.navn = navn;
        this.offisiellKode = offisiellKode;
    }

    @JsonCreator
    public static NaturalYtelseType fraKode(String kode) {
        if (kode == null) {
            return null;
        }
        var ad = KODER.get(kode);
        if (ad == null) {
            throw new IllegalArgumentException("Ukjent NaturalYtelseType: " + kode);
        }
        return ad;
    }
}
