package documents.functions;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface DocumentWriteFunctions {

    boolean execute(ObjectInputStream clientInputStream, ObjectOutputStream clientOutputStream, ObjectInputStream controllerInputStream,ObjectOutputStream controllerOutputStream);

}
