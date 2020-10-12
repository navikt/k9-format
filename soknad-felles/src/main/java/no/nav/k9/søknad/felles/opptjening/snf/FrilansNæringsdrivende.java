package no.nav.k9.søknad.omsorgspenger.utbetaling.snf;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.opptjening.Aktivitet;
import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Aktivitet.FRILANS_NÆRING)
public class FrilansNæringsdrivende implements Aktivitet {

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

    @Override
    public Type getType() {
        return Type.FRILANS_OG_NÆRING;
    }
}
