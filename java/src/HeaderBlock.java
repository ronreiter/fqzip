import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HeaderBlock implements HeaderSerializable {

	static final private int CONSTANT_FIELD = 0;
	static final private int INCREMENTAL_FIELD = 1;
	static final private int SMALL_DELTA_FIELD = 2;
	static final private int LARGE_DELTA_FIELD = 3;

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
	public void serialize(OutputStream stream) {
		// TODO write headers amount (DWord)
		// TODO write fields amount (Word)

		// TODO write fields

		// TODO write header data

	}

	@Override
	public void parse(InputStream stream) throws IOException {
		int numHeaders = readDword(stream);
		int numFields = readWord(stream);

		for (int i = 0; i < numFields; i++) {
			readField(stream);
		}

		// convert numblocks to int
		for (int i = 0; i < numHeaders; i++) {
			readHeader(stream);
		}

	}

	private void readField(InputStream stream) throws IOException {
		int fieldType = readByte(stream);

		switch (fieldType) {
		case 0: // string

			break;

		case 1: // increment 1 only
			break;

		case 3: // read 8 bit increment
			break;

		case 4: // read 16 bit increment
			break;
		}

	}

	private void readHeader(InputStream stream) throws IOException {

	}

	private int readDword(InputStream stream) throws IOException {
		byte[] dword = new byte[4];
		stream.read(dword);
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (dword[i] & 0x000000FF) << shift;
		}
		return value;
	}

	private int readWord(InputStream stream) throws IOException {
		byte[] dword = new byte[2];
		stream.read(dword);
		int value = 0;
		for (int i = 0; i < 2; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (dword[i] & 0x000000FF) << shift;
		}
		return value;
	}

	private int readByte(InputStream stream) throws IOException {
		int value = stream.read();
		return value;
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
