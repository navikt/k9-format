package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.NorskIdentitetsnummer;
import no.nav.k9.soknad.felles.Organisasjonsnummer;
import no.nav.k9.soknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Arbeidstaker {
    public final NorskIdentitetsnummer norskIdentitetsnummer;
    public final Organisasjonsnummer organisasjonsnummer;
    public final Map<Periode, ArbeidstakerPeriode> perioder;

    @JsonCreator
    private Arbeidstaker(
            @JsonProperty("norskIdentitetsnummer")
            NorskIdentitetsnummer norskIdentitetsnummer,
            @JsonProperty("organisasjonsnummer")
            Organisasjonsnummer organisasjonsnummer,
            @JsonProperty("perioder")
            Map<Periode, ArbeidstakerPeriode> perioder) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.organisasjonsnummer = organisasjonsnummer;
        this.perioder = perioder == null ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;
        private Organisasjonsnummer organisasjonsnummer;
        private Map<Periode, ArbeidstakerPeriode> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder organisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
            this.organisasjonsnummer = organisasjonsnummer;
            return this;
        }

        public Builder perioder(Map<Periode, ArbeidstakerPeriode> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, ArbeidstakerPeriode arbeidstakerPeriode) {
            this.perioder.put(periode, arbeidstakerPeriode);
            return this;
        }

        public Builder norskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
            this.norskIdentitetsnummer = norskIdentitetsnummer;
            return this;
        }

        public Arbeidstaker build() {
            return new Arbeidstaker(
                    norskIdentitetsnummer,
                    organisasjonsnummer,
                    perioder
            );
        }
    }

    public static final class ArbeidstakerPeriode {
        public final Double skalJobbeProsent;

        private ArbeidstakerPeriode(
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

            public ArbeidstakerPeriode build() {
                return new ArbeidstakerPeriode(
                        skalJobbeProsent
                );
            }
        }

    }
}
