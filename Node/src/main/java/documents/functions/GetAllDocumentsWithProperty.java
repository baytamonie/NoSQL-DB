package documents.functions;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GetAllDocumentsWithProperty implements DocumentReadFunctions {
    @Override
    public boolean execute(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        return false;
    }
}
