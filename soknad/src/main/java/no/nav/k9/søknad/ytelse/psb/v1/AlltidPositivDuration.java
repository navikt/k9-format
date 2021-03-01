package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.constraints.AssertTrue;
import java.time.Duration;


@JsonFormat
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class AlltidPositivDuration {

    @JsonValue
    private Duration duration;

    public AlltidPositivDuration(Duration duration) {
        this.duration = duration;
    }

    public AlltidPositivDuration() {
    }

    public Duration getDuration() {
        return duration;
    }

    @AssertTrue(message = "Duration kan ikke være negativ. ")
    private boolean isNotNegative() {
        if(duration == null) {
            return false;
        }
        return !this.duration.isNegative();
    }
}
