package documents.functions;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface DocumentFunction {

    boolean execute(ObjectInputStream inputStream, ObjectOutputStream outputStream);
}
