import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class FileTesting {

  @Test
  public void checkIfDbExists()  {
    String path = "src/main/resources/databases/";
    path = path + "videos";
    File tempFile = new File(path);
    assertTrue(tempFile.exists());

  }

  @Test
  public void indexingTest() {
    String path = "src/main/resources/databases/" + "videos" + '/' + "aws" + '/' + "name" + ".json";
    File file = new File(path);
    assertTrue(file.exists());
    path = "src/main/resources/databases/" + "videos" + '/' + "aws" + '/' + "description" + ".json";
    file = new File(path);
    assertFalse(file.exists());
  }
}
