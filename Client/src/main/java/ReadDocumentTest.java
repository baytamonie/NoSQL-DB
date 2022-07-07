import driver.ClientDriver;

public class ReadDocumentTest {
  public static ClientDriver clientDriver = new ClientDriver();

  public static void main(String[] args) {
    clientDriver = new ClientDriver();
    clientDriver.createConnection();
    clientDriver.login("bayta","123");
    getRightDocument();
    getWrongDocument();
  }

  public static void getWrongDocument() {
    assert null == clientDriver.getDocument("videos", "aws", "document1");
  }

  public static void getRightDocument() {
    assert null != clientDriver.getDocument("videos", "aws", "videos.aws.0");
    System.out.println(clientDriver.getDocument("videos","aws","videos.aws.0"));
  }
}
