package no.nav.k9.søknad.felles;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPerioder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SelvstendigNæringsdrivende {

    @JsonProperty("perioder")
    @Valid
    @NotNull
    @NotEmpty
    public final Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder;

    @JsonProperty("organisasjonsnummer")
    public final Organisasjonsnummer organisasjonsnummer;

    @JsonProperty("virksomhetNavn")
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message="'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    public final String virksomhetNavn;

    public static SelvstendigNæringsdrivende.Builder builder() {
        return new SelvstendigNæringsdrivende.Builder();
    }

    @JsonCreator
    public SelvstendigNæringsdrivende(
            @JsonProperty("perioder") Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder,
            @JsonProperty("organisasjonsnummer") Organisasjonsnummer organisasjonsnummer,
            @JsonProperty("virksomhetNavn") String virksomhetNavn) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
        this.organisasjonsnummer = organisasjonsnummer;
        this.virksomhetNavn = virksomhetNavn;
    }

    public static final class Builder {
        private Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder;
        private Organisasjonsnummer organisasjonsnummer;
        private String virksomhetNavn;

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

        public SelvstendigNæringsdrivende.Builder virksomhetNavn(String virksomhetNavn) {
            this.virksomhetNavn = virksomhetNavn;
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(perioder, organisasjonsnummer, virksomhetNavn);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class SelvstendigNæringsdrivendePeriodeInfo {

        @JsonInclude(value = Include.NON_NULL)
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

        @JsonProperty("erVarigEndring")
        public final Boolean erVarigEndring;

        @JsonProperty("endringDato")
        public final LocalDate endringDato;

        @JsonProperty("endringBegrunnelse")
        public final String endringBegrunnelse;

        @JsonProperty("bruttoInntekt")
        @Valid
        public final BigDecimal bruttoInntekt;

        @JsonProperty("erNyoppstartet")
        public final Boolean erNyoppstartet;

        @JsonProperty("registrertIUtlandet")
        public final Boolean registrertIUtlandet;

        @JsonProperty("landkode")
        @Valid
        public final Landkode landkode;

        @JsonCreator
        private SelvstendigNæringsdrivendePeriodeInfo(
                @JsonProperty("virksomhetstyper") List<VirksomhetType> virksomhetstyper,
                @JsonProperty("regnskapsførerNavn") String regnskapsførerNavn,
                @JsonProperty("regnskapsførerTlf") String regnskapsførerTlf,
                @JsonProperty("erVarigEndring") Boolean erVarigEndring,
                @JsonProperty("endringDato") LocalDate endringDato,
                @JsonProperty("endringBegrunnelse") String endringBegrunnelse,
                @JsonProperty("bruttoInntekt") BigDecimal bruttoInntekt,
                @JsonProperty("erNyoppstartet") Boolean erNyoppstartet,
                @JsonProperty("registrertIUtlandet") Boolean registrertIUtlandet,
                @JsonProperty("landkode") Landkode landkode) {
            this.virksomhetstyper = virksomhetstyper;
            this.regnskapsførerNavn = regnskapsførerNavn;
            this.regnskapsførerTlf = regnskapsførerTlf;
            this.erVarigEndring = erVarigEndring;
            this.endringDato = endringDato;
            this.endringBegrunnelse = endringBegrunnelse;
            this.bruttoInntekt = bruttoInntekt;
            this.erNyoppstartet = erNyoppstartet;
            this.registrertIUtlandet = registrertIUtlandet;
            this.landkode = landkode;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private List<VirksomhetType> virksomhetstyper;
            private String regnskapsførerNavn;
            private String regnskapsførerTelefon;
            private Boolean erVarigEndring;
            private LocalDate endringDato;
            private String endringBegrunnelse;
            private BigDecimal bruttoInntekt;
            private Boolean erNyoppstartet;
            private Boolean registrertIUtlandet;
            private Landkode landkode;

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

            public Builder registrertIUtlandet(Boolean registrertIUtlandet) {
                this.registrertIUtlandet = registrertIUtlandet;
                return this;
            }

            public Builder landkode(Landkode landkode) {
                this.landkode = landkode;
                return this;
            }

            public SelvstendigNæringsdrivendePeriodeInfo build() {
                return new SelvstendigNæringsdrivendePeriodeInfo(
                        virksomhetstyper,
                        regnskapsførerNavn,
                        regnskapsførerTelefon,
                        erVarigEndring,
                        endringDato,
                        endringBegrunnelse,
                        bruttoInntekt,
                        erNyoppstartet,
                        registrertIUtlandet, landkode);
            }
        }
    }
}
