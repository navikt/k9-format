package no.nav.k9.innsyn.søknadsammeslåer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;

public class Arbeidstidsammenslåer {

    public static Arbeidstid slåSammenArbeidstid(PleiepengerSyktBarn s1Ytelse, PleiepengerSyktBarn s2Ytelse) {
        final Arbeidstid a1 = s1Ytelse.getArbeidstid();
        final Arbeidstid a2 = s2Ytelse.getArbeidstid();
        final Arbeidstid sammenslåttArbeidstid = new Arbeidstid();

        final List<Arbeidstaker> arbeidstakerliste = slåSammenArbeidstakerArbeidstid(s2Ytelse, a1, a2);
        sammenslåttArbeidstid.medArbeidstaker(arbeidstakerliste);
        
        final ArbeidstidInfo frilanserArbeidstidInfo = slåSammenFrilanserArbeidstid(s2Ytelse, a1, a2);
        sammenslåttArbeidstid.medFrilanserArbeidstid(frilanserArbeidstidInfo);
        
        final ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo = slåSammenSelvstendigNæringsdrivendeArbeidstidInfo(s2Ytelse, a1, a2);
        sammenslåttArbeidstid.medSelvstendigNæringsdrivendeArbeidstidInfo(selvstendigNæringsdrivendeArbeidstidInfo);

        return sammenslåttArbeidstid;
    }

    private static ArbeidstidInfo slåSammenSelvstendigNæringsdrivendeArbeidstidInfo(PleiepengerSyktBarn s2Ytelse, Arbeidstid a1, Arbeidstid a2) {
        final LocalDateTimeline<ArbeidstidPeriodeInfo> t1 = lagArbeidstidTidslinje(a1.getSelvstendigNæringsdrivendeArbeidstidInfo().orElse(null));
        final LocalDateTimeline<ArbeidstidPeriodeInfo> t2 = lagArbeidstidTidslinje(a2.getSelvstendigNæringsdrivendeArbeidstidInfo().orElse(null));
        final ArbeidstidInfo frilanserArbeidstidInfo = slåSammenArbeidstidInfo(s2Ytelse, t1, t2);
        return frilanserArbeidstidInfo;
    }
    
    private static ArbeidstidInfo slåSammenFrilanserArbeidstid(PleiepengerSyktBarn s2Ytelse, final Arbeidstid a1, final Arbeidstid a2) {
        final LocalDateTimeline<ArbeidstidPeriodeInfo> t1 = lagArbeidstidTidslinje(a1.getFrilanserArbeidstidInfo().orElse(null));
        final LocalDateTimeline<ArbeidstidPeriodeInfo> t2 = lagArbeidstidTidslinje(a2.getFrilanserArbeidstidInfo().orElse(null));
        final ArbeidstidInfo frilanserArbeidstidInfo = slåSammenArbeidstidInfo(s2Ytelse, t1, t2);
        return frilanserArbeidstidInfo;
    }

    private static List<Arbeidstaker> slåSammenArbeidstakerArbeidstid(PleiepengerSyktBarn s2Ytelse, final Arbeidstid a1, final Arbeidstid a2) {
        final List<Arbeidstaker> arbeidstakerliste = new ArrayList<>();
        final Map<Object, LocalDateTimeline<ArbeidstidPeriodeInfo>> arbeidstakertid1 = byggTidslinjeMap(a1);
        final Map<Object, LocalDateTimeline<ArbeidstidPeriodeInfo>> arbeidstakertid2 = byggTidslinjeMap(a2);
        final Set<Object> arbeidstakere = new HashSet<>(arbeidstakertid1.keySet());
        arbeidstakere.addAll(arbeidstakertid2.keySet());
        for (Object arbeidstakerIdent : arbeidstakere) {
            final Arbeidstaker sammenslåttArbeidstaker = new Arbeidstaker();
            if (arbeidstakerIdent instanceof Organisasjonsnummer) {
                sammenslåttArbeidstaker.medOrganisasjonsnummer((Organisasjonsnummer) arbeidstakerIdent);
            } else {
                sammenslåttArbeidstaker.medNorskIdentitetsnummer((NorskIdentitetsnummer) arbeidstakerIdent);
            }
            final LocalDateTimeline<ArbeidstidPeriodeInfo> t1 = hentTidslinje(arbeidstakertid1, arbeidstakerIdent);
            final LocalDateTimeline<ArbeidstidPeriodeInfo> t2 = hentTidslinje(arbeidstakertid2, arbeidstakerIdent);
            final ArbeidstidInfo arbeidstidInfo = slåSammenArbeidstidInfo(s2Ytelse, t1, t2);
            sammenslåttArbeidstaker.medArbeidstidInfo(arbeidstidInfo);
            arbeidstakerliste.add(sammenslåttArbeidstaker);
        }
        return arbeidstakerliste;
    }

    private static ArbeidstidInfo slåSammenArbeidstidInfo(PleiepengerSyktBarn s2Ytelse, final LocalDateTimeline<ArbeidstidPeriodeInfo> t1, final LocalDateTimeline<ArbeidstidPeriodeInfo> t2) {
        final Map<Periode, ArbeidstidPeriodeInfo> periodeMap = SøknadsammenslåerUtils.slåSammenOgHåndterTrukkedeKrav(s2Ytelse, t1, t2);
        // Defensiv kopiering av ArbeidstidInfo for å få nye Periode- og ArbeidstidPeriodeInfo-objekter:
        return new ArbeidstidInfo(new ArbeidstidInfo().medPerioder(periodeMap));
    }

    @SuppressWarnings("unchecked")
    private static LocalDateTimeline<ArbeidstidPeriodeInfo> hentTidslinje(final Map<Object, LocalDateTimeline<ArbeidstidPeriodeInfo>> arbeidstakertid1, Object arbeidstakerIdent) {
        final LocalDateTimeline<ArbeidstidPeriodeInfo> tidslinje = arbeidstakertid1.get(arbeidstakerIdent);
        if (tidslinje == null) {
            return LocalDateTimeline.EMPTY_TIMELINE;
        }
        return tidslinje;
    }

    private static Map<Object, LocalDateTimeline<ArbeidstidPeriodeInfo>> byggTidslinjeMap(Arbeidstid arbeidstid) {
        return arbeidstid.getArbeidstakerList().stream()
            .map(a -> {
                final LocalDateTimeline<ArbeidstidPeriodeInfo> tidslinje = lagArbeidstidTidslinje(a.getArbeidstidInfo());
                return Map.entry(hentArbeidsgiverIdent(a),tidslinje);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SuppressWarnings("unchecked")
    private static LocalDateTimeline<ArbeidstidPeriodeInfo> lagArbeidstidTidslinje(ArbeidstidInfo arbeidstidInfo) {
        if (arbeidstidInfo == null) {
            return LocalDateTimeline.EMPTY_TIMELINE;
        }
        return SøknadsammenslåerUtils.lagTidslinje(arbeidstidInfo.getPerioder());
    }

    private static Object hentArbeidsgiverIdent(Arbeidstaker a) {
        return a.getOrganisasjonsnummer() != null ? a.getOrganisasjonsnummer() : a.getNorskIdentitetsnummer();
    }
}
