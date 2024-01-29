package no.nav.k9.innsyn;

import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.k9.innsyn.sak.Saksinnhold;

@Valid
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = InnsynHendelseData.PSB_SØKNADSINNHOLD, value = PsbSøknadsinnhold.class),
        @JsonSubTypes.Type(name = InnsynHendelseData.OMSORG, value = Omsorg.class),
        @JsonSubTypes.Type(name = InnsynHendelseData.SØKNAD_TRUKKET, value = SøknadTrukket.class),
        @JsonSubTypes.Type(name = InnsynHendelseData.SAK_INNHOLD, value = Saksinnhold.class),
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public interface InnsynHendelseData {
    String PSB_SØKNADSINNHOLD = "PSB_SØKNADSINNHOLD";
    String OMSORG = "OMSORG";
    String SØKNAD_TRUKKET = "SØKNAD_TRUKKET";
    String SAK_INNHOLD = "SAK_INNHOLD";
}
