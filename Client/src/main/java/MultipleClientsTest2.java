import driver.ClientDriver;

public class MultipleClientsTest2 {
    public static void main(String[] args){
        ClientDriver clientDriver = new ClientDriver();
        clientDriver.createConnection();
        clientDriver.login("bayta", "123");
        System.out.println(clientDriver.getDocument("videos", "aws", "videos.aws.0"));
        while (true){

        }
    }
}
