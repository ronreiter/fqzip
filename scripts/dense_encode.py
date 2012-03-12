import os

hg = open("/Users/Ron/work/hg19_merged.fa")
hg_dense = open("/Users/Ron/work/hg19_dense.data", "wb")


bits = {
	"T" : [0, 0, 0],
	"C" : [0, 0, 1],
	"A" : [0, 1, 0],
	"G" : [0, 1, 1],
	"N" : [1, 0, 0],
}
bit_array = []
pos = 0
while True:
	chunk = hg.read(8)
	if pos % 800000 == 0:
		print pos
	pos += 8

	if not chunk:
		break

	for letter in chunk:
		bit_array += bits[letter]
		if len(bit_array) > 8:
			byte = 0
			for i in xrange(8):
				if bit_array.pop(0):
					byte += 2 ** i
			hg_dense.write(chr(byte))


