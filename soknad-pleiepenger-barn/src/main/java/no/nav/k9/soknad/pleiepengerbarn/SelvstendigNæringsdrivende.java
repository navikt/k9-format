package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class SelvstendigNæringsdrivende {

    public final Map<Periode, Arbeidsforhold> arbeidsforhold;

    @JsonCreator
    private SelvstendigNæringsdrivende(
            @JsonProperty("arbeidsforhold")
            Map<Periode, Arbeidsforhold> arbeidsforhold) {
        this.arbeidsforhold = arbeidsforhold == null ? emptyMap() : unmodifiableMap(arbeidsforhold);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, Arbeidsforhold> arbeidsforhold;

        private Builder() {
            arbeidsforhold = new HashMap<>();
        }

        public Builder arbeidsforhold(Map<Periode, Arbeidsforhold> arbeidsforhold) {
            this.arbeidsforhold.putAll(arbeidsforhold);
            return this;
        }

        public Builder arbeidsforhold(Periode periode, Arbeidsforhold arbeidsforhold) {
            this.arbeidsforhold.put(periode, arbeidsforhold);
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(
                    arbeidsforhold
            );
        }
    }

    public static final class Arbeidsforhold {

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Builder() {}

            public Arbeidsforhold build() {
                return new Arbeidsforhold();
            }
        }

    }
}
