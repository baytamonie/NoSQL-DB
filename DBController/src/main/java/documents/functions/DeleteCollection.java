package documents.functions;

import utils.FileUtils;

public class DeleteCollection implements DatabaseWriteFunction {

    private final String dbName;
    private final String collectionName;

    public DeleteCollection(String dbName, String collectionName) {
        this.dbName = dbName;
        this.collectionName = collectionName;
    }

    @Override
    public boolean execute() {

            String path = "src/main/resources/databases/" + dbName + '/' + collectionName;
            if (FileUtils.checkIfFileOrDirectoryExists(path)) {
                FileUtils.deleteDirectory(path);
                return true;
            }
            return false;

        }
}
