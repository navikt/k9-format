package no.nav.k9.innsyn.inntektsmelding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.innsyn.InnsynHendelseData;
import no.nav.k9.innsyn.sak.AktørId;
import no.nav.k9.innsyn.sak.FagsakYtelseType;
import no.nav.k9.innsyn.sak.Saksnummer;

@JsonTypeName(InnsynHendelseData.INNTEKTSMELDING)
public record Inntektsmelding(

        @JsonProperty(value = "søkerAktørId", required = true)
        @NotNull
        AktørId søkerAktørId,

        @JsonProperty(value = "fagsakYtelseType", required = true)
        @NotNull
        FagsakYtelseType fagsakYtelseType,

        @JsonProperty(value = "status", required = true)
        @NotNull
        InntektsmeldingStatus status,

        @JsonProperty(value = "saksnummer", required = true)
        @NotNull
        @Valid
        Saksnummer saksnummer,

        @JsonProperty(value = "innsendingstidspunkt", required = true)
        @NotNull
        LocalDateTime innsendingstidspunkt,

        @JsonProperty(value = "kildesystem", required = true)
        @NotNull
        String kildesystem,

        @JsonProperty(value = "arbeidsgiver", required = true)
        @NotNull
        @Valid
        Arbeidsgiver arbeidsgiver,

        @JsonProperty(value = "nærRelasjon", required = true)
        @NotNull
        boolean nærRelasjon,

        @JsonProperty(value = "journalpostId", required = true)
        @NotNull
        @Valid
        JournalpostId journalpostId,

        @JsonProperty(value = "mottattDato", required = true)
        @NotNull
        LocalDate mottattDato,

        @JsonProperty(value = "inntektBeløp", required = true)
        @NotNull
        @Valid
        Beløp inntektBeløp,

        @JsonProperty(value = "innsendingsårsak", required = true)
        @NotNull
        InntektsmeldingInnsendingsårsak innsendingsårsak,

        @JsonProperty(value = "erstattetAv", required = true)
        @NotNull
        List<@Valid JournalpostId> erstattetAv,

        // Nesten ingen inntektsmeldinger har graderinger. Kun 10 i hele prod databasen per 4.10.25
        @JsonProperty(value = "graderinger")
        List<@Valid Gradering> graderinger,

        @JsonProperty(value = "naturalYtelser")
        List<@Valid NaturalYtelse> naturalYtelser,

        @JsonProperty(value = "utsettelsePerioder")
        List<@Valid UtsettelsePeriode> utsettelsePerioder,

        @JsonProperty(value = "startDatoPermisjon")
        LocalDate startDatoPermisjon,

        @JsonProperty(value = "oppgittFravær")
        List<@Valid PeriodeAndel> oppgittFravær,

        @JsonProperty(value = "refusjonBeløpPerMnd")
        @Valid
        Beløp refusjonBeløpPerMnd,

        @JsonProperty(value = "refusjonOpphører")
        LocalDate refusjonOpphører,

        @JsonProperty(value = "inntektsmeldingType")
        InntektsmeldingType inntektsmeldingType,

        @JsonProperty(value = "endringerRefusjon")
        List<@Valid Refusjon> endringerRefusjon

        /* ytelseType på inntektsmeldingen er alltid null fordi den ikke lagres i k9-abakus.*/

) implements InnsynHendelseData {
}

