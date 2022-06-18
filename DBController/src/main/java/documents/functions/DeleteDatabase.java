package documents.functions;

import utils.FileUtils;

public class DeleteDatabase implements DatabaseWriteFunction{

    private final String dbName;

    public DeleteDatabase(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public boolean execute() {

            String path = "src/main/resources/databases/" + dbName;
            if (FileUtils.checkIfFileOrDirectoryExists(path)) {
                if(FileUtils.deleteDirectory(path))
                return true;
            }
            return false;

        }

}
