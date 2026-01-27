package no.nav.k9.søknad.ytelse.ung.v1.inntekt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.k9.søknad.felles.type.Periode;

import java.time.LocalDate;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OppgittInntekt {

    /**
     * Inntekter i periode som arbeidstaker og/eller frilans
     */
    @JsonProperty(value = "oppgittePeriodeinntekter")
    @NotNull
    @Size(min = 1)
    private NavigableSet<@Valid @NotNull OppgittInntektForPeriode> oppgittePeriodeinntekter;

    @JsonCreator
    public OppgittInntekt(@JsonProperty(value = "oppgittePeriodeinntekter") Set<OppgittInntektForPeriode> oppgittePeriodeinntekter) {
        this.oppgittePeriodeinntekter = (oppgittePeriodeinntekter == null) ? Collections.emptyNavigableSet()
                : Collections.unmodifiableNavigableSet(new TreeSet<>(oppgittePeriodeinntekter));
    }

    public static Builder builder() {
        return new Builder();
    }

    public NavigableSet<OppgittInntektForPeriode> getOppgittePeriodeinntekter() {
        return oppgittePeriodeinntekter;
    }

    public Periode getMinMaksPeriode() {
        final var first = oppgittePeriodeinntekter.first();
        final var last = oppgittePeriodeinntekter.last();
        return new Periode(first.getPeriode().getFraOgMed(), last.getPeriode().getTilOgMed());
    }

    public static final class Builder {
        private Set<OppgittInntektForPeriode> oppgittePeriodeinntekter = new LinkedHashSet<>();

        private Builder() {
        }

        public Builder medOppgittePeriodeinntekter(Set<OppgittInntektForPeriode> inntekter) {
            if (inntekter != null) {
                oppgittePeriodeinntekter.addAll(inntekter);
            }
            return this;
        }

        public OppgittInntekt build() {
            if (oppgittePeriodeinntekter.isEmpty()) {
                throw new IllegalStateException("Må oppgi minst en periodeinntekt");
            }
            return new OppgittInntekt(oppgittePeriodeinntekter);
        }
    }

    @AssertTrue(message = "Perioder for inntekt kan ikke overlappe")
    public boolean isHarIngenOverlappendePerioder() {
        return harIngenOverlapp(oppgittePeriodeinntekter);
    }

    private boolean harIngenOverlapp(@Valid @NotNull NavigableSet<@NotNull OppgittInntektForPeriode> set) {
        final var iterator = set.iterator();
        // Initialiserer til første mulige periode
        var prev = new Periode(LocalDate.MIN, LocalDate.MIN);
        // Siden settet er av typen NavigableSet (sortert) trenger vi kun å sjekke forrige element i lista
        while (iterator.hasNext()) {
            final var next = iterator.next();
            if (!prev.getTilOgMed().isBefore(next.getPeriode().getFraOgMed())) {
                return false;
            }
            prev = next.getPeriode();
        }
        return true;
    }


}
