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
		// private class Instrument {
		// private class Run {
		// private String flowcellID;
		//
		// public Run(String flowcellID) {
		// this.flowcellID = flowcellID;
		// }
		// }
		//
		// private HashMap<Integer, Run> runs;
		// }
		//
		// private HashMap<String, Instrument> instruments;
		// private Instrument instrument;

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

	private static final int SUPERBLOCK_SIZE = 512;
	private OutputStream output;

	@Override
	public void setOutput(OutputStream output) {
		this.output = output;
	}

	@Override
	public void compressNext(ReadData data) {
		//
		// // TODO store separator template
		// String[] headerFields = splitHeader(data.getHeader());
		//
		// // TODO check header amount doesn't change in superblock
		// int headersAmount = headerFields.length;
		//
		// if (readRecordsInSuperblock == 0) {
		//
		// // Create fields array
		// fields = new String[SUPERBLOCK_SIZE][headersAmount];
		//
		// // Init constant flags and min/max values
		// constantFields = new boolean[headersAmount];
		// maximumValue = new int[headersAmount];
		// minimumValue = new int[headersAmount];
		// for (int i = 0; i < headersAmount; i++) {
		// constantFields[i] = true;
		// try {
		// int numericValue = Integer.parseInt(headerFields[i]);
		// maximumValue[i] = numericValue;
		// minimumValue[i] = numericValue;
		// } catch (NumberFormatException e) {
		// // Non numeric field
		// }
		// }
		//
		// } else {
		//
		// for (int i = 0; i < headersAmount; i++) {
		// constantFields[i] &= (headerFields[i].equals(fields[0][i]));
		// try {
		// int numericValue = Integer.parseInt(headerFields[i]);
		// if (numericValue > maximumValue[i]) {
		// maximumValue[i] = numericValue;
		// } else if (numericValue < minimumValue[i]) {
		// minimumValue[i] = numericValue;
		// }
		// } catch (NumberFormatException e) {
		// // Non numeric field
		// }
		// }
		// }
		//
		// fields[readRecordsInSuperblock] = headerFields;
		//
		// readRecordsInSuperblock++;

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

	@Override
	public void closeOutput() throws IOException {
		output.close();
	}
}
