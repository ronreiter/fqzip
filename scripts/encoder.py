import os
import array


hg = open("/Users/Ron/work/hg19_merged.fa")
CHUNK_SIZE = 30
db = array.array("L")
pos = 0

while True:
	chunk = hg.read(CHUNK_SIZE)

	if not chunk:
		break

	if db.has_key(chunk):
		db[chunk].append(pos)
	else:
		db[chunk] = [pos]

	pos += 30
	if pos % 30000000 == 0:
		print pos / (1024 * 1024)


