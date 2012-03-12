sequence_file = open("sequences")

def hamming(a, b):
	return len([x for x in xrange(len(a)) if a[x] == b[x]])

while True:
	buf = sequence_file.readlines(10000000)
	if not buf:
		break
		
	buf = [x[2:] for x in buf]
	buf.sort()
	last_line = buf[0]
	count = 0
	
	for line in buf:
		distance = hamming(last_line, line)
		if distance < 10:
			count += 1
		last_line = line

	print count
