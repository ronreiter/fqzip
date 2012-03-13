import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HeaderBlock {

	static final int CONSTANT_FIELD = 0;
	static final int INCREMENTAL_FIELD = 1;
	static final int SMALL_DELTA_FIELD = 2;
	static final int LARGE_DELTA_FIELD = 3;
    static final int SUPERBLOCK_SIZE = 10000;

	private List<Long[]> headerData;
	private List<Field> fields;
	private int fieldsAmount;
	private String separators;
	private int readHeaders = 0;
    private int numberOfNumericFields = 0;

    static final String SEPARATORS = ":. =/";

	/**
	 * 
	 * @param header
	 *            the first header in the block.
	 */
	public HeaderBlock(String header) {
		String[] splitHeader = splitHeader(header);
		separators = getSeparators(header);
		fieldsAmount = splitHeader.length;
        fields = new ArrayList<Field>();
        headerData = new ArrayList<Long[]>();
        
		for (int i = 0; i < fieldsAmount; i++) {
            Field field;
            boolean isNumeric = true;

            for (int c = 0; c < splitHeader[i].length(); c++) {
                if (!Character.isDigit(splitHeader[i].charAt(c))) {
                    isNumeric = false;
                    break;
                }
            }

			if (isNumeric) {
				long value = Integer.parseInt(splitHeader[i]);
				field = new NumericField(value);
                numberOfNumericFields++;
			} else {
				field = new ConstantField(splitHeader[i]);
			}

            fields.add(field);
		}
	}

	public HeaderBlock(DataInputStream stream) throws IOException {
		DataInputStream input = stream;
		parse(input);
	}

	private String getSeparators(String header) {
		return header.replaceAll("[^" + SEPARATORS + "]", "");
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

        // superblock size check
        if (headerData.size() == SUPERBLOCK_SIZE) {
            return false;
        }

		Long[] numericFields = new Long[numberOfNumericFields];

		// Constant fields check
        int numericIndex = 0;
		for (int i = 0; i < fieldsAmount; i++) {
            switch(fields.get(i).getType()) {
                case SMALL_DELTA_FIELD:
                case LARGE_DELTA_FIELD:
                    numericFields[numericIndex] = Long.parseLong(splitHeader[i]);
                    numericIndex++;
                    break;
            }
		}

		headerData.add(numericFields);

		return true;
	}

	public void serialize(DataOutputStream stream) throws IOException {
		List<Field> numericalHeaderTypes = new ArrayList<Field>();
		stream.write(separators.length());
		stream.writeChars(separators);
		stream.writeShort(headerData.size());
        
		// write fields
		for (Field field : fields) {
            stream.writeByte(field.getType());
			field.serialize(stream);
			if (field.getType() != CONSTANT_FIELD) {
				numericalHeaderTypes.add(field);
			}
		}

		for (Long[] header : headerData) {
			for (int i = 0; i < numericalHeaderTypes.size(); i++) {
				Field field = numericalHeaderTypes.get(i);
				switch (field.getType()) {
				case SMALL_DELTA_FIELD:
					stream.writeShort((short) ((NumericField) field)
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

	public void parse(DataInputStream stream) throws IOException {
		int length = stream.read();
        if (length < 0) {
            return;
        }
		StringBuilder buffer = new StringBuilder(length);
        fields = new ArrayList<Field>();
        headerData = new ArrayList<Long[]>();

		for (int i = 0; i < length; i++) {
			buffer.append(stream.readChar());
		}
		separators = buffer.toString();
		int numFields = length + 1;
		int numHeaders = stream.readShort();
		int numericalFields = 0;

		for (int i = 0; i < numFields; i++) {
			numericalFields += readField(stream);
		}

		// convert numblocks to int
		for (int i = 0; i < numHeaders; i++) {
			readHeader(stream, numericalFields);
		}

	}

	private int readField(DataInputStream stream) throws IOException {
		int fieldType = stream.readByte();

        switch(fieldType) {
            case CONSTANT_FIELD:
                fields.add(new ConstantField(stream));
                return 0;

            case INCREMENTAL_FIELD:
                fields.add(new NumericField(stream, fieldType));
                return 0;

            case SMALL_DELTA_FIELD:
            case LARGE_DELTA_FIELD:
                fields.add(new NumericField(stream, fieldType));
                return 1;

            default:
                throw new IOException("Corrupted file, got fieldType " + fieldType);
        }
	}

	private void readHeader(DataInputStream stream, int numericalFields)
			throws IOException {
		Long[] newHeader = new Long[numericalFields];
		int i = 0;
		for (Field field : fields) {

			switch (field.getType()) {
			case CONSTANT_FIELD:
				break;
			case INCREMENTAL_FIELD:
				break;
			case SMALL_DELTA_FIELD:
				newHeader[i] = (long)stream.readShort();
				i++;
				break;
			case LARGE_DELTA_FIELD:
				newHeader[i] = stream.readLong();
				i++;
				break;
			}
		}
        headerData.add(newHeader);
	}

	/*
	 * Splits header string into substrings on separators.
	 */
	private static String[] splitHeader(String header) {
		return header.split("[" + SEPARATORS + "]");
	}

	/**
	 * Supplies the next header from this block.
	 * 
	 * @return the next header if exists, null otherwise
	 */
	public String nextHeader() {
		if (readHeaders == headerData.size() - 1) {
			return null;
		}
        if (fieldsAmount == 0) {
            fieldsAmount = fields.size();
        }
		StringBuilder buffer = new StringBuilder();
        int deltaFieldIndex = 0;

		for (int i = 0; i < fieldsAmount; i++) {
			Field field = fields.get(i);
			switch (field.getType()) {
			case CONSTANT_FIELD:
				buffer.append(((ConstantField) field).getValue());
				break;
			case INCREMENTAL_FIELD:
				buffer.append(((NumericField) field).getOffset() + i);
				break;
			default:
				buffer.append(((NumericField) field).getOffset()
						+ headerData.get(readHeaders)[deltaFieldIndex]);
                deltaFieldIndex++;
				break;
			}
			if (i < fieldsAmount - 1) {
				buffer.append(separators.charAt(i));
			}
		}
		readHeaders++;
		return buffer.toString();
	}
    
    public int size() {
        return headerData.size();
    }
}
