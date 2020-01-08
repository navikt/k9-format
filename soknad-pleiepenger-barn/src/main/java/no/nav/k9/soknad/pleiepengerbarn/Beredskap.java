package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;
import no.nav.k9.soknad.felles.Periodisert;

public class Beredskap implements Periodisert {
    public final Periode periode;

    public final String tilleggsinformasjon;

    @JsonCreator
    private Beredskap(
            @JsonProperty("periode")
            Periode periode,
            @JsonProperty("tilleggsinformasjon")
            String tilleggsinformasjon) {
        this.periode = periode;
        this.tilleggsinformasjon = tilleggsinformasjon;
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
        private String tilleggsinformasjon;

        private Builder() { }

        public Builder tilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = tilleggsinformasjon;
            return this;
        }

        public Builder periode(Periode periode) {
            this.periode = periode;
            return this;
        }

        public Beredskap build() {
            return new Beredskap(
                    periode,
                    tilleggsinformasjon
            );
        }
    }
}
