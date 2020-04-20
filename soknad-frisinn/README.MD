# Søknad for Frisinn (FRIlanser og Selvstendig næringsdrivendes INNtektskompensasjon)

## Eksempel søknad (fra test case)

```
{
  "søknadId" : "100-abc",
  "søknadsperiode" : "2020-04-01/2020-04-30",
  "versjon" : "1.0.0",
  "mottattDato" : "2020-04-20T07:15:36.124Z",
  "søker" : {
    "norskIdentitetsnummer" : "12345678901"
  },
  "inntekter" : {
    "frilanser" : {
      "inntekterSøknadsperiode" : {
        "2020-04-01/2020-04-21" : {
          "beløp" : "100000.00"
        }
      }
    },
    "selvstendig" : {
      "inntekterFør" : {
        "../2020-02-21" : {
          "beløp" : "100000.00"
        },
        "2020-02-22/2020-03-12" : {
          "beløp" : "100000.00"
        }
      },
      "inntekterSøknadsperiode" : {
        "2020-04-05/2020-04-16" : {
          "beløp" : "0.00"
        },
        "2020-04-17/2020-04-30" : {
          "beløp" : "10000.00"
        }
      }
    }
  },
  "språk" : "nb"
}


```
