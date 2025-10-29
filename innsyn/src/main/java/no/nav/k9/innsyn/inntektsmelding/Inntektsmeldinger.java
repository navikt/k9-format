package no.nav.k9.innsyn.inntektsmelding;

import java.util.List;

import jakarta.validation.Valid;
import no.nav.k9.innsyn.InnsynHendelseData;

public record Inntektsmeldinger(@Valid List<Inntektsmelding> inntektsmeldinger) implements InnsynHendelseData {}
