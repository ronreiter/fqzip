public class NumericField extends Field {

	private int previousValue, minValue, maxValue,
			minDelta = Integer.MAX_VALUE, maxDelta = Integer.MIN_VALUE;

	public NumericField(int value) {
		minValue = maxValue = value;
	}

	public void add(int value) {
		previousValue = value;

		if (value > maxValue) {
			maxValue = value;
		} else if (value < minValue) {
			minValue = value;
		}

		int delta = value - previousValue;
		if (delta > maxDelta) {
			maxDelta = delta;
		} else if (delta < minDelta) {
			minDelta = delta;
		}
	}

}
