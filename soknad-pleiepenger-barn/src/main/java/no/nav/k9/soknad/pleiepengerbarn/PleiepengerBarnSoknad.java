package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.*;
import no.nav.k9.soknad.felles.*;
import java.time.ZonedDateTime;

public class PleiepengerBarnSoknad {

    @JsonProperty(required = true)
    private String versjon = "0.1.1";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private ZonedDateTime mottattDato;

    @JsonProperty(required = true)
    private Soker soker;

    private Medsoker medsoker;

    @JsonProperty(required = true)
    private Periode periode;

    private String relasjonTilBarnet;

    @JsonProperty(required = true)
    private Barn barn;

    @JsonProperty(required = true)
    private Medlemskap medlemskap;

    @JsonProperty(required = true)
    private Beredskap beredskap;

    @JsonProperty(required = true)
    private Nattevaak nattevaak;


    public PleiepengerBarnSoknad() {
        this.soker = new Soker();
        this.periode = new Periode();
        this.barn = new Barn();
        this.medlemskap = new Medlemskap();
        this.beredskap = new Beredskap();
        this.nattevaak = new Nattevaak();
    }


    public void setVersjon(String versjon) {
        if (versjon == null) {
            throw new IllegalArgumentException("versjon == null");
        }
        this.versjon = versjon;
    }

    public String getVersjon() {
        return versjon;
    }

    public void setMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = mottattDato;
    }

    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    public void setSoker(Soker soker) {
        if (soker == null) {
            throw new IllegalArgumentException("soker == null");
        }
        this.soker = soker;
    }

    public Soker getSoker() {
        return soker;
    }

    public void setPeriode(Periode periode) {
        if (periode == null) {
            throw new IllegalArgumentException("periode == null");
        }
        this.periode = periode;
    }

    public Periode getPeriode() {
        return periode;
    }

    public void setRelasjonTilBarnet(String relasjonTilBarnet) {
        this.relasjonTilBarnet = relasjonTilBarnet;
    }

    public void setBarn(Barn barn) {
        if (barn == null) {
            throw new IllegalArgumentException("barn == null");
        }
        this.barn = barn;
    }

    public Barn getBarn() {
        return barn;
    }

    public String getRelasjonTilBarnet() {
        return relasjonTilBarnet;
    }

    public void setMedsoker(Medsoker medsoker) {
        this.medsoker = medsoker;
    }

    public Medsoker getMedsoker() {
        return medsoker;
    }

    public void setMedlemskap(Medlemskap medlemskap) {
        if (medlemskap == null) {
            throw new IllegalArgumentException("medlemskap == null");
        }
        this.medlemskap = medlemskap;
    }

    public Medlemskap getMedlemskap() {
        return medlemskap;
    }

    public void setBeredskap(Beredskap beredskap) {
        if (beredskap == null) {
            throw new IllegalArgumentException("beredskap == null");
        }
        this.beredskap = beredskap;
    }

    public Beredskap getBeredskap() {
        return beredskap;
    }

    public void setNattevaak(Nattevaak nattevaak) {
        if (nattevaak == null) {
            throw new IllegalArgumentException("nattevaak == null");
        }
        this.nattevaak = nattevaak;
    }

    public Nattevaak getNattevaak() {
        return nattevaak;
    }
}
