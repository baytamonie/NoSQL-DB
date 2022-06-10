import documents.JSONConverter;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Server server = Server.getServer();
        System.out.println("To stop server, type STOP");
        server.startNode();


    }

}
