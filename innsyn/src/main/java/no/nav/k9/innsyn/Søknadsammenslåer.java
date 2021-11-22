package no.nav.k9.innsyn;

import java.util.Map;
import java.util.stream.Collectors;

import no.nav.k9.innsyn.søknadsammeslåer.Arbeidstidsammenslåer;
import no.nav.k9.innsyn.søknadsammeslåer.Tilsynsammenslåer;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class Søknadsammenslåer {
    
    public static Søknad kunPleietrengendedata(Søknad søknad) {
        if (!(søknad.getYtelse() instanceof PleiepengerSyktBarn)) {
            throw new IllegalArgumentException("Kun PSB-søknader er støttet.");
        }
        final PleiepengerSyktBarn ytelse = (PleiepengerSyktBarn) søknad.getYtelse();
        
        final Map<Periode, TilsynPeriodeInfo> periodeMap = hentUtTilsynsperioder(ytelse);
        final PleiepengerSyktBarn resultatYtelse = new PleiepengerSyktBarn();
        resultatYtelse.medTilsynsordning(new Tilsynsordning().medPerioder(periodeMap));
        
        return new Søknad().medYtelse(resultatYtelse);
    }

    private static Map<Periode, TilsynPeriodeInfo> hentUtTilsynsperioder(final PleiepengerSyktBarn ytelse) {
        /* 
         * Advarsel: Data som returneres her deles på tvers av søkere. Ikke legg inn
         *           flere felter i mappingen hvis de ikke skal deles.
         *           
         *           Av denne grunn brukes ikke standard copy-contructor for Map/Tilsynordning
         */
        return ytelse.getTilsynsordning()
                .getPerioder()
                .entrySet()
                .stream()
                .map(e -> Map.entry(
                    new Periode(e.getKey()),
                    new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(e.getValue().getEtablertTilsynTimerPerDag())
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    public static Søknad slåSammen(Søknad tidligereSøknad, Søknad nySøknad) {
        if (!(tidligereSøknad.getYtelse() instanceof PleiepengerSyktBarn) || !(nySøknad.getYtelse() instanceof PleiepengerSyktBarn)) {
            throw new IllegalArgumentException("Kun PSB-søknader er støttet.");
        }

        final PleiepengerSyktBarn s1Ytelse = (PleiepengerSyktBarn) tidligereSøknad.getYtelse();
        final PleiepengerSyktBarn s2Ytelse = (PleiepengerSyktBarn) nySøknad.getYtelse();
        
        final PleiepengerSyktBarn ytelse = new PleiepengerSyktBarn();
        /* Bruk aktørId fra PsbSøknadsinnhold fremfor:
        ytelse.medBarn(new Barn()
            .medNorskIdentitetsnummer(NorskIdentitetsnummer.of(s2Ytelse.getBarn().getPersonIdent().getVerdi()))
            .medFødselsdato(s2Ytelse.getBarn().getFødselsdato())
        );
        */
        ytelse.medArbeidstid(Arbeidstidsammenslåer.slåSammenArbeidstid(s1Ytelse, s2Ytelse));
        ytelse.medTilsynsordning(Tilsynsammenslåer.slåsammen(s1Ytelse, s2Ytelse));
        
        /* Bruk aktørId fra PsbSøknadsinnhold fremfor:
        final Søknad s = new Søknad(
                SøknadId.of("generert"),
                Versjon.of("1.0.0"),
                nySøknad.getMottattDato(),
                new Søker(NorskIdentitetsnummer.of(nySøknad.getSøker().getPersonIdent().getVerdi())),
                nySøknad.getSpråk(),
                ytelse);
        */
        final Søknad s = new Søknad()
                .medSpråk(nySøknad.getSpråk())
                .medYtelse(ytelse);
        return s;
    }
}
