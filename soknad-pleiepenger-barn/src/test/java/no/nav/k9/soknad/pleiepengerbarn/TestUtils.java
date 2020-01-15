package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import no.nav.k9.soknad.felles.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class TestUtils {

    private TestUtils() {}

    static String jsonFromFile(String filename) {
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
                .soknadId(SoknadId.of(soknad.soknadId.id))
                .periode(Periode.builder()
                        .fraOgMed(soknad.periode.fraOgMed)
                        .tilOgMed(soknad.periode.tilOgMed)
                        .build())
                .mottattDato(soknad.mottattDato)
                .spraak(Spraak.of(soknad.spraak.dto))
                .soker(Soker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of(soknad.soker.norskIdentitetsnummer.verdi))
                        .build())
                .barn(Barn.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of((soknad.barn.norskIdentitetsnummer == null) ? null : soknad.barn.norskIdentitetsnummer.verdi))
                        .foedselsdato(soknad.barn.foedselsdato)
                        .build())
                .utland(Utland.builder()
                        .harBoddIUtlandetSiste12Mnd(soknad.utland.harBoddIUtlandetSiste12Mnd)
                        .skalBoIUtlandetNeste12Mnd(soknad.utland.skalBoIUtlandetNeste12Mnd)
                        .opphold(rekonstruerUtlandOpphold(soknad.utland.opphold))
                        .build())
                .beredskap(Beredskap.builder()
                        .perioder(rekonstruerBeredskapPerioder(soknad.beredskap.perioder))
                        .build())
                .nattevaak(Nattevaak.builder()
                        .perioder(rekonstruerNattevaakPerioder(soknad.nattevaak.perioder))
                        .build())
                .tilsynsordning(Tilsynsordning.builder()
                        .iTilsynsordning(TilsynsordningSvar.of(soknad.tilsynsordning.iTilsynsordning.dto))
                        .opphold(rekonstruerTilsynsordningOpphold(soknad.tilsynsordning.opphold))
                        .build())
                .arbeid(Arbeid.builder()
                        .arbeidstaker(rekonstruerArbeidstaker(soknad.arbeid.arbeidstaker))
                        .selvstendigNæringsdrivende(rekonstruerSelvstendigNæringsdrivende(soknad.arbeid.selvstendigNæringsdrivende))
                        .frilanser(rekonstruerFrilanser(soknad.arbeid.frilanser))
                        .build());
    }

    private static Map<Periode, UtlandOpphold> rekonstruerUtlandOpphold(
            Map<Periode, UtlandOpphold> opphold) {
        Map<Periode, UtlandOpphold> fraBuilder = new HashMap<>();
        opphold.forEach((periode, info) -> fraBuilder.put(
                Periode.builder().fraOgMed(periode.fraOgMed).tilOgMed(periode.tilOgMed).build(),
                UtlandOpphold.builder().land(Landkode.of(info.land.landkode)).build()
        ));
        return fraBuilder;
    }

    private static Map<Periode, Beredskap.BeredskapPeriodeInfo> rekonstruerBeredskapPerioder(
            Map<Periode, Beredskap.BeredskapPeriodeInfo> perioder) {
        Map<Periode, Beredskap.BeredskapPeriodeInfo> fraBuilder = new HashMap<>();
        perioder.forEach((periode, info) -> fraBuilder.put(
                Periode.builder().fraOgMed(periode.fraOgMed).tilOgMed(periode.tilOgMed).build(),
                Beredskap.BeredskapPeriodeInfo.builder().tilleggsinformasjon(info.tilleggsinformasjon).build()
        ));
        return fraBuilder;
    }

    private static Map<Periode, Nattevaak.NattevaakPeriodeInfo> rekonstruerNattevaakPerioder(
            Map<Periode, Nattevaak.NattevaakPeriodeInfo> perioder) {
        Map<Periode, Nattevaak.NattevaakPeriodeInfo> fraBuilder = new HashMap<>();
        perioder.forEach((periode, info) -> fraBuilder.put(
                Periode.builder().fraOgMed(periode.fraOgMed).tilOgMed(periode.tilOgMed).build(),
                Nattevaak.NattevaakPeriodeInfo.builder().tilleggsinformasjon(info.tilleggsinformasjon).build()
        ));
        return fraBuilder;
    }

    private static Map<Periode, TilsynsordningOpphold> rekonstruerTilsynsordningOpphold(
            Map<Periode, TilsynsordningOpphold> opphold) {
        Map<Periode, TilsynsordningOpphold> fraBuilder = new HashMap<>();
        opphold.forEach((periode, info) -> fraBuilder.put(
                Periode.builder().fraOgMed(periode.fraOgMed).tilOgMed(periode.tilOgMed).build(),
                TilsynsordningOpphold.builder().lengde(info.lengde).build()
        ));
        return fraBuilder;
    }

    private static List<Arbeidstaker> rekonstruerArbeidstaker(
            List<Arbeidstaker> arbeidstaker) {
        List<Arbeidstaker> fraBuilder = new ArrayList<>();
        arbeidstaker.forEach(a -> {
            Arbeidstaker.Builder builder = Arbeidstaker.builder();
            String organisasjonsnummer = (a.organisasjonsnummer == null) ? null : a.organisasjonsnummer.verdi;
            builder.organisasjonsnummer(Organisasjonsnummer.of(organisasjonsnummer));
            String norskIdentitetsnummer = (a.norskIdentitetsnummer == null) ? null : a.norskIdentitetsnummer.verdi;
            builder.norskIdentitetsnummer(NorskIdentitetsnummer.of(norskIdentitetsnummer));
            a.perioder.forEach((periode, info) -> builder.periode(
                    Periode.builder().fraOgMed(periode.fraOgMed).tilOgMed(periode.tilOgMed).build(),
                    Arbeidstaker.ArbeidstakerPeriodeInfo.builder().skalJobbeProsent(info.skalJobbeProsent).build()
            ));
            fraBuilder.add(builder.build());
        });
        return fraBuilder;
    }

    private static List<SelvstendigNæringsdrivende> rekonstruerSelvstendigNæringsdrivende(
            List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende) {
        List<SelvstendigNæringsdrivende> fraBuilder = new ArrayList<>();
        selvstendigNæringsdrivende.forEach(s -> {
            SelvstendigNæringsdrivende.Builder builder = SelvstendigNæringsdrivende.builder();
            s.perioder.forEach((periode, info) -> builder.periode(
                    Periode.builder().fraOgMed(periode.fraOgMed).tilOgMed(periode.tilOgMed).build(),
                    SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder().build()
            ));
            fraBuilder.add(builder.build());
        });
        return fraBuilder;
    }

    private static List<Frilanser> rekonstruerFrilanser(
            List<Frilanser> frilanser) {
        List<Frilanser> fraBuilder = new ArrayList<>();
        frilanser.forEach(f -> {
            Frilanser.Builder builder = Frilanser.builder();
            f.perioder.forEach((periode, info) -> builder.periode(
                    Periode.builder().fraOgMed(periode.fraOgMed).tilOgMed(periode.tilOgMed).build(),
                    Frilanser.FrilanserPeriodeInfo.builder().build()
            ));
            fraBuilder.add(builder.build());
        });
        return fraBuilder;
    }
}
