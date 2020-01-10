package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Arbeidsgivere {
    public final List<Arbeidstaker> arbeidstaker;
    public final List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;
    public final List<Frilanser> frilanser;

    @JsonCreator
    private Arbeidsgivere(
            @JsonProperty("arbeidstaker")
            List<Arbeidstaker> arbeidstaker,
            @JsonProperty("selvstendigNæringsdrivende")
            List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
            @JsonProperty("frilanser")
            List<Frilanser> frilanser) {
        this.arbeidstaker = arbeidstaker;
        this.selvstendigNæringsdrivende = selvstendigNæringsdrivende;
        this.frilanser = frilanser;
    }
}
