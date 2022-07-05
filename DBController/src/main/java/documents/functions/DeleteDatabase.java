package documents.functions;

public class DeleteDatabase implements DatabaseWriteFunction{

    private final String dbName;

    public DeleteDatabase(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public synchronized boolean execute() {

            String path = "src/main/resources/databases/" + dbName;
            if (fileUtils.checkIfFileOrDirectoryExists(path)) {
                if(fileUtils.deleteDirectory(path))
                return true;
            }
            return false;

        }

}
