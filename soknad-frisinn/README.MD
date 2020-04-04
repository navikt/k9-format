# Søknad for Frisinn (FRIlanser og Selvstendig næringsdrivendes INNtektskompensasjon)

## Eksempel søknad (fra test case)

```
{
  "søknadId" : "10-abc-def",
  "søknadsperiode" : "2020-03-01/2020-03-31",
  "versjon" : "1.0.0",
  "mottattDato" : "2019-10-20T07:15:36.124Z",
  "søker" : {
    "norskIdentitetsnummer" : "12345678901"
  },
  "inntekter" : {
    "frilanser" : {
      "inntektstapStartet" : "2020-04-04",
      "inntekterFør" : {
        "2020-03-15/2020-04-03" : {
          "beløp" : "1000000.00"
        }
      },
      "inntekterEtter" : {
        "2020-04-05/2020-04-24" : {
          "beløp" : "1000000.00"
        }
      }
    },
    "selvstendig" : [ {
      "orgnummer" : "123",
      "inntektstapStartet" : "2020-04-04",
      "inntekterFør" : {
        "2020-03-15/2020-04-03" : {
          "beløp" : "1000000.00"
        }
      },
      "inntekterEtter" : {
        "2020-04-05/2020-04-24" : {
          "beløp" : "1000000.00"
        }
      }
    } ]
  },
  "språk" : "nb"
}
```
