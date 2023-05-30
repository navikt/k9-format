package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;

import javax.validation.Valid;

public class UkjentArbeidsforhold {
    @JsonProperty(value = "organisasjonsnummer", required = true)
    @Valid
    private Organisasjonsnummer organisasjonsnummer;

    @JsonProperty(value = "organisasjonsnavn")
    @Valid
    private String organisasjonsnavn; // brukes ikke ved saksbehandling

    @JsonProperty(value = "erAnsatt", required = true)
    @Valid
    private Boolean erAnsatt;

    @JsonProperty(value = "normalarbeidstid")
    @Valid
    private NormalArbeidstid normalarbeidstid;

    @JsonProperty(value = "arbeiderIPerioden")
    @Valid
    private ArbeiderIPeriodenSvar arbeiderIPerioden;

    public UkjentArbeidsforhold(
            @JsonProperty(value = "organisasjonsnummer", required = true) @Valid Organisasjonsnummer organisasjonsnummer,
            @JsonProperty(value = "organisasjonsnavn", required = true) @Valid String organisasjonsnavn,
            @JsonProperty(value = "erAnsatt", required = true) @Valid Boolean erAnsatt,
            @JsonProperty(value = "normalarbeidstid") @Valid NormalArbeidstid normalarbeidstid,
            @JsonProperty(value = "arbeiderIPerioden") @Valid ArbeiderIPeriodenSvar arbeiderIPerioden
    ) {
        this.organisasjonsnummer = organisasjonsnummer;
        this.organisasjonsnavn = organisasjonsnavn;
        this.erAnsatt = erAnsatt;
        this.normalarbeidstid = normalarbeidstid;
        this.arbeiderIPerioden = arbeiderIPerioden;
    }

    public UkjentArbeidsforhold() {
    }

    public Organisasjonsnummer getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public UkjentArbeidsforhold medOrganisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
        this.organisasjonsnummer = organisasjonsnummer;
        return this;
    }

    public UkjentArbeidsforhold medOrganisasjonsnavn(String organisasjonsnavn) {
        this.organisasjonsnavn = organisasjonsnavn;
        return this;
    }

    public String getOrganisasjonsnavn() {
        return organisasjonsnavn;
    }

    public boolean isErAnsatt() {
        return erAnsatt;
    }

    public UkjentArbeidsforhold medErAnsatt(Boolean erAnsatt) {
        this.erAnsatt = erAnsatt;
        return this;
    }

    public NormalArbeidstid getNormalarbeidstid() {
        return normalarbeidstid;
    }

    public UkjentArbeidsforhold medNormalarbeidstid(NormalArbeidstid normalarbeidstid) {
        this.normalarbeidstid = normalarbeidstid;
        return this;
    }

    public ArbeiderIPeriodenSvar getArbeiderIPerioden() {
        return arbeiderIPerioden;
    }

    public UkjentArbeidsforhold medArbeiderIPerioden(ArbeiderIPeriodenSvar arbeiderIPerioden) {
        this.arbeiderIPerioden = arbeiderIPerioden;
        return this;
    }
}
