package no.nav.k9.innsyn.inntektsmelding;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.Valid;
import no.nav.k9.innsyn.InnsynHendelseData;

@JsonTypeName(InnsynHendelseData.INNTEKTSMELDINGER)
public record Inntektsmeldinger(@Valid List<Inntektsmelding> inntektsmeldinger) implements InnsynHendelseData {}
