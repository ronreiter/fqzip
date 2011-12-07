import re

# run result on 6GB file
# Compressed: 22149806
# Uncompressed: [0, 0, 0, 0, 110, 0, 0, 0]
# Max size: [5, 23, 21193, 1]
# field ID's
# 0000000000 1111111 22222222222222222222 3 44 5555 6666 7
# @SRR062634.2060165 HWI-EAS110_103327062:6:10:8487:7763/1
# field 1 max increment: 5 (need 3 bits)
# field 5 max increment: 23 (need 5 bits)
# field 6 max increment: 21193 (need 15 bits)
# field 7 max increment: 1 (need 0-1 bits)

header_re = re.compile("(@[\w]+).(\d+) ([\w\-]+):(\d+):(\d+):(\d+):(\d+)/(\d+)")
compressed = 0
uncompressed = [0] * 8
max_size = [0] * 4

def extract_header_data(header):
	data = header_re.findall(header)
	if not data:
		return
	res = list(data[0])
	
	for i in [1, 3, 4, 5, 6, 7]:
		res[i] = int(res[i])
		
	return res
	

def get_next_header(prev_header_data, new_header_data):
	global compressed, uncompressed, max_size
	if not prev_header_data:
		return new_header_data
		
	for i in [3, 4]:
		if prev_header_data[i] != new_header_data[i]:
			uncompressed[i] += 1
			return new_header_data
		
	compressed += 1
	small_header_data = [new_header_data[1] - prev_header_data[1], new_header_data[5] - prev_header_data[5], new_header_data[6], new_header_data[7]]
	
	for i, field in enumerate(small_header_data):
		if max_size[i] < field:
			max_size[i] = field
	
	return small_header_data
	
if __name__ == "__main__":
	import sys
	filedata = open(sys.argv[1])
	header_data = None
	while True:
		try:
			header = filedata.readline()
			if not header:
				break
			#print header.strip()
			for i in xrange(3): filedata.readline()
		
			prev_header_data = header_data
			header_data = extract_header_data(header)
			to_encode = get_next_header(prev_header_data, header_data)
		except KeyboardInterrupt:
			break
	
	print "Compressed: %d" % compressed
	print "Uncompressed: %s" % uncompressed
	print "Max size: %s"% max_size