import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HeaderBlock implements HeaderSerializable {

	static final int CONSTANT_FIELD = 0;
	static final int INCREMENTAL_FIELD = 1;
	static final int SMALL_DELTA_FIELD = 2;
	static final int LARGE_DELTA_FIELD = 3;

	private List<Integer[]> headerData = new ArrayList<Integer[]>();
	private ArrayList<Field> fields;
	private int fieldsAmount;

	private static enum Separator {
		Colon(':'), Period('.'), Space(' '), EqualsSign('='), Slash('/');

		private final char character;

		Separator(char character) {
			this.character = character;
		}

		char getCharacter() {
			return this.character;
		}
	}

	/**
	 * 
	 * @param header
	 *            the first header in the block.
	 */
	public HeaderBlock(String header) {

		String[] splitHeader = splitHeader(header);
		fieldsAmount = splitHeader.length;
		ArrayList<Integer> numericFields = new ArrayList<Integer>();

		for (int i = 0; i < fieldsAmount; i++) {
			try {
				int value = Integer.parseInt(splitHeader[i]);
				fields.set(i, new NumericField(value));
				numericFields.add(value);
			} catch (NumberFormatException e) {
				fields.set(i, new ConstantField(splitHeader[i]));
			}
		}

		headerData.add((Integer[]) numericFields.toArray());

	}

	/**
	 * Adds a new header to the block
	 * 
	 * @param header
	 * @return false if header does not match current block, true otherwise.
	 */
	public boolean add(String header) {
		String[] splitHeader = splitHeader(header);

		// Fields amount check
		if (splitHeader.length != fieldsAmount) {
			return false;
		}

		ArrayList<Integer> numericFields = new ArrayList<Integer>();

		// Constant fields check
		for (int i = 0; i < fieldsAmount; i++) {
			try {
				int value = Integer.parseInt(splitHeader[i]);
				fields.set(i, new NumericField(value));
				numericFields.add(value);
			} catch (NumberFormatException e) {
				if (!(fields.get(i) instanceof ConstantField)) {
					return false;
				}
			}
		}

		headerData.add((Integer[]) numericFields.toArray());

		return true;
	}

	@Override
	public void serialize(DataOutputStream stream) throws IOException {
		List<Field> numericalHeaderTypes = new ArrayList<Field>();
		stream.writeShort(fields.size());
		stream.writeShort(headerData.size());

		// TODO write fields
		for (Field field : fields) {
			field.serialize(stream);
			if (field.getType() != CONSTANT_FIELD) {
				numericalHeaderTypes.add(field);
			}
		}

		for (Integer[] header : headerData) {
			for (int i = 0; i < numericalHeaderTypes.size(); i++) {
				Field field = numericalHeaderTypes.get(i);
				switch (field.getType()) {
				case SMALL_DELTA_FIELD:
					stream.writeShort(((NumericField) field)
							.serializeNumber(header[i]));
					break;

				case LARGE_DELTA_FIELD:
					stream.writeLong(((NumericField) field)
							.serializeNumber(header[i]));
					break;
				}

			}
		}

	}

	@Override
	public void parse(DataInputStream stream) throws IOException {
		int numHeaders = stream.readShort();
		int numFields = stream.readShort();

		for (int i = 0; i < numFields; i++) {
			readField(stream);
		}

		// convert numblocks to int
		for (int i = 0; i < numHeaders; i++) {
			readHeader(stream);
		}

	}

	private void readField(DataInputStream stream) throws IOException {
		int fieldType = stream.readByte();

		switch (fieldType) {
		case CONSTANT_FIELD: // string

			break;

		case INCREMENTAL_FIELD: // increment 1 only
			break;

		case SMALL_DELTA_FIELD: // read 8 bit increment
			break;

		case LARGE_DELTA_FIELD: // read 16 bit increment
			break;
		}

	}

	private void readHeader(InputStream stream) throws IOException {

	}

	/*
	 * Splits header string into substrings on separators.
	 */
	private static String[] splitHeader(String header) {
		StringBuilder pattern = new StringBuilder("[");
		for (Separator s : Separator.values()) {
			pattern.append(s.getCharacter());
		}
		pattern.append(']');
		return header.split(pattern.toString());
	}
}
