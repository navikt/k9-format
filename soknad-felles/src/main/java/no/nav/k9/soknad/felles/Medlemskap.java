package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Medlemskap {

    private Boolean harBoddIUtlandetSiste12Mnd;

    private Boolean skalBoIUtlandetNeste12Mnd;

    @JsonProperty(required = true)
    private List<Opphold> opphold = Collections.emptyList();

    public Boolean getHarBoddIUtlandetSiste12Mnd() {
        return harBoddIUtlandetSiste12Mnd;
    }

    public void setHarBoddIUtlandetSiste12Mnd(Boolean harBoddIUtlandetSiste12Mnd) {
        this.harBoddIUtlandetSiste12Mnd = harBoddIUtlandetSiste12Mnd;
    }

    public Boolean getSkalBoIUtlandetNeste12Mnd() {
        return skalBoIUtlandetNeste12Mnd;
    }

    public void setSkalBoIUtlandetNeste12Mnd(Boolean skalBoIUtlandetNeste12Mnd) {
        this.skalBoIUtlandetNeste12Mnd = skalBoIUtlandetNeste12Mnd;
    }

    public void setOpphold(List<Opphold> opphold) {
        if (opphold == null) {
            throw new IllegalArgumentException("opphold == null");
        }
        if (opphold.contains(null)) {
            throw new IllegalArgumentException("opphold inneholder en null-verdi");
        }
        this.opphold = Collections.unmodifiableList(opphold);
    }

    public List<Opphold> getOpphold() {
        return Collections.unmodifiableList(opphold);
    }
}


