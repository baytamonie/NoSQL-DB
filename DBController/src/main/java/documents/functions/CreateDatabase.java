package documents.functions;

import utils.FileUtils;

public class CreateDatabase implements DatabaseWriteFunction{

    private final String dbName;

    public CreateDatabase(String dbName) {
        this.dbName = dbName;
    }
    @Override
    public boolean execute(){

            String path = "src/main/resources/databases/"+dbName;
            FileUtils.makeDirectory(path);
            return true;
    }

}
