package no.nav.k9.søknad.ytelse.psb.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import no.nav.k9.søknad.felles.type.Periode;

public class PerioderMedEndring {
    private String felt;
    private Map<Periode, ?> periodeMap;

    public PerioderMedEndring() {

    }

    public String getFelt() {
        return felt;
    }

    public Map<Periode, ?> getPeriodeMap() {
        return periodeMap;
    }

    public List<Periode> getPeriodeList() {
        return new ArrayList<>(periodeMap.keySet());
    }

    public PerioderMedEndring medPerioder(String felt, Map<Periode, ?> periodeMap) {
        this.felt = felt;
        this.periodeMap = periodeMap;
        return this;
    }
}
