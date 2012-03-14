sequence_file = open("sequences")
SHIFT = 2

def hamming(a, b, shift):
	return len([x for x in xrange(len(a)) if a[x] == b[(x + shift) % len(b)]])

while True:
	buf = sequence_file.readlines(10000000)
	buf = [x[2:] for x in buf]
	buf.sort()
	
	represent = buf[200]
	count = 0
	
	for line in buf:
		for shift in xrange(-1 * SHIFT, SHIFT+1):
			
			distance = hamming(represent, line, shift)
			if distance < 10:
				count += 1
				break
			
	print count