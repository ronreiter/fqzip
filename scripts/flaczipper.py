#!/usr/bin/python
import os
import sys
import argparse
import bz2

parser = argparse.ArgumentParser(description='Compresses FASTQ files.')
parser.add_argument('input', metavar='I', help='Input file')
parser.add_argument('output', metavar='O', help='Output file')
parser.add_argument('compress', metavar='C', help='Compress')
parser.add_argument('extract', metavar='X', help='Extract')

parser.parse_args()

def fastq_reader(filename):
	data = open(filename):
	while True:
		header = data.readline()
		sequence = data.readline()
		plus = data.readline()
		quality = data.readline()
		
		yield header, sequence, quality
	
if parser.compress:	
	header_file = bz2.BZ2Open(parser.input + ".head","wb")
	sequence_file = bz2.BZ2Open(parser.input + ".seq", "wb")
	quality_file = open(parser.input + ".qual", "wb")
	
	for header, sequence, quality in fastq_reader(parser.input):
		header_file.write(header)
		sequence_file.write(sequence)
		quality_file.write(quality)
		
	system("flac -")
		
