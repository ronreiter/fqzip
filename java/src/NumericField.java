import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NumericField extends Field {

	private long previousValue, minValue, maxValue,
			minDelta = Integer.MAX_VALUE, maxDelta = Integer.MIN_VALUE;
	private long lastSerializedNumber;
	private Integer fieldType;

	public NumericField(int value) {
		// We set lastSerializedNumber for the first call to serializeNumber
		lastSerializedNumber = minValue = maxValue = value;
	}

	public NumericField(DataInputStream stream, int fieldType)
			throws IOException {

		this.fieldType = fieldType;
		minValue = stream.readLong();
	}

	public void add(int value) {
		previousValue = value;

		if (value > maxValue) {
			maxValue = value;
		} else if (value < minValue) {

			// We set lastSerializedNumber for the first call to serializeNumber
			lastSerializedNumber = minValue = value;
		}

		long delta = value - previousValue;
		if (delta > maxDelta) {
			maxDelta = delta;
		} else if (delta < minDelta) {
			minDelta = delta;
		}
	}

	public int getType() {
		if (fieldType == null) {
			if (minDelta == maxDelta && minDelta == 1) {
				return HeaderBlock.INCREMENTAL_FIELD;
			} else if (maxDelta < Short.MAX_VALUE
					&& -minDelta < Short.MAX_VALUE) {
				return HeaderBlock.SMALL_DELTA_FIELD;
			} else {
				return HeaderBlock.LARGE_DELTA_FIELD;
			}
		} else {
			return fieldType;
		}
	}

	public long serializeNumber(long number) {
		return number - lastSerializedNumber;
	}

	@Override
	public void serialize(DataOutputStream stream) throws IOException {
		stream.writeLong(minValue);
	}

	public long getOffset() {
		return minValue;
	}
}