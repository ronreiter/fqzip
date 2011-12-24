import os
import sys
import argparse
import bz2
import zipfile
import wave

def log(line):
	print >> sys.stderr, line

def fastq_reader(filename):
	data = open(filename)
	while True:
		header = data.readline()
		sequence = data.readline()
		plus = data.readline()
		quality = data.readline()
		
		yield header, sequence, quality

parser = argparse.ArgumentParser(description='Compresses FASTQ files using FLAC compression.')
parser.add_argument('-i', '--input', dest='input_file', required=True, help='Input file')
parser.add_argument('-o', '--output', dest='output_file', required=True, help='Output file')

group = parser.add_mutually_exclusive_group(required=True)
group.add_argument('-c', '--compress', dest='compress', help='Compress', action='store_true')
group.add_argument('-d', '--decompress', dest='compress', help='Decompress', action='store_false')

args = parser.parse_args()

header_filename = args.input_file + ".head"
sequence_filename = args.input_file + ".seq"
quality_filename = args.input_file + ".wav"
quality_compressed_filename = args.input_file + ".flac"
if args.compress:	
	header_file = bz2.BZ2File(header_filename,"w")
	sequence_file = bz2.BZ2File(sequence_filename, "w")
	quality_file = wave.open(quality_filename, "w")
	
	quality_file.setnchannels(1)
	quality_file.setsampwidth(1)
	quality_file.setframerate(101)
	
	log("Splitting file and compressing headers and sequences...")
	for header, sequence, quality in fastq_reader(args.input_file):
		header_file.write(header)
		sequence_file.write(sequence)
		quality_file.writeframes(quality)
		
	header_file.close()
	sequence_file.close()
	quality_file.close()
		
	log("Encoding quality file using FLAC compression...")
	os.system("flac -8 %s" % quality_filename)
	
	log("Creating output archive...")
	output = zipfile.open(args.output_file)
	output.write(header_filename)
	output.write(sequence_filename)
	output.write(quality_compressed_filename)
	output.close()
	
	log("Removing temporary file...")
	os.unlink(header_filename)
	os.unlink(sequence_filename)
	os.unlink(quality_filename)
	
else:
	log("Extracting sections...")
	output = zipfile.open(args.output_file, "r")
	output.extractall()
	output.close()
	
	log("Extracting qualities using FLAC...")
	header_file = bz2.BZ2Open(header_filename)
	sequence_file = bz2.BZ2Open(sequence_filename)
	os.system("flac -d %s" % quality_compressed_filename)
	quality_file = wave.open(quality_filename)
	
	output = open(args.output_file, "w")
	
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
	
	
	
	
