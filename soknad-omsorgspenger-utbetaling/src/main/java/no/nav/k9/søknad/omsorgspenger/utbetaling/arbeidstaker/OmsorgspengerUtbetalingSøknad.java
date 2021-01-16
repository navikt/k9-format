package no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.omsorgspenger.v1.OmsorgspengerUtbetaling;

/** @deprecated bytt til {@link OmsorgspengerUtbetaling}. */
@Deprecated(forRemoval = true, since = "5.0.1")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OmsorgspengerUtbetalingSøknad implements Innsending {

    @JsonProperty(value = "søknadId", required = true)
    @Valid
    @NotNull
    public final SøknadId søknadId;

    @JsonProperty(value = "versjon", required = true)
    @Valid
    @NotNull
    public final Versjon versjon;

    @JsonProperty(value = "mottattDato", required = true)
    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    @JsonProperty(value = "søker", required = true)
    @Valid
    @NotNull
    public final Søker søker;

    @JsonProperty("fosterbarn")
    public final List<Barn> fosterbarn;

    @JsonCreator
    private OmsorgspengerUtbetalingSøknad(
                                          @JsonProperty("søknadId") SøknadId søknadId,
                                          @JsonProperty("versjon") Versjon versjon,
                                          @JsonProperty("mottattDato") ZonedDateTime mottattDato,
                                          @JsonProperty("søker") Søker søker,
                                          @JsonProperty("fosterbarn") List<Barn> fosterbarn) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.fosterbarn = (fosterbarn == null) ? List.of() : fosterbarn;

    }

    @Override
    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }
    
    @Override
    public Søker getSøker() {
        return søker;
    }
    
    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }
    
    @Size(max=0)
    private List<Feil> getValiderAngittFosterbarn() {
        var barn = this.fosterbarn;
        if (barn == null || barn.isEmpty())
            return List.of();
        var index = 0;
        List<Feil> feil = new ArrayList<>();
        for (Barn b : barn) {
            if (b.norskIdentitetsnummer == null && b.fødselsdato == null) {
                feil.add(new Feil("fosterbarn[" + index + "]", "norskIdentitetsnummerEllerFødselsdatoPåkrevd",
                    "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            } else if (b.norskIdentitetsnummer != null && b.fødselsdato != null) {
                feil.add(
                    new Feil("fosterbarn[" + index + "]", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            }
            index++;
        }
        return feil;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(OmsorgspengerUtbetalingSøknad søknad) {
            return JsonUtils.toString(søknad);
        }

        public static OmsorgspengerUtbetalingSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, OmsorgspengerUtbetalingSøknad.class);
        }
    }

    public static final class Builder {
        private final static OmsorgspengerUtbetalingSøknadValidator validator = new OmsorgspengerUtbetalingSøknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        List<Barn> barn;

        private Builder() {
            barn = new ArrayList<>();
        }

        public Builder søknadId(SøknadId søknadId) {
            this.søknadId = søknadId;
            return this;
        }

        public Builder mottattDato(ZonedDateTime mottattDato) {
            this.mottattDato = mottattDato;
            return this;
        }

        public Builder søker(Søker søker) {
            this.søker = søker;
            return this;
        }

        public Builder fosterbarn(Barn fosterbarn) {
            if (fosterbarn != null)
                this.barn.add(fosterbarn);
            return this;
        }

        public Builder fosterbarn(List<Barn> fosterbarn) {
            if (fosterbarn != null)
                this.barn.addAll(fosterbarn);
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public OmsorgspengerUtbetalingSøknad build() {
            OmsorgspengerUtbetalingSøknad søknad;
            if (json == null)
                søknad = new OmsorgspengerUtbetalingSøknad(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker,
                    barn);
            else
                søknad = SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
