import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: ron Date: 12/14/11 Time: 1:15 AM To change
 * this template use File | Settings | File Templates.
 */
public class HeaderCompressor implements Compressor {
	public static void debug() {
		
	}

	private static class HeaderEncoder {
//		private class Instrument {
//			private class Run {
//				private String flowcellID;
//
//				public Run(String flowcellID) {
//					this.flowcellID = flowcellID;
//				}
//			}
//
//			private HashMap<Integer, Run> runs;
//		}
//
//		private HashMap<String, Instrument> instruments;
//		private Instrument instrument;

		/**
		 * Sets the processed Instrument ID
		 * 
		 * @param instrument
		 *            Instrument ID
		 */
		// public void setInstrument(String instrument) {
		// if (!instrumentExists(instrument)) {
		// // Add instrument
		// }
		// // Set instrument and records
		// }

		/**
		 * Sets the processed Run number on instrument
		 * 
		 * @param runNumber
		 *            Run number on instrument
		 */
		// public void setRunNumber(int runNumber) {
		// if (runNumberExists(runNumber)) then {
		// // Set run number
		// } else {
		// // Add run number and set
		// }
		// }
	}

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

	private class Field {
		private int length;
		private Separator separator;

		Field(int length, Separator separator) {
			this.length = length;
			this.separator = separator;
		}

		public int getLength() {
			return this.length;
		}
	}

	private class NumericField extends Field {
		private int minValue, maxValue;
		private boolean deltaEncoding;

		public NumericField(int length, Separator separator, int minValue,
				int maxValue, boolean deltaEncoding) {
			super(length, separator);
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.deltaEncoding = deltaEncoding;
		}

		public int getMinValue() {
			return this.minValue;
		}

		public int getMaxValue() {
			return this.maxValue;
		}

		public boolean usesDeltaEncoding() {
			return this.deltaEncoding;
		}
	}

	private class NonNumericField extends Field {
		private static final int VARIABLE_LENGTH = 0;
		private BitSet bitVector;

		public NonNumericField(int length, Separator separator) {
			super(length, separator);
		}
	}

	private class ConstantField extends Field {
		private String value;

		public ConstantField(int length, Separator separator, String value) {
			super(length, separator);
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	private static final int SUPERBLOCK_SIZE = 512;
	private static int readRecordsInSuperblock = 0;
	private static String[][] fields;
	private static String[] rawHeaders = new String[SUPERBLOCK_SIZE];
	private static boolean[] constantFields;
	private static int[] maximumValue;
	private static int[] minimumValue;

	@Override
	public void setOutput(OutputStream input) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public void compressNext(ReadData data) {

		// TODO store separator template
		String[] headerFields = splitHeader(data.getHeader());
		
		// TODO check header amount doesn't change in superblock
		int headersAmount = headerFields.length;

		if (readRecordsInSuperblock == 0) {

			// Create fields array
			fields = new String[SUPERBLOCK_SIZE][headersAmount];

			// Init constant flags and min/max values
			constantFields = new boolean[headersAmount];
			maximumValue = new int[headersAmount];
			minimumValue = new int[headersAmount];
			for (int i = 0; i < headersAmount; i++) {
				constantFields[i] = true;
				try {
					int numericValue = Integer.parseInt(headerFields[i]);
					maximumValue[i] = numericValue;
					minimumValue[i] = numericValue;
				} catch (NumberFormatException e) {
					// Non numeric field
				}
			}
			
		} else {

			for (int i = 0; i < headersAmount; i++) {
				constantFields[i] &= (headerFields[i].equals(fields[0][i]));
				try {
					int numericValue = Integer.parseInt(headerFields[i]);
					if (numericValue > maximumValue[i]) {
						maximumValue[i] = numericValue;
					} else if (numericValue < minimumValue[i]) {
						minimumValue[i] = numericValue;
					}
				} catch (NumberFormatException e) {
					// Non numeric field
				}
			}
		}

		fields[readRecordsInSuperblock] = headerFields;

		readRecordsInSuperblock++;

		// // Store header
		// rawHeaders[readRecordsInSuperblock++] = data.getHeader();
		//
		// // Split first line, resolve fields
		// if (readRecordsInSuperblock == SUPERBLOCK_SIZE) {
		//
		// // Identify constant fields
		//
		//
		// fields = getSuperblockFields();
		//
		//
		//
		// for (Field f : fields) {
		// if (f instanceof NumericField) {
		// if (((NumericField) f).usesDeltaEncoding()) {
		// // TODO get delta
		// } else {
		// // TODO get value
		// }
		// }
		//
		// if (f instanceof NonNumericField) {
		// // TODO
		// }
		// }
		// }

	}
}
