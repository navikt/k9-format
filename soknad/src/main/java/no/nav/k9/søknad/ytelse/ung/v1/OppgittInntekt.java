package no.nav.k9.søknad.ytelse.ung.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.ung.v1.inntekt.InntektForPeriode;

import java.time.LocalDate;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OppgittInntekt {

    /**
     * Inntekter i periode som arbeidstaker og/eller frilans
     */
    @JsonProperty(value = "arbeidstakerOgFrilansInntekter")
    @Valid
    @NotNull
    private NavigableSet<InntektForPeriode> arbeidstakerOgFrilansInntekter;

    /**
     * Inntekter i periode som selvstendig næringsdrivende.
     */
    @JsonProperty(value = "næringsinntekter")
    @Valid
    @NotNull
    private NavigableSet<InntektForPeriode> næringsinntekter;

    /**
     * Inntekter i periode som ytelse
     */
    @JsonProperty(value = "ytelser")
    @Valid
    @NotNull
    private NavigableSet<InntektForPeriode> ytelser;


    @JsonCreator
    public OppgittInntekt(@JsonProperty(value = "arbeidstakerOgFrilansInntekter") Set<InntektForPeriode> arbeidstakerOgFrilansInntekter,
                          @JsonProperty(value = "næringsinntekter") Set<InntektForPeriode> næringsinntekter,
                          @JsonProperty(value = "ytelser") Set<InntektForPeriode> ytelser) {
        this.arbeidstakerOgFrilansInntekter = (arbeidstakerOgFrilansInntekter == null) ? Collections.emptyNavigableSet()
                : Collections.unmodifiableNavigableSet(new TreeSet<>(arbeidstakerOgFrilansInntekter));
        this.næringsinntekter = (næringsinntekter == null) ? Collections.emptyNavigableSet()
                : Collections.unmodifiableNavigableSet(new TreeSet<>(næringsinntekter));
        this.ytelser = (ytelser == null) ? Collections.emptyNavigableSet()
                : Collections.unmodifiableNavigableSet(new TreeSet<>(ytelser));
    }

    public static Builder builder() {
        return new Builder();
    }

    public NavigableSet<InntektForPeriode> getArbeidstakerOgFrilansInntekter() {
        return arbeidstakerOgFrilansInntekter;
    }

    public NavigableSet<InntektForPeriode> getNæringsinntekter() {
        return næringsinntekter;
    }

    public NavigableSet<InntektForPeriode> getYtelser() {
        return ytelser;
    }

    public static final class Builder {
        private Set<InntektForPeriode> arbeidstakerOgFrilansInntekter = new LinkedHashSet<>();
        private Set<InntektForPeriode> næringsinntekter = new LinkedHashSet<>();
        private Set<InntektForPeriode> ytelser = new LinkedHashSet<>();

        private Builder() {
        }

        public Builder medArbeidstakerOgFrilansinntekter(Set<InntektForPeriode> inntekter) {
            arbeidstakerOgFrilansInntekter.addAll(inntekter);
            return this;
        }

        public Builder medNæringsinntekter(Set<InntektForPeriode> inntekter) {
            næringsinntekter.addAll(inntekter);
            return this;
        }

        public Builder medYtelser(Set<InntektForPeriode> inntekter) {
            ytelser.addAll(inntekter);
            return this;
        }

        public OppgittInntekt build() {
            return new OppgittInntekt(arbeidstakerOgFrilansInntekter, næringsinntekter, ytelser);
        }
    }

    @AssertTrue(message = "Perioder for inntekt kan ikke overlappe")
    public boolean isHarIngenOverlappendePerioder() {
        return harIngenOverlapp(arbeidstakerOgFrilansInntekter) && harIngenOverlapp(næringsinntekter) && harIngenOverlapp(ytelser);
    }

    private boolean harIngenOverlapp(NavigableSet<InntektForPeriode> set) {
        final var iterator = set.iterator();
        // Initialiserer til første mulige periode
        var prev = new Periode(LocalDate.MIN, LocalDate.MIN);
        // Siden settet er av typen NavigableSet (sortert) trenger vi kun å sjekke forrige element i lista
        while (iterator.hasNext()) {
            final var next = iterator.next();
            if (prev.inneholder(next.getPeriode())) {
                return false;
            }
            prev = next.getPeriode();
        }
        return true;
    }


}
