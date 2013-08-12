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
BUILDIR=tmp/$BUILD
CLBASELIBDIR=../CLBaseLib

rm build/trupax$1_linux64.zip
rm build/trupax$1_linux32.zip
rm build/trupax$1_win64.zip
rm build/trupax$1_win32.zip
rm -rf $BUILDIR
mkdir -p $BUILDIR

cp $TRUPAXJAR                           $BUILDIR/
cp LICENSE                              $BUILDIR/
cp lib/fat32-lib-0.6.2.jar              $BUILDIR/
cp etc/README.txt                       $BUILDIR/README
cp etc/LIESMICH.txt                     $BUILDIR/LIESMICH
cp etc/trupax.desktop                   $BUILDIR/
cp etc/license_3rd_party.txt            $BUILDIR/license_3rd_party
cp etc/trupaxcmd_example_config.txt     $BUILDIR/
cp etc/scripts/trupax                   $BUILDIR/
cp etc/scripts/trupaxgui                $BUILDIR/
cp etc/scripts/install.sh               $BUILDIR/
cp etc/scripts/uninstall.sh             $BUILDIR/
cp etc/images/tpicon_256x256.png        $BUILDIR/trupax_icon.png 

chmod 755 $BUILDIR/trupax
chmod 755 $BUILDIR/trupaxgui
chmod 755 $BUILDIR/install.sh
chmod 755 $BUILDIR/uninstall.sh

cp $CLBASELIBDIR/lib/swt/4.3/gtk-linux-x86/swt.jar $BUILDIR/
cd tmp
zip -9 -r -X ../build/trupax$1_linux32.zip $BUILD
unzip -v     ../build/trupax$1_linux32.zip
cd ..

cp $CLBASELIBDIR/lib/swt/4.3/gtk-linux-x86_64/swt.jar $BUILDIR/
cd tmp
zip -9 -r -X ../build/trupax$1_linux64.zip $BUILD
unzip -v     ../build/trupax$1_linux64.zip
cd ..

rm $BUILDIR/trupax
rm $BUILDIR/trupaxgui
rm $BUILDIR/trupax_icon.png
rm $BUILDIR/trupax.desktop
rm $BUILDIR/*.sh
rm $BUILDIR/README
rm $BUILDIR/LIESMICH
rm $BUILDIR/LICENSE
rm $BUILDIR/license_3rd_party

cp LICENSE                              $BUILDIR/LICENSE.txt
cp etc/README.txt                       $BUILDIR/
cp etc/LIESMICH.txt                     $BUILDIR/
cp etc/license_3rd_party.txt            $BUILDIR/
cp etc/scripts/trupax*.cmd              $BUILDIR/
cp etc/scripts/install*.vbs             $BUILDIR/
cp etc/images/trupax_all.ico            $BUILDIR/trupax.ico 

cp $CLBASELIBDIR/lib/swt/4.3/win32-win32-x86/swt.jar $BUILDIR/
cd tmp
zip -9 -r -X ../build/trupax$1_win32.zip $BUILD
unzip -v     ../build/trupax$1_win32.zip
cd ..

cp $CLBASELIBDIR/lib/swt/4.3/win32-win32-x86_64/swt.jar $BUILDIR/
cd tmp
zip -9 -r -X ../build/trupax$1_win64.zip $BUILD
unzip -v     ../build/trupax$1_win64.zip
cd ..

rm -rf tmp

ls -lah   build/trupax$1_linux64.zip
ls -lah   build/trupax$1_linux32.zip
ls -lah   build/trupax$1_win64.zip
ls -lah   build/trupax$1_win32.zip
sha256sum build/trupax$1_linux64.zip
sha256sum build/trupax$1_linux32.zip
sha256sum build/trupax$1_win64.zip
sha256sum build/trupax$1_win32.zip

