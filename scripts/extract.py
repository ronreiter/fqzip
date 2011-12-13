import sys
filedata = open(sys.argv[1])

while True:
	header = filedata.readline()
	sequence = filedata.readline()
	plus = filedata.readline()
	quality = filedata.readline()

	if not header:
		break

	print sequence,
