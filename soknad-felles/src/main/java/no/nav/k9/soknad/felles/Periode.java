package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Periode {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    private LocalDate fraOgMed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    private LocalDate tilOgMed;


    public Periode() {

    }

    public Periode(LocalDate fraOgMed, LocalDate tilOgMed) {
        this.fraOgMed = fraOgMed;
        this.tilOgMed = tilOgMed;
    }


    public void setFraOgMed(LocalDate fraOgMed) {
        this.fraOgMed = fraOgMed;
    }

    public LocalDate getFraOgMed() {
        return fraOgMed;
    }

    public void setTilOgMed(LocalDate tilOgMed) {
        this.tilOgMed = tilOgMed;
    }

    public LocalDate getTilOgMed() {
        return tilOgMed;
    }
}
