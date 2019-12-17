package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Medlemskap {

    @JsonProperty("har_bodd_i_utlandet_siste_12_mnd")
    private Boolean harBoddIUtlandetSiste12mnd;

    @JsonProperty("skal_bo_i_utlandet_neste_12_mnd")
    private Boolean skalBoIUtlandetNeste12mnd;

    @JsonProperty(required = true)
    private List<Opphold> opphold = Collections.emptyList();


    public void setHarBoddIUtlandetSiste12mnd(Boolean harBoddIUtlandetSiste12mnd) {
        this.harBoddIUtlandetSiste12mnd = harBoddIUtlandetSiste12mnd;
    }

    public Boolean getHarBoddIUtlandetSiste12mnd() {
        return harBoddIUtlandetSiste12mnd;
    }

    public void setSkalBoIUtlandetNeste12mnd(Boolean skalBoIUtlandetNeste12mnd) {
        this.skalBoIUtlandetNeste12mnd = skalBoIUtlandetNeste12mnd;
    }

    public Boolean getSkalBoIUtlandetNeste12mnd() {
        return skalBoIUtlandetNeste12mnd;
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
