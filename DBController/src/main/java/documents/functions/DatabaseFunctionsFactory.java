package documents.functions;

public class DatabaseFunctionsFactory {

  public DatabaseWriteFunction getDataBaseFunction(String function) {
    switch (function) {
      case "createDatabase":
        return new CreateDataBase();
      case "createCollection":
        return new CreateCollection();
      case "addDocument":
        return new AddDocumentToCollection();
      case "DeleteDataBase":
        return new DeleteDataBase();
      case "DeleteCollection":
        return new DeleteCollection();
      default:
        return null;
    }
  }
}
