fqzip
=====

fqzip is a Java command line program which compresses FASTQ files using the following methods:
* BZIP2 for sequence data
* PPM + Huffman for quality data
* Efficient superblock encoding and BZIP compression for header data

FQZip uses a premade Huffman tree index for the PPM quality compression. For that, it must learn an existing FASTQ 
file for it to generate an efficient encoding.

Also, the compression and decompression of FASTQ files MUST be done using the same tree!

Another limitation of the current code is that it compresses code into separate files, depending on the number of 
threads used. Therefore, the same number of threads must be used to compress and decompress the data. The recommended
amount of threads should be the number of processors your machine is using.

Currently, the tree is always created and read to the "tree.out" file.

Usage
=====

To use fqzip, run it using a Command line interface.

Learning mode
-------------

Use a large FASTQ file with data which resembles the rest the data you wish to compress.

    fqzip learn <FASTQ FILE>

Compression mode
----------------

The compression is done to 3 files per thread using the output prefix.

    fqzip compress <FASTQ INPUT FILE> <OUTPUT PREFIX> <NUMBER OF THREADS>


Decompression mode
------------------

The decompression mode reads all the files it requires using the output prefix and the 
number of threads, and writes them out into a FASTQ file.

    fqzip decompress <INPUT PREFIX> <FASTQ OUTPUT FILE> <NUMBER OF THREADS>

Example
-------

Running:

    fqzip compress input.fastq output 4

Will generate the following files:

    output.0.headers
    output.0.sequence
    output.0.quality
    output.1.headers
    output.1.sequence
    output.1.quality
    output.2.headers
    output.2.sequence
    output.2.quality
    output.3.headers
    output.3.sequence
    output.3.quality

To decompress the FASTQ file back to the original, use:

    fqzip decompress output input-decompressed.fastq 4


About Us
--------

FQZip is an IDC (Interdisciplinary Center - Herzliya) project lead by Inbal Landsberg for the sequence squeeze competition.
The creators of FQZip are Or Peled, Barak Yacov, Yonatan Amir, Dan Benjamin and Ron Reiter.

Sequence Squeeze - http://www.sequencesqueeze.org/

License
-------

The code is given under the BSD-3 License.

http://www.opensource.org/licenses/BSD-2-Clause


