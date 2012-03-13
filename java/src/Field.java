import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Field {
	public void serialize(DataOutputStream stream) throws IOException;
	public int getType();
}
