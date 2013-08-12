#!/bin/bash
if [ $# -eq 0 ]
then
  echo "usage: $0 version {release}"
  echo "example: $0 3 B"
  exit 1
fi
VERSION=$1
VERREL=$1
RELEASE_PRG='null;'
RELEASE_VER='0,0'
if [ $# -ge 2 ]
then
  VERREL="$VERSION $2"
  RELEASE_PRG="\"$2\";"
fi
FNAME=src/coderslagoon/trupax/lib/prg/Prg.java
sed -i -r -e "s/(int\ VERSION\ = ).*;/\1$VERSION;/"       $FNAME
sed -i -r -e "s/(String\ RELEASE\ = ).*$/\1$RELEASE_PRG/" $FNAME
FNAME=MANIFEST.MF
sed -i -r -e "s/(Specification-Version:\ ).*/\1$VERREL/"  $FNAME
sed -i -r -e "s/(Implementation-Version:\ ).*/\1$VERREL/" $FNAME

