import sys
import shelve
import json
import argparse

import frequency
import huffman
import bz2

READ_AHEAD = 2
POSITION_GRANULARITY = 10
QUALITY_GRANULARITY = 3

# stats table is used to store both specific occurence count of the position,
# and both the specific occurence count of the data. The general position occurence
# count is assigned when data equals -1.

gene_id = {
	"T" : 0,
	"C" : 50,
	"A" : 100,
	"G" : 150,
	"N" : 200,
}
gene_id_reverse = {
	0 : "T",
	50: "C",
	100 : "A",
	150 : "G",
	200 : "N",
}

parser = argparse.ArgumentParser(description='NGZip compressor.')
parser.add_argument("-c", action="store_true", dest="compress", default=False)
parser.add_argument("-x", action="store_true", dest="decompress", default=False)
parser.add_argument("-l", action="store_true", dest="learn", default=False)
parser.add_argument("-i", action="store", dest="infile", required=True)
parser.add_argument("-e", action="store", dest="encoding_table", default="huffman_encoding_table.json")
parser.add_argument("-d", action="store", dest="decoding_table", default="huffman_decoding_table.json")
parser.add_argument("-o", action="store", dest="outfile")

args = parser.parse_args()

if args.compress or args.learn:
	filedata = open(args.infile)

if args.compress:
	encoding_table = json.load(open(args.encoding_table))
if args.decompress:
	decoding_table = json.load(open(args.decoding_table))

if args.compress:
	outfile = args.outfile if args.outfile else args.infile + ".ngz"

	compressed = open(args.outfile, "wb")
	headerdata = bz2.BZ2File(args.outfile + ".headers", "wb")

if args.decompress:
	outfile = args.outfile if args.outfile else args.infile[:-4]

	compressed = open(args.infile, "rb")
	headerdata = bz2.BZ2File(args.infile + ".headers", "rb")

	decompressed = open(args.outfile, "w")

stats = {}
info_strips_processed = 0
bitstream = ""
uncompressed_data_count = 0
compressed_data_count = 0

seqlen = 100
effective_seqlen = seqlen / POSITION_GRANULARITY


while True:
	pos = 0
	position_string = ""

	# go over the file
	if args.compress or args.learn:
		header = filedata.readline()
		sequence = filedata.readline()[:-1]
		plus = filedata.readline()[:-1]
		quality = filedata.readline()[:-1]

		# compress headers using bzip2
		if args.compress:
			headerdata.write(header)
			
		for gene, score in zip(sequence, quality):
			# create an 8 bit data structure for the gene and quality value
			quality_value = ord(score) - 35		
			data = gene_id[gene] + quality_value
	
	
			if args.compress:
				if not encoding_table.has_key(position_string):
					compressed.write(chr(data))
					uncompressed_data_count += 8
				else:
					encoded_data = encoding_table[position_string][data]
					bitstream += encoded_data
		
					while len(bitstream) > 8:
						compressed.write(chr(int(bitstream[:8], 2)))
						bitstream = bitstream[8:]
	
					compressed_data_count += len(encoded_data)
	
			if args.learn:
				position_key = "%02x_%s" % (data, position_string)
		
				if stats.has_key(position_key):
					stats[position_key] += 1
				else:
					stats[position_key] = 1

			effective_quality = quality_value / QUALITY_GRANULARITY + 35

			# calculate the effective position (segment)
			effective_pos = pos / POSITION_GRANULARITY
			if effective_pos > effective_seqlen:
				effective_pos = 256 - effective_seqlen + effective_pos
		
			# inefficient method - we can store this in 2 bytes instead of 5 but this
			# is easier to debug.
			# variance calculation - 4 (gene) * 10 (effective pos) * 13 (effective quality) = 520 variants
			# for each variant, we have 4 (gene) * 40 (real quality) = 160 different alphabets
	
			# NOTE: position_id is built using the following syntax:
			# (G, P, Q)(n-i) ... (G, P, Q)(n-2) (G, P, Q)(n-1)
	
			current_position_string = "%s%02x%02x" %  (gene, effective_pos, effective_quality)
			position_string += current_position_string
	
			if pos > READ_AHEAD:
				position_string = position_string[5:]

			pos += 1

			if args.compress:
				compressed.write("\n")

	# start by decompressing the header
	if args.decompress:
		header = headerdata.readline()
		position_string = ""
		reconstructed_gene = ""
		reconstructed_quality = ""

		for pos in xrange(seqlen):

			current_tree = decoding_table[position_string]
		
			while True:
				bit = huffman.get_bit(compressed)
				if bit is None: 
					pos = -1
					break
					
				if bit == 1:
					current_tree = current_tree[1]
				else:
					current_tree = current_tree[0]
					
				if type(current_tree) is int:
					break
					
			if pos == -1:
				break

			data = current_tree

			gene = gene_id_reverse[data / 50]
			quality_value = data % 50 + 35
			quality = chr(quality_value)

			# calculate effective position and quality
			effective_quality = quality_value / QUALITY_GRANULARITY + 35
			effective_pos = pos / POSITION_GRANULARITY
			if effective_pos > effective_seqlen:
				effective_pos = 256 - effective_seqlen + effective_pos
			
			current_position_string = "%s%02x%02x" %  (gene, effective_pos, effective_quality)
			position_string += current_position_string
			
			if pos > READ_AHEAD:
				position_string = position_string[5:]
			
			reconstructed_gene += gene
			reconstructed_quality += quality
		
		decompressed.write(header)
		decompressed.write(reconstructed_gene + "\n")
		decompressed.write("+\n")
		decompressed.write(reconstructed_quality + "\n")

	info_strips_processed += 1
	if not info_strips_processed % 1000:
		print header.strip()

	if not header:
		break



if args.compress:
	print "Uncompressed bits: %d" % uncompressed_data_count
	print "Compressed bits: %d" % compressed_data_count

# generate a Huffman tree from a frequency table of the statistics
if args.learn:
	print "Generating Huffman table..."
	frequency_table = frequency.generate_frequency_table(stats)
	encoding_table, decoding_table = huffman.generate_huffman_table(frequency_table)
	open(args.encoding_table, "w").write(json.dumps(encoding_table))
	open(args.decoding_table, "w").write(json.dumps(decoding_table))
	
