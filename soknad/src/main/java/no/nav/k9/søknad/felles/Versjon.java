package no.nav.k9.søknad.felles;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

public class Versjon implements Comparable<Versjon> {

    @JsonIgnore
    private static final String SEMVER_REGEX = "(\\d+)\\.(\\d+)\\.(\\d+)";

    @JsonValue
    @Size(max = 10)
    @Pattern(regexp = SEMVER_REGEX, message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    private final String verdi;

    @JsonCreator
    public Versjon(String verdi) {
        if (verdi == null || verdi.isBlank()) {
            throw new IllegalArgumentException("Kan ikke ha null eller blank versjon");
        }
        this.verdi = verdi;
    }

    public String getVerdi() {
        return verdi;
    }
    
    @JsonCreator
    public static Versjon of(String verdi) {
        if (verdi == null || verdi.isBlank()) {
            return null;
        }
        return new Versjon(verdi);
    }

    @JsonIgnore
    public boolean erGyldig() {
        return verdi.matches(SEMVER_REGEX);
    }

    @Override
    public int compareTo(Versjon that) {
        if (!this.erGyldig() || !that.erGyldig()) {
            throw new IllegalArgumentException("Ugyldig format på Versjon");
        }

        List<Integer> thisComponents = Arrays.stream(this.verdi.split("\\."))
                .map(Integer::parseInt).toList();
        List<Integer> thatComponents = Arrays.stream(that.verdi.split("\\."))
                .map(Integer::parseInt).toList();

        for (int i=0; i<3; i++) {
            Integer thisVersjonstall = thisComponents.get(i);
            Integer thatVersjonstall = thatComponents.get(i);
            if (!Objects.equals(thisVersjonstall, thatVersjonstall)) {
                return thisVersjonstall.compareTo(thatVersjonstall);
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var other = (Versjon) obj;
        return Objects.equals(verdi, other.verdi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verdi);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<versjon=" + verdi + ">";
    }
}
