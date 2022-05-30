package no.nav.k9.innsyn;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.innsyn.søknadsammeslåer.Arbeidstidsammenslåer;
import no.nav.k9.innsyn.søknadsammeslåer.Tilsynsammenslåer;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.TidsserieUtils;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class Søknadsammenslåer {

    private Søknadsammenslåer() {
    }

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

        final PleiepengerSyktBarn s1Ytelse = tidligereSøknad.getYtelse();
        final PleiepengerSyktBarn s2Ytelse = nySøknad.getYtelse();

        final PleiepengerSyktBarn ytelse = new PleiepengerSyktBarn();
        ytelse.medSøknadsperiode(slåSammenSøknadsperioder(s1Ytelse, s2Ytelse));

        /* Bruk pleietrengendeAktørId fra PsbSøknadsinnhold fremfor: */
        ytelse.medBarn(new Barn()
                .medNorskIdentitetsnummer(NorskIdentitetsnummer.of("00000000000"))
        );

        ytelse.medArbeidstid(Arbeidstidsammenslåer.slåSammenArbeidstid(s1Ytelse, s2Ytelse));
        ytelse.medTilsynsordning(Tilsynsammenslåer.slåsammen(s1Ytelse, s2Ytelse));

        final Søknad s = new Søknad()
                .medSøknadId(SøknadId.of("generert"))
                .medVersjon("1.0.0.")
                .medMottattDato(ZonedDateTime.now())
                /* Bruk aktørId fra PsbSøknadsinnhold fremfor: */
                .medSøker(new Søker(NorskIdentitetsnummer.of("00000000000")))
                .medSpråk(nySøknad.getSpråk())
                .medYtelse(ytelse);
        return s;
    }

    private static List<Periode> slåSammenSøknadsperioder(final PleiepengerSyktBarn s1Ytelse, final PleiepengerSyktBarn s2Ytelse) {
        final LocalDateTimeline<Boolean> t1 = TidsserieUtils.toLocalDateTimeline(s1Ytelse.getSøknadsperiodeList());
        final LocalDateTimeline<Boolean> t2 = TidsserieUtils.toLocalDateTimeline(s2Ytelse.getSøknadsperiodeList());
        LocalDateTimeline<Boolean> harSøknadTidslinje = t1
                .union(t2, StandardCombinators::coalesceRightHandSide)
                .disjoint(TidsserieUtils.toLocalDateTimeline(s2Ytelse.getTrekkKravPerioder()))
                .compress();
        return TidsserieUtils.tilPeriodeList(harSøknadTidslinje);
    }
}
