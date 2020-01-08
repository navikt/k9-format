package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Utland {

    public final Boolean harBoddIUtlandetSiste12Mnd;

    public final Boolean skalBoIUtlandetNeste12Mnd;

    public final List<Opphold> opphold;

    @JsonCreator
    private Utland(
            @JsonProperty("harBoddIUtlandetSiste12Mnd")
            Boolean harBoddIUtlandetSiste12Mnd,
            @JsonProperty("skalBoIUtlandetNeste12Mnd")
            Boolean skalBoIUtlandetNeste12Mnd,
            @JsonProperty("opphold")
            List<Opphold> opphold) {
        this.harBoddIUtlandetSiste12Mnd = harBoddIUtlandetSiste12Mnd;
        this.skalBoIUtlandetNeste12Mnd = skalBoIUtlandetNeste12Mnd;
        opphold.removeIf(Objects::isNull);
        this.opphold = Collections.unmodifiableList(opphold);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Boolean harBoddIUtlandetSiste12Mnd;
        private Boolean skalBoIUtlandetNeste12Mnd;
        private List<Opphold> opphold;

        private Builder() {
            opphold = new ArrayList<>();
        }

        public Builder harBoddIUtlandetSiste12Mnd(Boolean harBoddIUtlandetSiste12Mnd) {
            this.harBoddIUtlandetSiste12Mnd = harBoddIUtlandetSiste12Mnd;
            return this;
        }

        public Builder skalBoIUtlandetNeste12Mnd(Boolean skalBoIUtlandetNeste12Mnd) {
            this.skalBoIUtlandetNeste12Mnd = skalBoIUtlandetNeste12Mnd;
            return this;
        }

        public Builder opphold(List<Opphold> opphold) {
            this.opphold.addAll(opphold);
            return this;
        }

        public Builder opphold(Opphold opphold) {
            this.opphold.add(opphold);
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


