#!/bin/bash
if [[ ! ("$#" -eq 1) ]]; then
    echo 'need version'
    exit 1
fi

TRUPAXJAR=build/trupax.jar
if [ ! -f $TRUPAXJAR ]
then
    echo "$TRUPAXJAR is missing"
    exit 2
fi

BUILD=TruPax$1
APPDIR=tmp/TruPax.app
CONTDIR=$APPDIR/Contents
CLBASELIBDIR=../CLBaseLib
DMGFILE=build/trupax$1_osx.dmg

rm $DMGFILE
rm -rf $CONTDIR
mkdir -p $CONTDIR/MacOS
mkdir    $CONTDIR/Resources

cp $TRUPAXJAR                         $CONTDIR/MacOS/
cp LICENSE                            $CONTDIR/MacOS/
cp lib/fat32-lib-0.6.2.jar            $CONTDIR/MacOS/
cp etc/scripts/trupaxgui_osx          $CONTDIR/MacOS/trupaxgui
cp etc/Info.plist                     $CONTDIR/
cp etc/images/trupax.icns             $CONTDIR/Resources/ 

chmod 755 $CONTDIR/MacOS/trupaxgui

cp $CLBASELIBDIR/lib/swt/4.3/cocoa-macosx-x86_64/swt.jar $CONTDIR/MacOS/

hdiutil create -srcfolder $APPDIR $DMGFILE
hdiutil internet-enable -yes $DMGFILE

rm -rf tmp

