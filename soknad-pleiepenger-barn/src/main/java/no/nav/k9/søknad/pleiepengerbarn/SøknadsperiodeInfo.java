package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SøknadsperiodeInfo {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Builder() {}

        public SøknadsperiodeInfo build() {
            return new SøknadsperiodeInfo();
        }
    }

}
