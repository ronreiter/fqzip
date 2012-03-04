import java.io.DataOutputStream;
import java.io.IOException;

public class NumericField extends Field {

	private int previousValue, minValue, maxValue,
			minDelta = Integer.MAX_VALUE, maxDelta = Integer.MIN_VALUE;
	private Integer lastSerializedNumber;

	public NumericField(int value) {
		// We set lastSerializedNumber for the first call to serializeNumber
		lastSerializedNumber = minValue = maxValue = value;
	}

	public void add(int value) {
		previousValue = value;

		if (value > maxValue) {
			maxValue = value;
		} else if (value < minValue) {

			// We set lastSerializedNumber for the first call to serializeNumber
			lastSerializedNumber = minValue = value;
		}

		int delta = value - previousValue;
		if (delta > maxDelta) {
			maxDelta = delta;
		} else if (delta < minDelta) {
			minDelta = delta;
		}
	}

	public int getType() {
		if (minDelta == maxDelta && minDelta == 1) {
			return HeaderBlock.INCREMENTAL_FIELD;
		} else if (maxDelta < Short.MAX_VALUE && -minDelta < Short.MAX_VALUE) {
			return HeaderBlock.SMALL_DELTA_FIELD;
		} else {
			return HeaderBlock.LARGE_DELTA_FIELD;
		}
	}

	public int serializeNumber(Integer integer) {
		return integer - lastSerializedNumber;
	}

	@Override
	public void serialize(DataOutputStream stream) throws IOException {
		stream.write(minValue);
	}
}