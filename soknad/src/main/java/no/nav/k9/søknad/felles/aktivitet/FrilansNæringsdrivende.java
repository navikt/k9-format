package no.nav.k9.søknad.felles.aktivitet;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class FrilansNæringsdrivende {

    @JsonProperty("selvstendigNæringsdrivende")
    @Valid
    private final List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;

    @JsonProperty("frilanser")
    @Valid
    private final Frilanser frilanser;

    @JsonCreator
    public FrilansNæringsdrivende(@JsonProperty("selvstendigNæringsdrivende") @Valid List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
                                  @JsonProperty("frilanser") @Valid Frilanser frilanser) {
        this.selvstendigNæringsdrivende = selvstendigNæringsdrivende;
        this.frilanser = frilanser;
    }

    public FrilansNæringsdrivende(@JsonProperty("selvstendigNæringsdrivende") @Valid List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende) {
        this(selvstendigNæringsdrivende, null);
    }

    public FrilansNæringsdrivende(@JsonProperty("frilanser") @Valid Frilanser frilanser) {
        this(null, frilanser);
    }

    public List<SelvstendigNæringsdrivende> getSelvstendigNæringsdrivende() {
        return selvstendigNæringsdrivende;
    }

    public Frilanser getFrilanser() {
        return frilanser;
    }
}
