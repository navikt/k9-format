package no.nav.k9.innsyn;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;

public class SøknadsammenslåerTest {

    @Test
    public void kanSlåSammenDataForEttArbeidsforhold() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        final String ORGANISASJONSNUMMER = "987654321";
        ytelse1.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(
                new Arbeidstaker()
                    .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER))
                    .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 10, 11)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(4))
                    )))
            ))
        );
        
        final Søknad søknad2 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse2 = søknad2.getYtelse();
        ytelse2.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(
                new Arbeidstaker()
                    .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER))
                    .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30))
                    )))
            ))
        );
        
        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, søknad2);
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();
        assertThat(resultatYtelse.getArbeidstid().getArbeidstakerList().size()).isEqualTo(1);
        
        final Arbeidstaker resultatArbeidstaker = resultatYtelse.getArbeidstid().getArbeidstakerList().get(0);
        assertResultet(resultatArbeidstaker, ORGANISASJONSNUMMER, Map.of(
            new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 24)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(4)).medJobberNormaltTimerPerDag(Duration.ofHours(8)),
            new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));
    }
    
    @Test
    public void kanSlåSammenDataForFrilanser() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        ytelse1.medArbeidstid(new Arbeidstid().medFrilanserArbeidstid(
            new ArbeidstidInfo().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 10, 11)),
                new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(4))
            ))
        ));
        
        final Søknad søknad2 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse2 = søknad2.getYtelse();
        ytelse2.medArbeidstid(new Arbeidstid().medFrilanserArbeidstid(
            new ArbeidstidInfo().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30))
            ))
        ));
        
        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, søknad2);
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();
        
        assertResultet(resultatYtelse.getArbeidstid().getFrilanserArbeidstidInfo().get().getPerioder(), Map.of(
            new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 24)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(4)).medJobberNormaltTimerPerDag(Duration.ofHours(8)),
            new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));
    }
    
    @Test
    public void kanSlåSammenDataForSelvstendigNæringsdrivende() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        ytelse1.medArbeidstid(new Arbeidstid().medSelvstendigNæringsdrivendeArbeidstidInfo(
            new ArbeidstidInfo().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 10, 11)),
                new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(4))
            ))
        ));
        
        final Søknad søknad2 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse2 = søknad2.getYtelse();
        ytelse2.medArbeidstid(new Arbeidstid().medSelvstendigNæringsdrivendeArbeidstidInfo(
            new ArbeidstidInfo().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30))
            ))
        ));
        
        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, søknad2);
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();
        
        assertResultet(resultatYtelse.getArbeidstid().getSelvstendigNæringsdrivendeArbeidstidInfo().get().getPerioder(), Map.of(
            new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 24)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(4)).medJobberNormaltTimerPerDag(Duration.ofHours(8)),
            new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));
    }
    
    @Test
    public void kanSlåSammenDataForFlereArbeidsforhold() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        final String ORGANISASJONSNUMMER1 = "911111111";
        final String ORGANISASJONSNUMMER2 = "922222222";
        final String ORGANISASJONSNUMMER3 = "933333333";
        final String ORGANISASJONSNUMMER4 = "944444444";
        ytelse1.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(
                new Arbeidstaker()
                    .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER1))
                    .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 10, 11)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(4))
                    ))),
                new Arbeidstaker()
                    .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER2))
                    .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 8, 5), LocalDate.of(2021, 9, 12)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(4))
                    ))),
                new Arbeidstaker()
                    .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER3))
                    .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 6, 1), LocalDate.of(2021, 6, 10)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(3))
                    )))
            ))
        );
        
        final Søknad søknad2 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse2 = søknad2.getYtelse();
        ytelse2.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(
                new Arbeidstaker()
                    .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER1))
                    .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30))
                    ))),
                new Arbeidstaker()
                    .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER2))
                    .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30))
                    ))),
                new Arbeidstaker()
                    .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER4))
                    .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 11, 3), LocalDate.of(2021, 11, 15)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(2))
                    )))
            ))
        );
        
        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, søknad2);
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();
        final List<Arbeidstaker> resultatArbeidstakere = sortertArbeidstakere(resultatYtelse);
        assertThat(resultatArbeidstakere.size()).isEqualTo(4);
        
        assertResultet(resultatArbeidstakere.get(0), ORGANISASJONSNUMMER1, Map.of(
            new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 24)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(4)).medJobberNormaltTimerPerDag(Duration.ofHours(8)),
            new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));
        
        assertResultet(resultatArbeidstakere.get(1), ORGANISASJONSNUMMER2, Map.of(
            new Periode(LocalDate.of(2021, 8, 5), LocalDate.of(2021, 9, 12)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(4)).medJobberNormaltTimerPerDag(Duration.ofHours(8)),
            new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));
        
        assertResultet(resultatArbeidstakere.get(2), ORGANISASJONSNUMMER3, Map.of(
            new Periode(LocalDate.of(2021, 6, 1), LocalDate.of(2021, 6, 10)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(3)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));
        
        assertResultet(resultatArbeidstakere.get(3), ORGANISASJONSNUMMER4, Map.of(
            new Periode(LocalDate.of(2021, 11, 3), LocalDate.of(2021, 11, 15)),
            new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(2)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));
    }

    private List<Arbeidstaker> sortertArbeidstakere(final PleiepengerSyktBarn resultatYtelse) {
        return resultatYtelse.getArbeidstid()
                .getArbeidstakerList()
                .stream()
                .sorted((a, b) -> a.getOrganisasjonsnummer().getVerdi().compareTo(b.getOrganisasjonsnummer().getVerdi()))
                .collect(Collectors.toList());
    }
    
    private void assertResultet(Arbeidstaker actual, String expectedOrganisasjonsnummer, Map<Periode, ArbeidstidPeriodeInfo> expectedPerioder) {
        assertThat(actual.getOrganisasjonsnummer()).isEqualTo(Organisasjonsnummer.of(expectedOrganisasjonsnummer));
        assertResultet(actual.getArbeidstidInfo().getPerioder(), expectedPerioder);
    }
    
    private void assertResultet(Map<Periode, ArbeidstidPeriodeInfo> actualPerioder, Map<Periode, ArbeidstidPeriodeInfo> expectedPerioder) {
        assertThat(actualPerioder.size()).isEqualTo(expectedPerioder.size());
        for (Entry<Periode, ArbeidstidPeriodeInfo> p : expectedPerioder.entrySet()) {
            final ArbeidstidPeriodeInfo actualArbeidstidPeriodeInfo = actualPerioder.get(p.getKey());
            assertThat(actualArbeidstidPeriodeInfo).isNotNull();
            assertThat(actualArbeidstidPeriodeInfo.getFaktiskArbeidTimerPerDag()).isEqualTo(p.getValue().getFaktiskArbeidTimerPerDag());
            assertThat(actualArbeidstidPeriodeInfo.getJobberNormaltTimerPerDag()).isEqualTo(p.getValue().getJobberNormaltTimerPerDag());
        }
    }

    private Søknad lagGyldigSøknad() {
        var ytelse = new PleiepengerSyktBarn().medBarn(new Barn().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("22211111111")));
        var søknad = new Søknad(
                SøknadId.of("lala"),
                Versjon.of("1.0.0"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("22222222222")),
                ytelse);
        return søknad;
    }
}
