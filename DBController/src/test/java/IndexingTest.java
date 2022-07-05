import org.junit.Test;

import java.io.File;

public class IndexingTest {

    @Test
    public void checkIfIndexesAvailable(){
        String dbName = "videos";
        String collectionName = "aws";
        String path = "src/main/resources/databases/"+dbName+'/'+collectionName;
        File file = new File(path);
    System.out.println(file.getPath());
        File[] files = new File[file.listFiles().length];
        int i=0;
        for (File f : file.listFiles()){
      if (f.getName().equals("data.json")
          || f.getName().equals("schema.json")
          || f.getName().equals("index.txt")
          || f.getName().equals("ids.json")) System.out.println(f.getPath());
            files[i++] = f;
      System.out.println(f.getPath());
      String fileName = f.getName();
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
      System.out.println(fileNameWithoutExtension);
        }



    }

}
