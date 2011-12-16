import json
import heapq

from array import array
bitstream = array("B")
chunk = []

def get_bit(stream):
	global bitstream, chunk
	if not len(bitstream):
		if not len(chunk):
			chunk = [x for x in stream.read(1000)]

		if not len(chunk):
			return
		
		byte = ord(chunk.pop(0))
		
		for i in xrange(8):
			bitstream.append(byte % 2)
			byte = byte / 2
	
	return bitstream.pop()

def fill_table(huffman, prefix, table):
	if type(huffman) is int:
		table[huffman] = prefix
		return
			
	fill_table(huffman[0], prefix + "0", table)
	fill_table(huffman[1], prefix + "1", table)

def generate_huffman_table(frequencies):

	encoding_table = {}
	decoding_table = {}

	for position_id, histogram in frequencies.iteritems():
		encoding_table[position_id] = [None] * 256
	
		# create a mapping table for characters 
		ordinal_freq = [(histogram["%02x" % ordinal], ordinal) if histogram.has_key("%02x" % ordinal) else (1, ordinal) for ordinal in xrange(256)]

		heap = []
		for node in ordinal_freq:
			heapq.heappush(heap, node)
	
		while len(heap) > 1:
			left = heapq.heappop(heap)
			right = heapq.heappop(heap)
		
			new_node = (left[0] + right[0], (left[1], right[1]))

			heapq.heappush(heap, new_node)
	
		total, huffman = heap[0]
	
		fill_table(huffman, "", encoding_table[position_id])
		decoding_table[position_id] = huffman

	return encoding_table, decoding_table
	
if __name__ == "__main__":
	frequencies = json.load(open("frequencies.json"))
	encoding_table, decoding_table = generate_huffman_table(frequencies)
	open("huffman_encoding_table.json", "w").write(json.dumps(encoding_table))
	open("huffman_decoding_table.json", "w").write(json.dumps(decoding_table))

