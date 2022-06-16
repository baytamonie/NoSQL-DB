package documents.functions;

public class DocumentWriteFunctionsFactory {
    public DocumentWriteFunctions getFunction(String function){
        if( function == null || function.isEmpty())
            throw new IllegalArgumentException();
        switch (function){
            case "createDatabase":
                return new CreateDatabase();
            case "createCollection":
                return new CreateCollection();
            case "addDocument":
                return new AddDocumentToCollection();

            default:
                throw new IllegalArgumentException();
        }
    }
}
