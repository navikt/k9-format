package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.UtlandBosted;
import no.nav.k9.søknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Utland {

    public final Map<Periode, UtlandBosted> bosteder;

    public final Map<Periode, UtlandOpphold> opphold;

    @JsonCreator
    private Utland(
            @JsonProperty("bosteder")
            Map<Periode, UtlandBosted> bosteder,
            @JsonProperty("opphold")
            Map<Periode, UtlandOpphold> opphold) {
        this.bosteder = (bosteder == null) ? emptyMap() : unmodifiableMap(bosteder);
        this.opphold = (opphold == null) ? emptyMap() : unmodifiableMap(opphold);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, UtlandBosted> bosteder;
        private Map<Periode, UtlandOpphold> opphold;

        private Builder() {
            bosteder = new HashMap<>();
            opphold = new HashMap<>();
        }

        public Builder opphold(Map<Periode, UtlandOpphold> opphold) {
            this.opphold.putAll(opphold);
            return this;
        }

        public Builder opphold(Periode periode, UtlandOpphold opphold) {
            this.opphold.put(periode, opphold);
            return this;
        }

        public Builder bosted(Map<Periode, UtlandBosted> bosteder) {
            this.bosteder.putAll(bosteder);
            return this;
        }

        public Builder bosted(Periode periode, UtlandBosted bosted) {
            this.bosteder.put(periode, bosted);
            return this;
        }

        public Utland build() {
            return new Utland(
                    bosteder,
                    opphold
            );
        }
    }
}


