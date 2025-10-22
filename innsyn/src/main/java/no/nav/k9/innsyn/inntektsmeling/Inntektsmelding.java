package no.nav.k9.innsyn.inntektsmeling;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.innsyn.InnsynHendelseData;
import no.nav.k9.innsyn.sak.FagsakYtelseType;
import no.nav.k9.innsyn.sak.Saksnummer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonTypeName(InnsynHendelseData.INNTEKTSMELDING)
public record Inntektsmelding(
        @JsonProperty(value = "status", required = true)
        @NotNull
        InntektsmeldingStatus status,

        @JsonProperty(value = "saksnummer", required = true)
        @Valid
        @NotNull
        Saksnummer saksnummer,

        @JsonProperty(value = "graderinger", required = true)
        @Valid
        @NotNull
        List<Gradering> graderinger,

        @JsonProperty(value = "naturalYtelser", required = true)
        @Valid
        @NotNull
        List<NaturalYtelse> naturalYtelser,

        @JsonProperty(value = "utsettelsePerioder", required = true)
        @Valid
        @NotNull
        List<UtsettelsePeriode> utsettelsePerioder,

        @JsonProperty(value = "arbeidsgiver", required = true)
        @Valid
        @NotNull
        Arbeidsgiver arbeidsgiver,

        @JsonProperty(value = "startDatoPermisjon", required = true)
        @NotNull
        LocalDate startDatoPermisjon,

        @JsonProperty(value = "oppgittFravær", required = true)
        @Valid
        @NotNull
        List<PeriodeAndel> oppgittFravær,

        @JsonProperty(value = "nærRelasjon", required = true)
        @NotNull
        boolean nærRelasjon,

        @JsonProperty(value = "journalpostId", required = true)
        @Valid
        @NotNull
        JournalpostId journalpostId,

        @JsonProperty(value = "mottattDato", required = true)
        @NotNull
        LocalDate mottattDato,

        @JsonProperty(value = "inntektBeløp", required = true)
        @Valid
        @NotNull
        Beløp inntektBeløp,

        @JsonProperty(value = "refusjonBeløpPerMnd", required = true)
        @Valid
        @NotNull
        Beløp refusjonBeløpPerMnd,

        @JsonProperty(value = "refusjonOpphører", required = true)
        @NotNull
        LocalDate refusjonOpphører,

        @JsonProperty(value = "innsendingstidspunkt", required = true)
        @NotNull
        LocalDateTime innsendingstidspunkt,

        @JsonProperty(value = "kildesystem", required = true)
        String kildesystem,

        @JsonProperty(value = "inntektsmeldingType", required = true)
        InntektsmeldingType inntektsmeldingType,

        @JsonProperty(value = "endringerRefusjon", required = true)
        @Valid
        @NotNull
        List<Refusjon> endringerRefusjon,

        @JsonProperty(value = "innsendingsårsak", required = true)
        InntektsmeldingInnsendingsårsak innsendingsårsak,

        @JsonProperty(value = "ytelseType", required = true)
        FagsakYtelseType ytelseType,

        @JsonProperty(value = "erstattetAv", required = true)
        @Valid
        @NotNull
        List<JournalpostId> erstattetAv

) implements InnsynHendelseData {
}

