import os
import sys

i = 0

if len(sys.argv) < 4:
	print "Usage: splitter.py <file> <chunk size> <line in chunk>"
	print "For example: 'file a.fastq 4 1' will print headers only"
	sys.exit(1)


f = open(sys.argv[1])
chunk_size = int(sys.argv[2])
modulu = int(sys.argv[3]) - 1

print >> sys.stderr, "chunk size: %d" % chunk_size
print >> sys.stderr, "modulu: %d" % modulu 

while True:
	line = f.readline()

	if not line:
		break

	if i % chunk_size == modulu:
		print line,
	i += 1

