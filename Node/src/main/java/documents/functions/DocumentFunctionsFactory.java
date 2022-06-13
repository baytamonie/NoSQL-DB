package documents.functions;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DocumentFunctionsFactory {

    public DocumentFunction createDocumentFunction(String function){
        if( function == null || function.isEmpty())
            throw new IllegalArgumentException();
        switch (function){
            case "getDocumentById":
                return new GetDocumentById();
            case "getProperty":
                return new GetPropertyValueFromDocument();
            case "login":
                return new Login();
            case "getAllDocumentsWithProperty":
                return new GetAllDocumentsWithProperty();
            case "getCollection":
                return new GetCollectionFromDatabase();
            default:
                throw new IllegalArgumentException();
        }
    }

}
