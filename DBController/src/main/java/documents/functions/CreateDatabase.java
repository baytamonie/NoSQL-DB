package documents.functions;

public class CreateDatabase implements DatabaseWriteFunction{

    private final String dbName;

    public  CreateDatabase(String dbName) {
        this.dbName = dbName;
    }
    @Override
    public synchronized boolean execute(){

            String path = "src/main/resources/databases/"+dbName;
        fileUtils.makeDirectory(path);
            return true;
    }

}
