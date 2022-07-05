package databaseTransfer;

import documents.entities.Packet;
import server.ClientHandler;
import utilities.FileUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class HorizontalScaling implements Runnable {

  private final ObjectInputStream objectInputStream;
  private final ObjectOutputStream objectOutputStream;
  private final FileUtils fileUtils = FileUtils.getInstance();
  private List<String> directories;
  private final Socket controller;

  public static boolean isScalingDone = false;

  public HorizontalScaling(
      ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket socket) {

    this.objectInputStream = objectInputStream;
    this.objectOutputStream = objectOutputStream;
    this.controller = socket;
    directories = new LinkedList<>();
  }

  public synchronized void performRefresh() {
    isScalingDone = false;

    try {
      loadDirectories();
      boolean isDone = false;
      while (!isDone) {
        String msg = getMessage();
        switch (msg) {
          case "database":
            String dbPath = getMessage();
            if (!fileUtils.checkIfFileOrDirectoryExists(dbPath))
              fileUtils.makeDirectory(dbPath);
            continue;
          case "collection":
            String collectionPath = getMessage();
            sendPacket(String.valueOf(fileUtils.checkIfFileOrDirectoryExists(collectionPath)));
            if (!fileUtils.checkIfFileOrDirectoryExists(collectionPath)) {
              fileUtils.makeDirectory(collectionPath);
              int fileCount = Integer.parseInt(getMessage());
              for (int i = 0; i < fileCount; i++) receiveFile(collectionPath);
            } else {
              String x = getMessage();

              int versionNumController = Integer.parseInt(x);
              int versionNumHere = getVersionNumber(collectionPath + "/index.txt");
              if (versionNumHere == versionNumController) sendPacket("equal");
              else {
                sendPacket("!equal");
                int fileCount = Integer.parseInt(getMessage());
                for (int i = 0; i < fileCount; i++) receiveFile(collectionPath);
              }
            }
            continue;
          case "_DONE_":
            isDone = true;
            break;
        }
      }
      loadDirectories();
      for (String path : directories) {
        sendPacket(path);
        if (getMessage().equals("NO")) fileUtils.deleteDirectory(path);
      }

      sendPacket("_DONE_");
      System.out.println("DONE REFRESHING NODE");
      isScalingDone = true;
    } catch (Exception e) {
      e.printStackTrace();
      try {
        objectOutputStream.flush();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  private synchronized int getVersionNumber(String path) {
    try (FileInputStream fileInputStream = new FileInputStream(path)) {
      Scanner scanner = new Scanner(fileInputStream, "UTF-8");
      String line = scanner.nextLine();
      int versionNumber = Integer.parseInt(line);
      fileInputStream.close();
      scanner.close();
      return versionNumber;
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public synchronized void sendPacket(String msg) {
    try {
      objectOutputStream.writeObject(new Packet(msg));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private synchronized void receiveFile(String path) {
    try {

      String fileName = getMessage();
      byte[] content = (byte[]) getObject();
      File file = new File(path + "/" + fileName);
      Files.write(file.toPath(), content);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private synchronized String getMessage() {
    try {
      Packet packet = (Packet) objectInputStream.readObject();

      return packet.getMessage();
    } catch (IOException | ClassNotFoundException e) {
      return null;
    }
  }

  public synchronized void loadDirectories() {
    directories = new LinkedList<>();
    File dir = new File("src/main/resources/databases");
    showFiles(dir.listFiles());
  }

  public synchronized void showFiles(File[] files) {
    for (File file : files) {
      if (file.isDirectory()) {
        directories.add(file.getPath());
        showFiles(file.listFiles());
      }
    }
  }

  public synchronized Object getObject() {
    try {
      Object obj = objectInputStream.readObject();
      return obj;
    } catch (IOException | ClassNotFoundException e) {
      return null;
    }
  }

  @Override
  public void run() {
    while (!controller.isClosed()) {
      try {
        Packet msg = (Packet) objectInputStream.readObject();

        if(msg.getMessage().equals("refresh")){
          performRefresh();
        }
      } catch (Exception e) {
        try {
          e.printStackTrace();
          controller.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
