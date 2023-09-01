package no.nav.k9.innsyn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie.LovbestemtFeriePeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

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


    @Test
    public void kanSlåSammenSøknaderMedDuplikatePerioderOgArbeidstidPåSammeArbeidstaker() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        final String ORGANISASJONSNUMMER1 = "911111111";
        final String ORGANISASJONSNUMMER2 = "933333333";

        Arbeidstaker duplikatArbeidstakerInfo = new Arbeidstaker()
                .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER1))
                .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                        new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 10, 11)),
                        new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(4))
                )));

        ytelse1.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(duplikatArbeidstakerInfo, duplikatArbeidstakerInfo)));

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
                                )))
                ))
        );

        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, søknad2);
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();
        final List<Arbeidstaker> resultatArbeidstakere = sortertArbeidstakere(resultatYtelse);
        assertThat(resultatArbeidstakere).hasSize(2);

        assertResultet(resultatArbeidstakere.get(0), ORGANISASJONSNUMMER1, Map.of(
                new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 24)),
                new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(4)).medJobberNormaltTimerPerDag(Duration.ofHours(8)),
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));

        assertResultet(resultatArbeidstakere.get(1), ORGANISASJONSNUMMER2, Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new ArbeidstidPeriodeInfo().medFaktiskArbeidTimerPerDag(Duration.ofHours(2).plusMinutes(30)).medJobberNormaltTimerPerDag(Duration.ofHours(8))
        ));
    }


    @Test
    public void kanIkkeSlåSammenSøknaderMedDuplikatePerioderMedUlikeArbeidstidPåSammeArbeidstaker() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        final String ORGANISASJONSNUMMER1 = "911111111";
        final String ORGANISASJONSNUMMER2 = "933333333";

        ytelse1.medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(
                new Arbeidstaker()
                        .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER1))
                        .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                                new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 10, 11)),
                                new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(8)).medFaktiskArbeidTimerPerDag(Duration.ofHours(4))
                        ))),
                new Arbeidstaker()
                        .medOrganisasjonsnummer(Organisasjonsnummer.of(ORGANISASJONSNUMMER1))
                        .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(Map.of(
                                new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 10, 11)),
                                new ArbeidstidPeriodeInfo().medJobberNormaltTimerPerDag(Duration.ofHours(6)).medFaktiskArbeidTimerPerDag(Duration.ofHours(3))
                        )))
        )));

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
                                )))
                ))
        );

        assertThrows(IllegalStateException.class, () -> Søknadsammenslåer.slåSammen(søknad1, søknad2));
    }

    @Test
    public void kanSlåSammenTilsyn() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        ytelse1.medTilsynsordning(new Tilsynsordning().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 10, 11)),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(4))
        )));

        final Søknad søknad2 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse2 = søknad2.getYtelse();
        ytelse2.medTilsynsordning(new Tilsynsordning().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(2).plusMinutes(30))
        )));
        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, Søknadsammenslåer.kunPleietrengendedata(søknad2));
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();

        assertResultet(resultatYtelse.getTilsynsordning().getPerioder(), Map.of(
                new Periode(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 24)),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(4)),
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(2).plusMinutes(30))
        ));
    }

    @Test
    public void kanSlåSammenLovbestemtFerieMedTomSøknad() {
        final Søknad søknad1 = lagGyldigSøknad();

        final Søknad søknad2 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse2 = søknad2.getYtelse();
        ytelse2.medLovbestemtFerie(new LovbestemtFerie().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true)
        )));

        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, søknad2);
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();

        assertResultet(resultatYtelse.getLovbestemtFerie().getPerioder(), Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true)
        ));
    }

    @Test
    public void kanSlåSammenLovbestemtFerieMedToSøknader() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        ytelse1.medLovbestemtFerie(new LovbestemtFerie().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true)
        )));

        final Søknad søknad2 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse2 = søknad2.getYtelse();
        ytelse2.medLovbestemtFerie(new LovbestemtFerie().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 9, 26)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(false),
                new Periode(LocalDate.of(2021, 9, 23), LocalDate.of(2021, 9, 24)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true)
        )));

        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, søknad2);
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();

        assertResultet(resultatYtelse.getLovbestemtFerie().getPerioder(), Map.of(
                new Periode(LocalDate.of(2021, 9, 23), LocalDate.of(2021, 9, 24)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true),
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 9, 26)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(false),
                new Periode(LocalDate.of(2021, 9, 27), LocalDate.of(2021, 12, 1)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true)
        ));
    }

    @Test
    public void kanSlåSammenLovbestemtFerieMedSøknadsperiodeutnulling() {
        final Søknad søknad1 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse1 = søknad1.getYtelse();
        ytelse1.medLovbestemtFerie(new LovbestemtFerie().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 12, 1)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true)
        )));

        final Søknad søknad2 = lagGyldigSøknad();
        final PleiepengerSyktBarn ytelse2 = søknad2.getYtelse();
        ytelse2.medLovbestemtFerie(new LovbestemtFerie().medPerioder(Map.of(
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 9, 26)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(false),
                new Periode(LocalDate.of(2021, 9, 23), LocalDate.of(2021, 9, 24)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true)
        )));
        ytelse2.medSøknadsperiode(new Periode(LocalDate.of(2021, 10, 2), LocalDate.of(2021, 10, 3)));

        final Søknad resultat = Søknadsammenslåer.slåSammen(søknad1, søknad2);
        final PleiepengerSyktBarn resultatYtelse = resultat.getYtelse();

        assertResultet(resultatYtelse.getLovbestemtFerie().getPerioder(), Map.of(
                new Periode(LocalDate.of(2021, 9, 23), LocalDate.of(2021, 9, 24)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true),
                new Periode(LocalDate.of(2021, 9, 25), LocalDate.of(2021, 9, 26)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(false),
                new Periode(LocalDate.of(2021, 9, 27), LocalDate.of(2021, 10, 1)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true),
                new Periode(LocalDate.of(2021, 10, 4), LocalDate.of(2021, 12, 1)),
                new LovbestemtFeriePeriodeInfo().medSkalHaFerie(true)
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

    private <T> void assertResultet(Map<Periode, T> actualPerioder, Map<Periode, T> expectedPerioder) {
        assertThat(actualPerioder.size()).isEqualTo(expectedPerioder.size());
        for (Entry<Periode, T> p : expectedPerioder.entrySet()) {
            final T data = actualPerioder.get(p.getKey());
            assertThat(data).isNotNull();
            assertThat(data).isEqualTo(p.getValue());
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
