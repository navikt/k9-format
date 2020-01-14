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
    public final Map<Periode, ArbeidstakerInfo> perioder;

    @JsonCreator
    private Arbeidstaker(
            @JsonProperty("norskIdentitetsnummer")
            NorskIdentitetsnummer norskIdentitetsnummer,
            @JsonProperty("organisasjonsnummer")
            Organisasjonsnummer organisasjonsnummer,
            @JsonProperty("perioder")
            Map<Periode, ArbeidstakerInfo> perioder) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.organisasjonsnummer = organisasjonsnummer;
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;
        private Organisasjonsnummer organisasjonsnummer;
        private Map<Periode, ArbeidstakerInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder organisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
            this.organisasjonsnummer = organisasjonsnummer;
            return this;
        }

        public Builder perioder(Map<Periode, ArbeidstakerInfo> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, ArbeidstakerInfo arbeidstakerInfo) {
            this.perioder.put(periode, arbeidstakerInfo);
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

    public static final class ArbeidstakerInfo {
        public final Double skalJobbeProsent;

        private ArbeidstakerInfo(
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

            public ArbeidstakerInfo build() {
                return new ArbeidstakerInfo(
                        skalJobbeProsent
                );
            }
        }

    }
}
