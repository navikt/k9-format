package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Organisasjonsnummer;
import no.nav.k9.soknad.felles.Periode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Arbeidstaker {
    public final Organisasjonsnummer organisasjonsnummer;
    public final Map<Periode, Arbeidsforhold> arbeidsforhold;

    @JsonCreator
    private Arbeidstaker(
            @JsonProperty("organisasjonsnummer")
            Organisasjonsnummer organisasjonsnummer,
            @JsonProperty("arbeidsforhold")
            Map<Periode, Arbeidsforhold> arbeidsforhold) {
        this.organisasjonsnummer = organisasjonsnummer;
        this.arbeidsforhold = arbeidsforhold;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Organisasjonsnummer organisasjonsnummer;
        private Map<Periode, Arbeidsforhold> arbeidsforhold;

        private Builder() {
            arbeidsforhold = new HashMap<>();
        }

        public Builder organisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
            this.organisasjonsnummer = organisasjonsnummer;
            return this;
        }

        public Builder arbeidsforhold(Map<Periode, Arbeidsforhold> arbeidsforhold) {
            this.arbeidsforhold.putAll(arbeidsforhold);
            return this;
        }

        public Builder arbeidsforhold(Periode periode, Arbeidsforhold arbeidsforhold) {
            this.arbeidsforhold.put(periode, arbeidsforhold);
            return this;
        }

        public Arbeidstaker build() {
            return new Arbeidstaker(
                    organisasjonsnummer,
                    Collections.unmodifiableMap(arbeidsforhold)
            );
        }
    }

    public static final class Arbeidsforhold {
        public final Double skalJobbeProsent;

        private Arbeidsforhold(
                @JsonProperty("skalJobbeProsent")
                Double skalJobbeProsent) {
            this.skalJobbeProsent = skalJobbeProsent;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Double skalJobbeProsent;

            private Builder() {}

            public Builder skalJobbeProsent(Double skalJobbeProsent) {
                this.skalJobbeProsent = skalJobbeProsent;
                return this;
            }

            public Arbeidsforhold build() {
                return new Arbeidsforhold(
                        skalJobbeProsent
                );
            }
        }

    }
}
