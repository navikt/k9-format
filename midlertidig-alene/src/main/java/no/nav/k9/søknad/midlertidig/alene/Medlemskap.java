package no.nav.k9.søknad.midlertidig.alene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Medlemskap {

    @JsonProperty(value="harBoddIUtlandetSiste12Mnd", required = true)
    public final Boolean harBoddIUtlandetSiste12Mnd;

    @JsonProperty(value="utenlandsoppholdSiste12Mnd")
    public final List<Utenlandsopphold> utenlandsoppholdSiste12Mnd;

    @JsonProperty(value="skalBoIUtlandetNeste12Mnd", required = true)
    public final Boolean skalBoIUtlandetNeste12Mnd;

    @JsonProperty(value="utenlandsoppholdNeste12Mnd")
    public final List<Utenlandsopphold> utenlandsoppholdNeste12Mnd;

    @JsonCreator
    private Medlemskap(
            @JsonProperty(value = "harBoddIUtlandetSiste12Mnd", required = true) Boolean harBoddIUtlandetSiste12Mnd,
            @JsonProperty(value = "utenlandsoppholdSiste12Mnd") List<Utenlandsopphold>  utenlandsoppholdSiste12Mnd,
            @JsonProperty(value = "skalBoIUtlandetNeste12Mnd", required = true) Boolean skalBoIUtlandetNeste12Mnd,
            @JsonProperty(value = "utenlandsoppholdNeste12Mnd") List<Utenlandsopphold>  utenlandsoppholdNeste12Mnd) {

        this.harBoddIUtlandetSiste12Mnd = harBoddIUtlandetSiste12Mnd;
        this.utenlandsoppholdSiste12Mnd = utenlandsoppholdSiste12Mnd;
        this.skalBoIUtlandetNeste12Mnd = skalBoIUtlandetNeste12Mnd;
        this.utenlandsoppholdNeste12Mnd = utenlandsoppholdNeste12Mnd;
    }

    public static Medlemskap.Builder builder() {
        return new Medlemskap.Builder();
    }

    public static final class Builder {
        private Boolean harBoddIUtlandetSiste12Mnd;
        private List<Utenlandsopphold>  utenlandsoppholdSiste12Mnd;
        private Boolean skalBoIUtlandetNeste12Mnd;
        private List<Utenlandsopphold>  utenlandsoppholdNeste12Mnd;

        private Builder() {}

        public Medlemskap.Builder harBoddIUtlandetSiste12Mnd(Boolean harBoddIUtlandetSiste12Mnd) {
            this.harBoddIUtlandetSiste12Mnd = harBoddIUtlandetSiste12Mnd;
            return this;
        }

        public Medlemskap.Builder skalBoIUtlandetNeste12Mnd(Boolean skalBoIUtlandetNeste12Mnd) {
            this.skalBoIUtlandetNeste12Mnd = skalBoIUtlandetNeste12Mnd;
            return this;
        }

        public Medlemskap.Builder utenlandsoppholdSiste12Mnd(List<Utenlandsopphold>  utenlandsoppholdSiste12Mnd) {
            this.utenlandsoppholdSiste12Mnd = utenlandsoppholdSiste12Mnd;
            return this;
        }

        public Medlemskap.Builder utenlandsoppholdNeste12Mnd(List<Utenlandsopphold>  utenlandsoppholdNeste12Mnd) {
            this.utenlandsoppholdNeste12Mnd = utenlandsoppholdNeste12Mnd;
            return this;
        }

        public Medlemskap build() {
            return new Medlemskap(
                    harBoddIUtlandetSiste12Mnd,
                    utenlandsoppholdSiste12Mnd,
                    skalBoIUtlandetNeste12Mnd,
                    utenlandsoppholdNeste12Mnd
            );
        }
    }

}
