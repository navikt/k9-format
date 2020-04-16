package no.nav.k9.søknad.omsorgspenger.utbetaling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class OmsorgspengerUtbetalingSøknad {
    public final SøknadId søknadId;

    public final Versjon versjon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Søker søker;

    public final List<Barn> barn;

    public final List<SelvstendigNæringsdrivende> selvstendingNæringsdrivende;
    public final Frilanser frilanser;

    @JsonCreator
    private OmsorgspengerUtbetalingSøknad(
            @JsonProperty("søknadId")
                    SøknadId søknadId,
            @JsonProperty("versjon")
                    Versjon versjon,
            @JsonProperty("mottattDato")
                    ZonedDateTime mottattDato,
            @JsonProperty("søker")
                    Søker søker,
            @JsonProperty("barn")
                    List<Barn> barn,
            @JsonProperty("selvstendingNæringsdrivende")
                    List<SelvstendigNæringsdrivende> selvstendingNæringsdrivende,
            @JsonProperty("frilanser") Frilanser frilanser) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.barn = (barn == null) ? List.of() : barn;
        this.selvstendingNæringsdrivende = selvstendingNæringsdrivende;
        this.frilanser = frilanser;
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

        public Builder barn(Barn barn) {
            if (barn != null) this.barn.add(barn);
            return this;
        }

        public Builder barn(List<Barn> barn) {
            if (barn != null) this.barn.addAll(barn);
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
            OmsorgspengerUtbetalingSøknad søknad = (json == null) ? new OmsorgspengerUtbetalingSøknad(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker,
                    barn,
                    selvstendingeVirksomheter,
                    frilanser) : SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
