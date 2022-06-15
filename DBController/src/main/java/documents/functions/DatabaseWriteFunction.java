package documents.functions;

import java.io.ObjectInputStream;

public interface DatabaseWriteFunction {

    boolean execute(ObjectInputStream inputStream);
}
