package documents.functions;

public class DeleteCollection implements DatabaseWriteFunction {

    private final String dbName;
    private final String collectionName;


    public DeleteCollection(String dbName, String collectionName) {
        this.dbName = dbName;
        this.collectionName = collectionName;
    }

    @Override
    public  synchronized boolean execute() {
            String path = "src/main/resources/databases/" + dbName + '/' + collectionName;
            if (fileUtils.checkIfFileOrDirectoryExists(path)) {
                if(fileUtils.deleteDirectory(path))
                return true;
            }
            return false;

        }
}
