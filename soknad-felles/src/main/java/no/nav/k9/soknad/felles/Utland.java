package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Utland {

    public final Boolean harBoddIUtlandetSiste12Mnd;

    public final Boolean skalBoIUtlandetNeste12Mnd;

    public final Map<Periode, UtlandOpphold> opphold;

    @JsonCreator
    private Utland(
            @JsonProperty("harBoddIUtlandetSiste12Mnd")
            Boolean harBoddIUtlandetSiste12Mnd,
            @JsonProperty("skalBoIUtlandetNeste12Mnd")
            Boolean skalBoIUtlandetNeste12Mnd,
            @JsonProperty("opphold")
            Map<Periode, UtlandOpphold> opphold) {
        this.harBoddIUtlandetSiste12Mnd = harBoddIUtlandetSiste12Mnd;
        this.skalBoIUtlandetNeste12Mnd = skalBoIUtlandetNeste12Mnd;
        this.opphold = opphold == null ? emptyMap() : unmodifiableMap(opphold);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Boolean harBoddIUtlandetSiste12Mnd;
        private Boolean skalBoIUtlandetNeste12Mnd;
        private Map<Periode, UtlandOpphold> opphold;

        private Builder() {
            opphold = new HashMap<>();
        }

        public Builder harBoddIUtlandetSiste12Mnd(Boolean harBoddIUtlandetSiste12Mnd) {
            this.harBoddIUtlandetSiste12Mnd = harBoddIUtlandetSiste12Mnd;
            return this;
        }

        public Builder skalBoIUtlandetNeste12Mnd(Boolean skalBoIUtlandetNeste12Mnd) {
            this.skalBoIUtlandetNeste12Mnd = skalBoIUtlandetNeste12Mnd;
            return this;
        }

        public Builder opphold(Map<Periode, UtlandOpphold> opphold) {
            this.opphold.putAll(opphold);
            return this;
        }

        public Builder opphold(Periode periode, UtlandOpphold opphold) {
            this.opphold.put(periode, opphold);
            return this;
        }

        public Utland build() {
            return new Utland(
                    harBoddIUtlandetSiste12Mnd,
                    skalBoIUtlandetNeste12Mnd,
                    opphold
            );
        }
    }
}


