package no.nav.k9.søknad.omsorgspenger.utbetaling.snf;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.aktivitet.Frilanser;
import no.nav.k9.søknad.felles.aktivitet.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.omsorgspenger.v1.OmsorgspengerUtbetaling;

/** @deprecated bytt til {@link Søknad} med {@link OmsorgspengerUtbetaling}. */
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
    @JsonAlias({"barn"}) //TODO: Fjern barn etter at avhengige prosjekter er prodsatt.
    public final List<Barn> fosterbarn;

    @JsonProperty("selvstendigNæringsdrivende")
    @Valid
    public final List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;

    @JsonProperty("frilanser")
    @Valid
    public final Frilanser frilanser;
    
    @JsonCreator
    private OmsorgspengerUtbetalingSøknad(
            @JsonProperty("søknadId") SøknadId søknadId,
            @JsonProperty("versjon") Versjon versjon,
            @JsonProperty("mottattDato") ZonedDateTime mottattDato,
            @JsonProperty("søker") Søker søker,
            @JsonProperty("fosterbarn") @JsonAlias({"barn"}) List<Barn> fosterbarn, //TODO: Fjern barn etter at avhengige prosjekter er prodsatt.
            @JsonProperty("selvstendingNæringsdrivende") List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
            @JsonProperty("frilanser") Frilanser frilanser) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.fosterbarn = (fosterbarn == null) ? List.of() : fosterbarn;
        this.selvstendigNæringsdrivende = selvstendigNæringsdrivende;
        this.frilanser = frilanser;
    }
    
    @AssertTrue(message="Enten frilanser eller selvstendingNæringsdrivende må være satt i søknaden")
    private boolean isFrilanserEllerSn() {
        return frilanser!=null || (selvstendigNæringsdrivende!=null && !selvstendigNæringsdrivende.isEmpty());
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

    public List<Barn> getBarn() {
        return fosterbarn;
    }
    
    @Size(max=0, message="${validatedValue}")
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
        
        public static OmsorgspengerUtbetalingSøknad deserialize(ObjectNode node) {
            try {
                return JsonUtils.getObjectMapper().treeToValue(node, OmsorgspengerUtbetalingSøknad.class);
            } catch (IOException e) {
                throw new IllegalArgumentException("Kunne ikke konvertere til OmsorgspengerUtbetalingSøknad.class", e);
            }
        }
    }

    @SuppressWarnings("removal")
    public static final class Builder {
        private final static OmsorgspengerUtbetalingSøknadValidator validator = new OmsorgspengerUtbetalingSøknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        List<Barn> barn;
        private List<SelvstendigNæringsdrivende> selvstendingeVirksomheter;
        private Frilanser frilanser;

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
            if (fosterbarn != null) this.barn.add(fosterbarn);
            return this;
        }

        public Builder fosterbarn(List<Barn> fosterbarn) {
            if (fosterbarn != null) this.barn.addAll(fosterbarn);
            return this;
        }

        public Builder selvstendigNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendingeVirksomheter) {
            this.selvstendingeVirksomheter = selvstendingeVirksomheter;
            return this;
        }

        public Builder frilanser(Frilanser frilanser) {
            this.frilanser = frilanser;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public OmsorgspengerUtbetalingSøknad build() {
            OmsorgspengerUtbetalingSøknad søknad;
            if (json == null) søknad = new OmsorgspengerUtbetalingSøknad(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker,
                    barn,
                    selvstendingeVirksomheter,
                    frilanser);
            else søknad = SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
