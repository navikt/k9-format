#!/bin/bash

repositoryId=github
url=https://maven.pkg.github.com/navikt/k9-format
groupId=no.nav.k9
version=$(./scripts/get-version.sh)

deploy() {
    if [ "$1" == "pom" ]; then
        pomFile=pom.xml
        file=pom.xml
    else
        pomFile=$2/pom.xml
        file=$2/target/$2-$version.$1
    fi

    ./mvnw --settings .github/settings.xml deploy:deploy-file \
      -DgroupId=$groupId \
      -DartifactId=$2 \
      -Dversion=$version \
      -Dpackaging=$1 \
      -Dfile=$file \
      -DrepositoryId=$repositoryId \
      -Durl=$url \
      -DpomFile=$pomFile
}

deploy pom k9-format 
deploy jar soknad-pleiepenger-barn
