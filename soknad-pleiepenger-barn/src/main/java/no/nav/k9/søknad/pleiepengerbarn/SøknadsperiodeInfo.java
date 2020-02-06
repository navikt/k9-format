package no.nav.k9.søknad.pleiepengerbarn;


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
