package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class TestUtils {

    private TestUtils() {

    }

    static final String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String jsonForKomplettSoknad() {
        return jsonFromFile("komplett-soknad.json");
    }

    static PleiepengerBarnSoknad komplettSoknad() {
        final PleiepengerBarnSoknad soknad = JsonUtils.fromString(jsonForKomplettSoknad(), PleiepengerBarnSoknad.class);
        return soknad;
    }

    static PleiepengerBarnSoknad.Builder komplettBuilder() {
        PleiepengerBarnSoknad soknad = komplettSoknad();
        return PleiepengerBarnSoknad.builder()
                .soknadId(soknad.soknadId)
                .periode(soknad.periode)
                .mottattDato(soknad.mottattDato)
                .spraak(soknad.spraak)
                .soker(soknad.soker)
                .barn(soknad.barn)
                .utland(soknad.utland)
                .beredskap(soknad.beredskap)
                .nattevaak(soknad.nattevaak)
                .iTilsynsordning(soknad.iTilsynsordning)
                .tilsynsordning(soknad.tilsynsordning);
    }

    /*
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
     */

}
