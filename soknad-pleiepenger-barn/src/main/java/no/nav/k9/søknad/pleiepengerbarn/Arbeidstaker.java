package no.nav.k9.søknad.pleiepengerbarn;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPerioder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.Organisasjonsnummer;
import no.nav.k9.søknad.felles.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arbeidstaker {
    
    @JsonProperty(value="norskIdentitetsnummer")
    @Valid
    public final NorskIdentitetsnummer norskIdentitetsnummer;
    
    @JsonProperty(value="organisasjonsnummer")
    @Valid
    public final Organisasjonsnummer organisasjonsnummer;
    
    @JsonInclude(value = Include.NON_NULL)
    @JsonProperty(value="perioder")
    @Valid
    public final Map<Periode, ArbeidstakerPeriodeInfo> perioder;

    @JsonCreator
    private Arbeidstaker(
            @JsonProperty(value="norskIdentitetsnummer")
            NorskIdentitetsnummer norskIdentitetsnummer,
            @JsonProperty(value="organisasjonsnummer")
            Organisasjonsnummer organisasjonsnummer,
            @JsonProperty(value="perioder")
            Map<Periode, ArbeidstakerPeriodeInfo> perioder) {
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
        private Map<Periode, ArbeidstakerPeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder organisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
            this.organisasjonsnummer = organisasjonsnummer;
            return this;
        }

        public Builder perioder(Map<Periode, ArbeidstakerPeriodeInfo> perioder) {
            leggTilPerioder(this.perioder, perioder);
            return this;
        }

        public Builder periode(Periode periode, ArbeidstakerPeriodeInfo arbeidstakerPeriodeInfo) {
            leggTilPeriode(this.perioder, periode, arbeidstakerPeriodeInfo);
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

    public static final class ArbeidstakerPeriodeInfo {
        /**
         * En prosent mellom 0 og 100 som sier hvor mye søkeren kan jobbe (for en arbeidsgiver i en periode)
         * i forhold til hva den normalt ville gjort (om den ikke hadde hatt behov for pleiepenger).
         * - Ved avkorting mot arbeid er det tap av inntekt som skal dekkes. Hvilken stillingsprosent
         * eller hvor mange timer man jobber er ikke relevant, men er faktorer som kan brukes til å utlede "skalJobbeProsent".
         * - Om man har en stillingsprosent på 40%, men i perioden kan dekke en 20% stilling skal "skalJobbeProsent" være 50%.
         * - Om man normalt jobber 30 timer i uken, men nå får jobbet 10 timer skal "skalJobbeProsent" være 33.33%.
         * - Gitt at pleiepengene blir avkortet mot arbeid (ikke gradert mot tilsyn) vil "skalJobbeProsent" brukes ved
         * håndtering av inntektsmelding fra den aktuelle arbeidsgiveren for å regne ut inntektstapet i perioden.
         */
        public final BigDecimal skalJobbeProsent;

        public final Duration jobberNormaltPerUke;

        private ArbeidstakerPeriodeInfo(
                @JsonProperty("skalJobbeProsent")
                BigDecimal skalJobbeProsent,
                @JsonProperty("jobberNormaltPerUke")
                Duration jobberNormaltPerUke) {
            this.skalJobbeProsent = skalJobbeProsent == null ? null : skalJobbeProsent.setScale(2, RoundingMode.HALF_UP);
            this.jobberNormaltPerUke = jobberNormaltPerUke;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private BigDecimal skalJobbeProsent;
            private Duration jobberNormaltPerUke;

            private Builder() {}

            public Builder skalJobbeProsent(BigDecimal skalJobbeProsent) {
                this.skalJobbeProsent = skalJobbeProsent;
                return this;
            }

            public Builder jobberNormaltPerUke(Duration jobberNormaltPerUke) {
                this.jobberNormaltPerUke = jobberNormaltPerUke;
                return this;
            }

            public ArbeidstakerPeriodeInfo build() {
                return new ArbeidstakerPeriodeInfo(
                        skalJobbeProsent,
                        jobberNormaltPerUke
                );
            }
        }

    }
}
