import org.junit.Test;

import java.io.File;

public class HorizontalScaling {


    @Test
    public void goThroughDirectories(){
        File dir = new File("src/main/resources/databases");
        showFiles(dir.listFiles());
    }
    public  void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getPath());
                showFiles(file.listFiles());
            } else {
                System.out.println("File: " + file.getPath());
            }
        }
    }
}
