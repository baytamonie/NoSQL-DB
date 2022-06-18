package documents.functions;

import utils.FileUtils;

public class DeleteDocument implements DatabaseWriteFunction {

    private final String dbName;
    private final String collectionName;
    private final String documentId;

    public DeleteDocument(String dbName, String collectionName, String documentId) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.documentId = documentId;
    }

    @Override
    public boolean execute() {
        String path = "src/main/resources/databases"+'/'+dbName+'/'+collectionName;
        if(!FileUtils.checkIfFileOrDirectoryExists(path))
            return false;
      return FileUtils.deleteFromIds(path+"/ids.json",documentId);

    }
}
