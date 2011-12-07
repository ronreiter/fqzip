import sys
import shelve
import json
import argparse

import frequency
import huffman


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

parser = argparse.ArgumentParser(description='NGZip compressor.')
parser.add_argument("-c", action="store_true", dest="compress", default=False)
parser.add_argument("-x", action="store_true", dest="decompress", default=False)
parser.add_argument("-l", action="store_true", dest="learn", default=False)
parser.add_argument("-i", action="store", dest="infile", required=True)
parser.add_argument("-e", action="store", dest="encoding_table", default="huffman_encoding_table.json")
parser.add_argument("-o", action="store", dest="outfile")

args = parser.parse_args()
outfile = args.outfile if args.outfile else args.infile + ".ngz"
headerfile = outfile + ".headers"
filedata = open(args.infile)
if args.compress:
	encoding_table = json.load(open(args.encoding_table))
	compressed = open(outfile, "wb")
	headerdata = open(headerfile, "wb")

stats = {}
info_strips_processed = 0
bitstream = ""
uncompressed_data_count = 0
compressed_data_count = 0

while True:
	header = filedata.readline()
	sequence = filedata.readline()[:-1]
	plus = filedata.readline()[:-1]
	quality = filedata.readline()[:-1]

	headerdata.write(header)

	pos = 0
	position_string = ""
	seqlen = len(sequence)
	effective_seqlen = seqlen / POSITION_GRANULARITY
	
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

		pos += 1
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
			
			
	if not header:
		break

	info_strips_processed += 1
	if not info_strips_processed % 1000:
		print header.strip()

	if args.compress:
		compressed.write("\n")

if args.compress:
	print "Uncompressed bits: %d" % uncompressed_data_count
	print "Compressed bits: %d" % compressed_data_count

# generate a Huffman tree from a frequency table of the statistics
if args.learn:
	print "Generating Huffman table..."
	encoding_table = huffman.generate_huffman_table(frequency.generate_frequency_table(stats))
	open(args.encoding_table, "w").write(json.dumps(encoding_table))
	
