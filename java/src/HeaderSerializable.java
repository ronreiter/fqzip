import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface HeaderSerializable {
	public void serialize(OutputStream stream);

	public void parse(InputStream stream) throws IOException;
}