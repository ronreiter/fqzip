import os
import sys
import argparse
import bz2
import zipfile
import wave

parser = argparse.ArgumentParser(description='Compresses FASTQ files.')
parser.add_argument('input', metavar='i', help='Input file', required=True)
parser.add_argument('output', metavar='o', help='Output file', required=True)
parser.add_argument('compress', metavar='c', help='Compress')
parser.add_argument('extract', metavar='x', help='Extract')

parser.parse_args()
def log(line):
	print >> sys.stderr, line

def fastq_reader(filename):
	data = open(filename):
	while True:
		header = data.readline()
		sequence = data.readline()
		plus = data.readline()
		quality = data.readline()
		
		yield header, sequence, quality
	
header_filename = parser.input + ".head"
sequence_filename = parser.input + ".seq"
quality_filename = parser.input + ".wav"
quality_compressed_filename = parser.input + ".flac"
if parser.compress:	
	header_file = bz2.BZ2Open(header_filename,"w")
	sequence_file = bz2.BZ2Open(sequence_filename, "w")
	quality_file = wave.open(quality_filename, "w")
	
	quality_file.setnchannels(1)
	quality_file.setsampwidth(1)
	quality_file.setframerate(101)
	
	log("Splitting file and compressing headers and sequences...")
	for header, sequence, quality in fastq_reader(parser.input):
		header_file.write(header)
		sequence_file.write(sequence)
		quality_file.writeframes(quality)
		
	header_file.close()
	sequence_file.close()
	quality_file.close()
		
	log("Encoding quality file using FLAC compression...")
	os.system("flac -8 %s" % quality_filename)
	
	log("Creating output archive...")
	output = zipfile.open(parser.output)
	output.write(header_filename)
	output.write(sequence_filename)
	output.write(quality_compressed_filename)
	output.close()
	
	log("Removing temporary file...")
	os.unlink(header_filename)
	os.unlink(sequence_filename)
	os.unlink(quality_filename)
	
elif parser.decompress:
	log("Extracting sections...")
	output = zipfile.open(parser.output, "r")
	output.extractall()
	output.close()
	
	log("Extracting qualities using FLAC...")
	header_file = bz2.BZ2Open(header_filename)
	sequence_file = bz2.BZ2Open(sequence_filename)
	os.system("flac -d %s" % quality_compressed_filename)
	quality_file = wave.open(quality_filename)
	
	output = open(parser.output, "w")
	
	log("Writing FASTQ file...")
	while True:
		header = header_file.readline()
		sequence = sequence_file.readline()
		quality = quality_file._file.file.readline()
		
		if not header:
			break
			
		output.write(header)
		output.write(sequence)
		output.write(r"+\n")
		output.write(quality)
		
	output.close()
	
	log("Removing temporary file...")
	os.unlink(header_filename)
	os.unlink(sequence_filename)
	os.unlink(quality_filename)
	
	
	
	
