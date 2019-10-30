#!/bin/bash

versionLine="$(grep '<revision>' pom.xml)"
version="$(echo $versionLine | sed 's/<revision>\(.*\)<\/revision>/\1/g')"
sha="$(git rev-parse --short HEAD)"
echo "${version}+${sha}"
