package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPerioder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SelvstendigNæringsdrivende {

    @JsonProperty("perioder")
    @Valid
    @NotNull
    @NotEmpty
    public final Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder;

    @JsonProperty("organisasjonsnummer")
    @NotNull
    public final Organisasjonsnummer organisasjonsnummer;

    public static SelvstendigNæringsdrivende.Builder builder() {
        return new SelvstendigNæringsdrivende.Builder();
    }

    @JsonCreator
    public SelvstendigNæringsdrivende(
            @JsonProperty("perioder") Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder,
            @JsonProperty("organisasjonsnummer") Organisasjonsnummer organisasjonsnummer
    ) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
        this.organisasjonsnummer = organisasjonsnummer;
    }

    public static final class Builder {
        private Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder;
        private Organisasjonsnummer organisasjonsnummer;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder) {
            leggTilPerioder(this.perioder, perioder);
            return this;
        }

        public Builder periode(Periode periode, SelvstendigNæringsdrivendePeriodeInfo selvstendigNæringsdrivendePeriodeInfo) {
            leggTilPeriode(this.perioder, periode, selvstendigNæringsdrivendePeriodeInfo);
            return this;
        }

        public SelvstendigNæringsdrivende.Builder organisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
            this.organisasjonsnummer = organisasjonsnummer;
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(perioder, organisasjonsnummer);
        }
    }

    public static final class SelvstendigNæringsdrivendePeriodeInfo {

        @JsonProperty("virksomhetstyper")
        @NotNull
        @NotEmpty
        public final List<VirksomhetType> virksomhetstyper;

        @JsonProperty("regnskapsførerNavn")
        @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message="'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
        public final String regnskapsførerNavn;

        @JsonProperty("regnskapsførerTlf")
        @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message="'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
        public final String regnskapsførerTlf;

        @JsonProperty("virksomhetNavn")
        @NotNull
        @NotBlank
        @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message="'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
        public final String virksomhetNavn;

        @JsonProperty("erVarigEndring")
        public final Boolean erVarigEndring;

        @JsonProperty("endringDato")
        public final LocalDate endringDato;

        @JsonProperty("endringBegrunnelse")
        public final String endringBegrunnelse;

        @JsonProperty("bruttoInntekt")
        @Valid
        @NotNull // TODO: Burde være påkrevd? Er ikke påkrevd i brukerdialog
        @DecimalMin(value = "0.00", message = "beløp '${validatedValue}' må være >= {value}")
        public final BigDecimal bruttoInntekt;

        @JsonProperty("erNyoppstartet")
        public final Boolean erNyoppstartet;

        @JsonCreator
        private SelvstendigNæringsdrivendePeriodeInfo(
                @JsonProperty("virksomhetstyper") List<VirksomhetType> virksomhetstyper,
                @JsonProperty("regnskapsførerNavn") String regnskapsførerNavn,
                @JsonProperty("regnskapsførerTlf") String regnskapsførerTlf,
                @JsonProperty("virksomhetNavn") String virksomhetNavn,
                @JsonProperty("erVarigEndring") Boolean erVarigEndring,
                @JsonProperty("endringDato") LocalDate endringDato,
                @JsonProperty("endringBegrunnelse") String endringBegrunnelse,
                @JsonProperty("bruttoInntekt") BigDecimal bruttoInntekt,
                @JsonProperty("erNyoppstartet") Boolean erNyoppstartet
        ) {
            this.virksomhetstyper = virksomhetstyper;
            this.regnskapsførerNavn = regnskapsførerNavn;
            this.regnskapsførerTlf = regnskapsførerTlf;
            this.virksomhetNavn = virksomhetNavn;
            this.erVarigEndring = erVarigEndring;
            this.endringDato = endringDato;
            this.endringBegrunnelse = endringBegrunnelse;
            this.bruttoInntekt = bruttoInntekt;
            this.erNyoppstartet = erNyoppstartet;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private List<VirksomhetType> virksomhetstyper;
            private String regnskapsførerNavn;
            private String regnskapsførerTelefon;
            private String virksomhetNavn;
            private Boolean erVarigEndring;
            private LocalDate endringDato;
            private String endringBegrunnelse;
            private BigDecimal bruttoInntekt;
            private Boolean erNyoppstartet;

            private Builder() {
            }

            public Builder virksomhetstyper(List<VirksomhetType> virksomhetstyper) {
                this.virksomhetstyper = virksomhetstyper;
                return this;
            }

            public Builder regnskapsførerNavn(String regnskapsførerNavn) {
                this.regnskapsførerNavn = regnskapsførerNavn;
                return this;
            }

            public Builder regnskapsførerTelefon(String regnskapsførerTelefon) {
                this.regnskapsførerTelefon = regnskapsførerTelefon;
                return this;
            }

            public Builder virksomhetNavn(String virksomhetNavn) {
                this.virksomhetNavn = virksomhetNavn;
                return this;
            }

            public Builder endringDato(LocalDate endringDato) {
                this.endringDato = endringDato;
                return this;
            }

            public Builder erVarigEndring(Boolean erVarigEndring) {
                this.erVarigEndring = erVarigEndring;
                return this;
            }

            public Builder endringBegrunnelse(String endringBegrunnelse) {
                this.endringBegrunnelse = endringBegrunnelse;
                return this;
            }

            public Builder bruttoInntekt(BigDecimal bruttoInntekt) {
                this.bruttoInntekt = bruttoInntekt;
                return this;
            }

            public Builder erNyoppstartet(Boolean erNyoppstartet) {
                this.erNyoppstartet = erNyoppstartet;
                return this;
            }

            public SelvstendigNæringsdrivendePeriodeInfo build() {
                return new SelvstendigNæringsdrivendePeriodeInfo(
                        virksomhetstyper,
                        regnskapsførerNavn,
                        regnskapsførerTelefon,
                        virksomhetNavn,
                        erVarigEndring,
                        endringDato,
                        endringBegrunnelse,
                        bruttoInntekt,
                        erNyoppstartet
                );
            }
        }
    }
}
