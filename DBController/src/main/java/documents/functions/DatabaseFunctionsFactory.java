package documents.functions;

public class DatabaseFunctionsFactory {

  public DatabaseWriteFunction getDataBaseFunction(String function) {
    switch (function) {
      case "CreateDataBase":
        return new CreateDataBase();
      case "CreateCollection":
        return new CreateCollection();
      case "DeleteDataBase":
        return new DeleteDataBase();
      case "DeleteCollection":
        return new DeleteCollection();
      default:
        return null;
    }
  }
}
