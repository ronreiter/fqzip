import shelve
import pprint
import json

def generate_frequency_table(stats):
	frequencies = {}

	for key, occurence in stats.iteritems():
		data, position_id = key.split("_")
	
		if frequencies.has_key(position_id):
			frequencies[position_id].update({data : occurence})
		else:
			frequencies[position_id] = {data: occurence}

if __name__ == "__main__":
	stats = shelve.open("stats")
	open("frequencies.json","w").write(json.dumps(frequencies))
	print frequencies