package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.*;


import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PleiepengerBarnSoknad implements Periodisert {

    public final SoknadId soknadId;

    public final Versjon versjon;

    public final Periode periode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Spraak spraak;

    public final Soker soker;

    public final Barn barn;

    public final Utland utland;

    public final List<Beredskap> beredskap;

    public final List<Nattevaak> nattevaak;

    public final TilsynsordningSvar iTilsynsordning;

    public final List<Tilsynsordning> tilsynsordning;

    @JsonCreator
    private PleiepengerBarnSoknad(
            @JsonProperty("soknadId")
            SoknadId soknadId,
            @JsonProperty("versjon")
            Versjon versjon,
            @JsonProperty("periode")
            Periode periode,
            @JsonProperty("mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty("spraak")
            Spraak spraak,
            @JsonProperty("soker")
            Soker soker,
            @JsonProperty("barn")
            Barn barn,
            @JsonProperty("utland")
            Utland utland,
            @JsonProperty("beredskap")
            List<Beredskap> beredskap,
            @JsonProperty("nattevaak")
            List<Nattevaak> nattevaak,
            @JsonProperty("iTilsynsordning")
            TilsynsordningSvar iTilsynsordning,
            @JsonProperty("tilsynsordning")
            List<Tilsynsordning> tilsynsordning) {
        this.soknadId = soknadId;
        this.versjon = versjon;
        this.periode = periode;
        this.mottattDato = mottattDato;
        this.spraak = spraak;
        this.soker = soker;
        this.barn = barn;
        this.utland = utland;
        this.beredskap = beredskap;
        this.nattevaak = nattevaak;
        this.iTilsynsordning = iTilsynsordning;
        this.tilsynsordning = tilsynsordning != null ? tilsynsordning : Collections.emptyList();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    @JsonIgnore
    public Periode getPeriode() {
        return periode;
    }

    public static final class Builder {
        private static final PleiepengerBarnSoknadValidator validator = new PleiepengerBarnSoknadValidator();
        private static final Versjon versjon = Versjon.of("0.0.1");

        private SoknadId soknadId;
        private ZonedDateTime mottattDato;
        private Periode periode;
        private Spraak spraak;
        private Soker soker;
        private Barn barn;
        private Utland utland;
        private List<Beredskap> beredskap;
        private List<Nattevaak> nattevaak;
        private TilsynsordningSvar iTilsynsordning;
        private List<Tilsynsordning> tilsynsordning;

        private Builder() {
            beredskap = new ArrayList<>();
            nattevaak = new ArrayList<>();
            tilsynsordning = new ArrayList<>();
        }

        public Builder soknadId(SoknadId soknadId) {
            this.soknadId = soknadId;
            return this;
        }

        public Builder mottattDato(ZonedDateTime mottattDato) {
            this.mottattDato = mottattDato;
            return this;
        }

        public Builder periode(Periode periode) {
            this.periode = periode;
            return this;
        }

        public Builder spraak(Spraak spraak) {
            this.spraak = spraak;
            return this;
        }

        public Builder soker(Soker soker) {
            this.soker = soker;
            return this;
        }

        public Builder barn(Barn barn) {
            this.barn = barn;
            return this;
        }

        public Builder utland(Utland utland) {
            this.utland = utland;
            return this;
        }

        public Builder beredskap(List<Beredskap> beredskap) {
            this.beredskap.addAll(beredskap);
            return this;
        }

        public Builder beredskap(Beredskap beredskap) {
            this.beredskap.add(beredskap);
            return this;
        }

        public Builder nattevaak(List<Nattevaak> nattevaak) {
            this.nattevaak.addAll(nattevaak);
            return this;
        }

        public Builder nattevaak(Nattevaak nattevaak) {
            this.nattevaak.add(nattevaak);
            return this;
        }

        public Builder iTilsynsordning(TilsynsordningSvar iTilsynsordning) {
            this.iTilsynsordning = iTilsynsordning;
            return this;
        }

        public Builder tilsynsordning(List<Tilsynsordning> tilsynsordning) {
            this.tilsynsordning.addAll(tilsynsordning);
            return this;
        }

        public Builder tilsynsordning(Tilsynsordning tilsynsordning) {
            this.tilsynsordning.add(tilsynsordning);
            return this;
        }

        public PleiepengerBarnSoknad build() {
            PleiepengerBarnSoknad soknad = new PleiepengerBarnSoknad(
                    soknadId,
                    versjon,
                    periode,
                    mottattDato,
                    spraak,
                    soker,
                    barn,
                    utland,
                    Collections.unmodifiableList(beredskap),
                    Collections.unmodifiableList(nattevaak),
                    iTilsynsordning,
                    Collections.unmodifiableList(tilsynsordning)
            );
            validator.forsikreValidert(soknad);
            return soknad;
        }
    }
}
