package no.nav.k9.søknad.ytelse.ung.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.DtoKonstanter;
import no.nav.k9.søknad.felles.Kildesystem;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.*;
import no.nav.k9.søknad.ytelse.Ytelse;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

public class UngdomsytelseInntektrapportering implements Innsending {

    private UngSøknadstype søknadType = UngSøknadstype.RAPPORTERING_SØKNAD;

    @Valid
    @NotNull
    @JsonProperty(value = "søknadId", required = true)
    private SøknadId søknadId;

    @Valid
    @NotNull
    @JsonProperty(value = "versjon", required = true)
    private Versjon versjon;

    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoKonstanter.DATO_TID_FORMAT, timezone = DtoKonstanter.TIDSSONE)
    @JsonProperty(value = "mottattDato", required = true)
    private ZonedDateTime mottattDato;

    @Valid
    @NotNull
    @JsonProperty(value = "søker", required = true)
    private Søker søker;

    @Valid
    @JsonProperty(value = "språk", required = false)
    private Språk språk = Språk.NORSK_BOKMÅL;

    @JsonManagedReference
    @Valid
    @NotNull
    @JsonProperty(value = "ytelse", required = true)
    private OppgittInntekt inntekter;

    @Valid
    @JsonProperty(value = "kildesystem", required = true)
    @NotNull
    private Kildesystem kildesystem;



    @JsonCreator
    public UngdomsytelseInntektrapportering(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                  @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                  @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                  @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                  @JsonProperty(value = "språk", required = false) @Valid Språk språk,
                  @JsonProperty(value = "inntekter", required = true) @Valid @NotNull OppgittInntekt inntekter) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
        this.versjon = Objects.requireNonNull(versjon, "versjon");
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
        this.søker = Objects.requireNonNull(søker, "søker");
        this.inntekter = Objects.requireNonNull(inntekter, "inntekter");
        this.språk = språk;
    }

    public UngdomsytelseInntektrapportering(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                  @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                  @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                  @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                  @JsonProperty(value = "inntekter", required = true) @Valid @NotNull OppgittInntekt inntekter) {
        this(søknadId, versjon, mottattDato, søker, Språk.NORSK_BOKMÅL, inntekter);
    }

    public UngSøknadstype getSøknadType() {
        return søknadType;
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }

    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    @Override
    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    public Språk getSpråk() {
        return språk;
    }

    @Override
    public Søker getSøker() {
        return søker;
    }

    @SuppressWarnings("unchecked")
    public <Y extends Ytelse> Y getInntekter() {
        return (Y) inntekter;
    }

    public Kildesystem getKildesystem() {
        return kildesystem;
    }

    public void setSøknadId(SøknadId søknadId) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
    }

    public void setVersjon(Versjon versjon) {
        this.versjon = Objects.requireNonNull(versjon);;
    }

    public void setMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
    }

    public void setSøker(Søker søker) {
        this.søker = Objects.requireNonNull(søker, "søker");
    }

    public void setInntekter(OppgittInntekt inntekter) {
        this.inntekter = Objects.requireNonNull(inntekter, "inntekter");
    }

    public void setKildesystem(Kildesystem kildesystem) {
        this.kildesystem = kildesystem;
    }

    public UngdomsytelseInntektrapportering medMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
        return this;
    }

    public UngdomsytelseInntektrapportering medSpråk(Språk språk) {
        this.språk = språk;
        return this;
    }

    public UngdomsytelseInntektrapportering medSøknadId(String søknadId) {
        this.søknadId = new SøknadId(Objects.requireNonNull(søknadId, "søknadId"));
        return this;
    }

    public UngdomsytelseInntektrapportering medSøknadId(SøknadId søknadId) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
        return this;
    }

    public UngdomsytelseInntektrapportering medVersjon(String versjon) {
        this.versjon = new Versjon(Objects.requireNonNull(versjon, "versjon"));
        return this;
    }

    public UngdomsytelseInntektrapportering medVersjon(Versjon versjon) {
        this.versjon = Objects.requireNonNull(versjon, "versjon");
        return this;
    }

    public UngdomsytelseInntektrapportering medSøker(Søker søker) {
        this.søker = Objects.requireNonNull(søker, "søker");
        return this;
    }

    public UngdomsytelseInntektrapportering medInntekter(OppgittInntekt inntekter) {
        this.inntekter = Objects.requireNonNull(inntekter, "inntekter");
        return this;
    }

    public UngdomsytelseInntektrapportering medKildesystem(Kildesystem kildesystem) {
        this.kildesystem = kildesystem;
        return this;
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(Søknad søknad) {
            return JsonUtils.toString(søknad);
        }

        public static Søknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, Søknad.class);
        }

        public static Søknad deserialize(ObjectNode node) {
            try {
                return JsonUtils.getObjectMapper().treeToValue(node, Søknad.class);
            } catch (IOException e) {
                throw new IllegalArgumentException("Kunne ikke konvertere til Søknad.class", e);
            }
        }

    }
}
