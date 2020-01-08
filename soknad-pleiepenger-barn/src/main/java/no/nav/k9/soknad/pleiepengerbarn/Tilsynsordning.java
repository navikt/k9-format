package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;
import no.nav.k9.soknad.felles.Periodisert;

import java.time.Duration;

public class Tilsynsordning implements Periodisert {

    public final Periode periode;
    public final Duration mandag;
    public final Duration tirsdag;
    public final Duration onsdag;
    public final Duration torsdag;
    public final Duration fredag;

    @JsonCreator
    private Tilsynsordning(
            @JsonProperty("periode")
            Periode periode,
            @JsonProperty("mandag")
            Duration mandag,
            @JsonProperty("tirsdag")
            Duration tirsdag,
            @JsonProperty("onsdag")
            Duration onsdag,
            @JsonProperty("torsdag")
            Duration torsdag,
            @JsonProperty("fredag")
            Duration fredag) {
        this.periode = periode;
        this.mandag = mandag;
        this.tirsdag = tirsdag;
        this.onsdag = onsdag;
        this.torsdag = torsdag;
        this.fredag = fredag;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    @JsonIgnore
    public Periode getPeriode() {
        return periode;
    }


    public static final class Builder {
        private Periode periode;
        private Duration mandag;
        private Duration tirsdag;
        private Duration onsdag;
        private Duration torsdag;
        private Duration fredag;

        private Builder() { }

        public Builder periode(Periode periode) {
            this.periode = periode;
            return this;
        }

        public Builder mandag(Duration mandag) {
            this.mandag = mandag;
            return this;
        }

        public Builder tirsdag(Duration tirsdag) {
            this.tirsdag = tirsdag;
            return this;
        }

        public Builder onsdag(Duration onsdag) {
            this.onsdag = onsdag;
            return this;
        }

        public Builder torsdag(Duration torsdag) {
            this.torsdag = torsdag;
            return this;
        }

        public Builder fredag(Duration fredag) {
            this.fredag = fredag;
            return this;
        }

        public Tilsynsordning build() {
            return new Tilsynsordning(
                    periode,
                    mandag,
                    tirsdag,
                    onsdag,
                    torsdag,
                    fredag
            );
        }
    }
}
