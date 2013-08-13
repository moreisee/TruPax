TruPax
======

TruPax generates [TrueCrypt](http://www.truecrypt.org/) compatible container files from arbitrary sets of files and folders. Such files match exactly the size of the contained material and can be mounted via TrueCrypt, but also directly extracted by TruPax itself. Latter also works for containers formatted with FAT32 by TrueCrypt itself (thanks to [fat32-lib](http://code.google.com/p/fat32-lib/)). For all of that there are no administrator rights required when using TruPax.

The generated file system of the containers is UDF 1.02, which is supported by all of the modern operating systems. Most of them also support writing - meaning files in a container can also be deleted or new ones added. TruPax also wipes files after container generation, or just as a separate action. You also invalidate any TrueCrypt container with it very quickly.

TruPax is completely written in Java 6+.
Next to the SWT UI there is also a command line version, and with it TruPax can be used in fully automated scenarios.

TruPax works fast and also uses all available CPU cores. Containers get generated in just one pass.

The software is free to use and the source code available under the terms of the GPLv3. TruPax is a completely independent implementation of the TrueCrypt logic and shares not a single line of code with latter.

If you want to use the TruPax technology in your own applications, the API is the right starting point.

Contributors are highly welcome, please contact the maintainer for further information.

For build and development instructions please [check out the wiki](https://github.com/coderslagoon/TruPax/wiki).
