#!/usr/bin/python
import os, sys

directory = sys.argv[1]
directory_files = [x for x in os.listdir(directory) if x.endswith(".fa")]
output = open(sys.argv[2], "w")

offset = 0

for fn in directory_files:
	print >> sys.stderr, "%s,%s" % (fn, offset)

	for line in open("%s/%s" % (directory, fn)):
		output.write(line.upper().strip())
		offset += len(line)
