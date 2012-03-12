import java.io.DataOutputStream;
import java.io.IOException;

public class ConstantField extends Field {
	private String value;

	public ConstantField(String value) {
		this.value = value;
	}
	
	@Override
	public void serialize(DataOutputStream stream) throws IOException {
		stream.write(value.length());
		stream.writeChars(value);
	}
}
