package no.nav.k9.søknad.felles;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Kildesystem {

    private static final Map<String, Kildesystem> cache = new HashMap<>();
    
    public static final Kildesystem SØKNADSDIALOG = placeInCache("søknadsdialog");
    public static final Kildesystem ENDRINGSDIALOG = placeInCache("endringsdialog");
    public static final Kildesystem PUNSJ = placeInCache("punsj");
    public static final Kildesystem UTLEDET = placeInCache("utledet");

    
    @JsonValue
    private final String kode;
    
    private Kildesystem(String kode) {
        this.kode = kode;
    }
    
    public String getKode() {
        return kode;
    }
    
    @JsonCreator
    public static Kildesystem of(String kode) {
        final Kildesystem kildesystem = cache.get(kode);
        if (kildesystem != null) {
            return kildesystem;
        }
        return new Kildesystem(kode);
    }
    
    private static Kildesystem placeInCache(String kode) {
        final Kildesystem kildesystem = new Kildesystem(kode);
        cache.put(kode, kildesystem);
        return kildesystem;
    }
}
