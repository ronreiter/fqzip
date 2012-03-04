import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface HeaderSerializable {
	public void serialize(DataOutputStream stream) throws IOException;

	public void parse(DataInputStream stream) throws IOException;
}
