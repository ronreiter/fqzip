import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConstantField extends Field {
	private String value;

	public ConstantField(String value) {
		this.value = value;
	}

	public ConstantField(DataInputStream input) throws IOException {
		int length = input.read();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			buffer.append(input.readChar());
		}
		this.value = buffer.toString();
	}

	@Override
	public void serialize(DataOutputStream stream) throws IOException {
		stream.write(value.length());
		stream.writeChars(value);
	}
	
	public String getValue() {
		return this.value;
	}

}
