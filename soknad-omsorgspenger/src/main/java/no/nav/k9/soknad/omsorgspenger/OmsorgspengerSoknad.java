package no.nav.k9.soknad.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class OmsorgspengerSoknad {

    @JsonProperty(required = true)
    private String versjon = "0.1.1";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private ZonedDateTime mottattDato;


}
