# bdjo

This tool reads an XML file description of the "BD-J Object"
file, and creates the binary file that's needed on a Blu-ray
disc image.  It can also read a binary file, and give you an
editable XML representation.  If you know MHP, the BDJO is kind of like MHP's
AIT.  It's the file that says what Java xlets are on the disc,
and what should be launched when.

This is .bdjo file format converter. This package contains classes for 
handling .bdjo files found in bluray disks. Refer to section 10.2 BD-J 
Object file of "System Description Blu-ray Disc Read-Only format" - 
Part 3 Audio Visual Basic Specifications.

Using this tool, you can write bdjo description as an XML document and convert 
the same into a binary .bdjo file.

## usage

    java -jar bdjo.jar 00000.xml 00000.bdjo
    java -jar bdjo.jar 00000.bdjo 00000.xml

bdjo also supports legacy JavaFX script (.fx).
