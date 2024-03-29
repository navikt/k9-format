package no.nav.k9.søknad.felles.opptjening;

import static java.util.Collections.unmodifiableMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.VirksomhetType;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class SelvstendigNæringsdrivende {

    @JsonProperty(value = "perioder", required = true)
    @Valid
    @GyldigePerioderMap(krevFomDato = true, krevTomDato = false, payload = {AvbrytendeValideringsfeil.class})
    @NotNull
    @NotEmpty
    private Map<@NotNull Periode, @NotNull SelvstendigNæringsdrivendePeriodeInfo> perioder;

    /**
     * Orgnummer - påkrevd for norske selskaper, ikke for utenlandske enn så lenge.
     */
    @JsonProperty(value = "organisasjonsnummer", required = false)
    @Valid
    private Organisasjonsnummer organisasjonsnummer;

    /**
     * Virsomhetsnavn - påkrevd for norske og utenlandske selskaper.
     */
    @JsonProperty(value = "virksomhetNavn", required = false)
    @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message = "[ugyldigSyntaks] '${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    private String virksomhetNavn;

    public SelvstendigNæringsdrivende medPerioder(Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder) {
        this.perioder = Objects.requireNonNull(perioder, "perioder");
        return this;
    }

    public SelvstendigNæringsdrivende medOrganisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
        this.organisasjonsnummer = Objects.requireNonNull(organisasjonsnummer, "organisasjonsnummer");
        return this;
    }

    public SelvstendigNæringsdrivende medVirksomhetNavn(String virksomhetNavn) {
        this.virksomhetNavn = Objects.requireNonNull(virksomhetNavn, "virksomhetNavn");
        return this;
    }

    public Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public String getVirksomhetNavn() {
        return virksomhetNavn;
    }

    public Organisasjonsnummer getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    @AssertTrue(message = "organisasjonsnummer må være satt med mindre virksomhet er registrert i utlandet")
    boolean isOkOrganisasjonsnummer() {
        if (perioder == null) {
            return true;
        }
        if (organisasjonsnummer == null) {
            for (var sn : perioder.entrySet()) {
                if (sn.getValue().registrertIUtlandet != null && !sn.getValue().registrertIUtlandet) {
                    return false;
                }
            }
            return !perioder.isEmpty();
        }
        return true;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(value = Include.NON_EMPTY)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class SelvstendigNæringsdrivendePeriodeInfo {

        private static final String PÅKREVD = "påkrevd";

        @Valid
        @JsonProperty(value = "virksomhetstyper", required = true)
        @NotEmpty
        @NotNull
        private List<VirksomhetType> virksomhetstyper;

        @JsonProperty("regnskapsførerNavn")
        @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message = "[ugyldigSyntaks] '${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
        private String regnskapsførerNavn;

        @JsonProperty("regnskapsførerTlf")
        @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message = "[ugyldigSyntaks] '${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
        private String regnskapsførerTlf;

        @JsonProperty("erVarigEndring")
        private Boolean erVarigEndring;

        @JsonProperty("erNyIArbeidslivet")
        private Boolean erNyIArbeidslivet;

        @JsonProperty("endringDato")
        private LocalDate endringDato;

        @JsonProperty("endringBegrunnelse")
        @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}§]+$", message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        private String endringBegrunnelse;

        @JsonProperty("bruttoInntekt")
        @Valid
        @DecimalMin(value = "0.00", message = "[ugyldigVerdi] bruttoInntekt [${validatedValue}] må være >= {value}")
        @DecimalMax(value = "10000000.00", message = "[ugyldigVerdi] bruttoInntekt [${validatedValue}] må være <= {value}")
        private BigDecimal bruttoInntekt;

        @JsonProperty("erNyoppstartet")
        private Boolean erNyoppstartet;

        @JsonProperty("registrertIUtlandet")
        private Boolean registrertIUtlandet;

        //TODO validering etter "ISO 3166 alpha-3"
        @JsonProperty("landkode")
        @Valid
        private Landkode landkode;

        @JsonProperty("erFiskerPåBladB")
        private Boolean erFiskerPåBladB;

        public SelvstendigNæringsdrivendePeriodeInfo medVirksomhetstyper(List<VirksomhetType> virksomhetstyper) {
            this.virksomhetstyper = Objects.requireNonNull(virksomhetstyper, "virksomhetstyper");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medRegnskapsførerNavn(String regnskapsførerNavn) {
            this.regnskapsførerNavn = Objects.requireNonNull(regnskapsførerNavn, "regnskapsførerNavn");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medRegnskapsførerTlf(String regnskapsførerTlf) {
            this.regnskapsførerTlf = Objects.requireNonNull(regnskapsførerTlf, "regnskapsførerTlf");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medErVarigEndring(Boolean erVarigEndring) {
            this.erVarigEndring = Objects.requireNonNull(erVarigEndring, "erVarigEndring");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medErNyIArbeidslivet(Boolean erNyIArbeidslivet) {
            this.erNyIArbeidslivet = Objects.requireNonNull(erNyIArbeidslivet, "erNyIArbeidslivet");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medEndringDato(LocalDate endringDato) {
            this.endringDato = Objects.requireNonNull(endringDato, "endringDato");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medEndringBegrunnelse(String endringBegrunnelse) {
            this.endringBegrunnelse = Objects.requireNonNull(endringBegrunnelse, "endringBegrunnelse");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medBruttoInntekt(BigDecimal bruttoInntekt) {
            this.bruttoInntekt = Objects.requireNonNull(bruttoInntekt, "bruttoInntekt");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medErNyoppstartet(Boolean erNyoppstartet) {
            this.erNyoppstartet = Objects.requireNonNull(erNyoppstartet, "erNyoppstartet");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medRegistrertIUtlandet(Boolean registrertIUtlandet) {
            this.registrertIUtlandet = Objects.requireNonNull(registrertIUtlandet, "registrertIUtlandet");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medErFiskerPåBladB(Boolean erFiskerPåBladB) {
            this.erFiskerPåBladB = Objects.requireNonNull(erFiskerPåBladB, "erFiskerPåBladB");
            return this;
        }

        public SelvstendigNæringsdrivendePeriodeInfo medLandkode(Landkode landkode) {
            this.landkode = landkode;
            return this;
        }

        public String getRegnskapsførerNavn() {
            return regnskapsførerNavn;
        }

        public String getRegnskapsførerTlf() {
            return regnskapsførerTlf;
        }

        public Boolean getErVarigEndring() {
            return erVarigEndring;
        }

        public Boolean getErNyIArbeidslivet() {
            return erNyIArbeidslivet;
        }

        public LocalDate getEndringDato() {
            return endringDato;
        }

        public String getEndringBegrunnelse() {
            return endringBegrunnelse;
        }

        public BigDecimal getBruttoInntekt() {
            return bruttoInntekt;
        }

        public Boolean getErNyoppstartet() {
            return erNyoppstartet;
        }

        public Boolean getRegistrertIUtlandet() {
            return registrertIUtlandet;
        }

        public Landkode getLandkode() {
            return landkode;
        }

        public List<VirksomhetType> getVirksomhetstyper() {
            return virksomhetstyper;
        }

        public Boolean getErFiskerPåBladB() {
            return erFiskerPåBladB;
        }

        @Size(max = 0, message = "${validatedValue}")
        private List<Feil> getValideringRegistrertUtlandet() {
            if (registrertIUtlandet != null) {
                if (registrertIUtlandet && landkode == null) {
                    return List.of(new Feil(".landkode", PÅKREVD, "landkode må være satt, og kan ikke være null, dersom virksomhet er registrert i utlandet."));
                }
            }
            return List.of();
        }

        @AssertTrue(message = "endringDato må være satt dersom erVarigEndring er true")
        private boolean isVarigEndringsdatoSatt() {
            if (erVarigEndring != null && erVarigEndring) {
                return endringDato != null;
            }
            return true;
        }

        @AssertTrue(message = "endringBegrunnelse må være satt dersom erVarigEndring er true.")
        private boolean isVarigEndringBegrunnelseSatt() {
            if (erVarigEndring != null && erVarigEndring) {
                return !(endringBegrunnelse == null || endringBegrunnelse.isBlank());
            }
            return true;
        }

        @AssertTrue(message = "Kan ikke være blankt")
        private boolean isRegnskapsførerNavnEmpty() {
            if (regnskapsførerNavn == null) {
                return true;
            }
            return !regnskapsførerNavn.isEmpty();
        }

        @AssertTrue(message = "Kan ikke være blankt")
        private boolean isRegnskapsførerTlfEmpty() {
            if (regnskapsførerTlf == null) {
                return true;
            }
            return !regnskapsførerTlf.isEmpty();
        }


        /*
        Deaktivert da punsj ikke stille dette spørmålet for PSB enda.
        TODO: Aktivere dette igjen når punsj har dette som et spørsmål.
         */
        /*@AssertTrue(message = "erFiskerPåBladB kan ikke være null dersom virksomhetstyper er FISKE.")
        private boolean isFiskerPåBladBValid() {
            if (virksomhetstyper.contains(VirksomhetType.FISKE)) {
                try {
                    Objects.requireNonNull(erFiskerPåBladB, "erFiskerPåBladB");
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }*/

        /* Deaktivert pga søknader med feil
        @AssertTrue(message = "[ugyldigVerdi] Norge kan ikke være en landkode")
        private boolean isLandkodeNotNor() {
            if (landkode == null) {
                return true;
            }
            return !landkode.equals(Landkode.NORGE);
        }
         */

    }
}
