package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import no.nav.k9.innsyn.InnsynHendelseData;
import no.nav.k9.innsyn.sak.AktørId;
import no.nav.k9.innsyn.sak.FagsakYtelseType;
import no.nav.k9.innsyn.sak.Saksnummer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonTypeName(InnsynHendelseData.INNTEKTSMELDING)
public record Inntektsmelding(

        @JsonProperty(value = "søkerAktørId", required = true)
        AktørId søkerAktørId,

        @JsonProperty(value = "fagsakYtelseType", required = true)
        FagsakYtelseType fagsakYtelseType,

        @JsonProperty(value = "status", required = true)
        InntektsmeldingStatus status,

        @JsonProperty(value = "saksnummer", required = true)
        @Valid
        Saksnummer saksnummer,

        @JsonProperty(value = "graderinger")
        @Valid
        List<Gradering> graderinger,

        @JsonProperty(value = "naturalYtelser")
        @Valid
        List<NaturalYtelse> naturalYtelser,

        @JsonProperty(value = "utsettelsePerioder")
        @Valid
        List<UtsettelsePeriode> utsettelsePerioder,

        @JsonProperty(value = "arbeidsgiver", required = true)
        @Valid
        Arbeidsgiver arbeidsgiver,

        @JsonProperty(value = "startDatoPermisjon")
        LocalDate startDatoPermisjon,

        @JsonProperty(value = "oppgittFravær")
        @Valid
        List<PeriodeAndel> oppgittFravær,

        @JsonProperty(value = "nærRelasjon")
        boolean nærRelasjon,

        @JsonProperty(value = "journalpostId", required = true)
        @Valid
        JournalpostId journalpostId,

        @JsonProperty(value = "mottattDato", required = true)
        LocalDate mottattDato,

        @JsonProperty(value = "inntektBeløp", required = true)
        @Valid
        Beløp inntektBeløp,

        @JsonProperty(value = "refusjonBeløpPerMnd")
        @Valid
        Beløp refusjonBeløpPerMnd,

        @JsonProperty(value = "refusjonOpphører")
        LocalDate refusjonOpphører,

        @JsonProperty(value = "innsendingstidspunkt", required = true)
        LocalDateTime innsendingstidspunkt,

        @JsonProperty(value = "kildesystem", required = true)
        String kildesystem,

        @JsonProperty(value = "inntektsmeldingType")
        InntektsmeldingType inntektsmeldingType,

        @JsonProperty(value = "endringerRefusjon")
        @Valid
        List<Refusjon> endringerRefusjon,

        @JsonProperty(value = "innsendingsårsak")
        InntektsmeldingInnsendingsårsak innsendingsårsak,

        @JsonProperty(value = "imYtelseType")
        FagsakYtelseType imYtelseType,

        @JsonProperty(value = "erstattetAv")
        @Valid
        List<JournalpostId> erstattetAv

) implements InnsynHendelseData {
}

