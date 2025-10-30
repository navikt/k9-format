package no.nav.k9.innsyn.inntektsmelding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.Valid;
import no.nav.k9.innsyn.InnsynHendelseData;
import no.nav.k9.innsyn.sak.FagsakYtelseType;
import no.nav.k9.innsyn.sak.Saksnummer;

@JsonTypeName(InnsynHendelseData.INNTEKTSMELDINGER)
public record Inntektsmelding(
        @JsonProperty(value = "status", required = true)
        InntektsmeldingStatus status,

        @JsonProperty(value = "saksnummer", required = true)
        @Valid
        Saksnummer saksnummer,

        @JsonProperty(value = "graderinger", required = true)
        @Valid
        List<Gradering> graderinger,

        @JsonProperty(value = "naturalYtelser", required = true)
        @Valid
        List<NaturalYtelse> naturalYtelser,

        @JsonProperty(value = "utsettelsePerioder", required = true)
        @Valid
        List<UtsettelsePeriode> utsettelsePerioder,

        @JsonProperty(value = "arbeidsgiver", required = true)
        @Valid
        Arbeidsgiver arbeidsgiver,

        @JsonProperty(value = "startDatoPermisjon")
        LocalDate startDatoPermisjon,

        @JsonProperty(value = "oppgittFravær", required = true)
        @Valid
        List<PeriodeAndel> oppgittFravær,

        @JsonProperty(value = "nærRelasjon", required = true)
        boolean nærRelasjon,

        @JsonProperty(value = "journalpostId", required = true)
        @Valid
        JournalpostId journalpostId,

        @JsonProperty(value = "mottattDato", required = true)
        LocalDate mottattDato,

        @JsonProperty(value = "inntektBeløp", required = true)
        @Valid
        Beløp inntektBeløp,

        @JsonProperty(value = "refusjonBeløpPerMnd", required = true)
        @Valid
        Beløp refusjonBeløpPerMnd,

        @JsonProperty(value = "refusjonOpphører", required = true)
        LocalDate refusjonOpphører,

        @JsonProperty(value = "innsendingstidspunkt", required = true)
        LocalDateTime innsendingstidspunkt,

        @JsonProperty(value = "kildesystem", required = true)
        String kildesystem,

        @JsonProperty(value = "inntektsmeldingType", required = true)
        InntektsmeldingType inntektsmeldingType,

        @JsonProperty(value = "endringerRefusjon", required = true)
        @Valid
        List<Refusjon> endringerRefusjon,

        @JsonProperty(value = "innsendingsårsak", required = true)
        InntektsmeldingInnsendingsårsak innsendingsårsak,

        @JsonProperty(value = "ytelseType", required = true)
        FagsakYtelseType ytelseType,

        @JsonProperty(value = "erstattetAv", required = true)
        @Valid
        List<JournalpostId> erstattetAv

) implements InnsynHendelseData {
}

