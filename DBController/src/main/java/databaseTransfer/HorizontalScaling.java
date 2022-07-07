package databaseTransfer;

import documents.entities.Packet;
import utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class HorizontalScaling {

  private final ObjectInputStream objectInputStream;
  private final ObjectOutputStream objectOutputStream;
  private List<File> directories;
  private final FileUtils fileUtils = FileUtils.getInstance();

  public HorizontalScaling(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
    this.objectInputStream = inputStream;
    this.objectOutputStream = outputStream;
  }

  public void loadDirectories() {
    directories = new LinkedList<>();
    File dir = new File("src/main/resources/databases");
    listFiles(Objects.requireNonNull(dir.listFiles()));
  }

  public void listFiles(File[] files) {
    for (File file : files) {
      if (file.isDirectory()) {
        directories.add(file);
        listFiles(Objects.requireNonNull(file.listFiles()));
      }
    }
  }

  private void sendFile(String path) {
    File file = new File(path);
    try {
      String fileName = file.getName();
      byte[] fileContentBytes = Files.readAllBytes(file.toPath());
      sendPacket(fileName);
      sendObject(fileContentBytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void refreshNode() {
    sendPacket("refresh");
    loadDirectories();
    for (File dir : directories) {
      if (dir.getParentFile().getName().equals("databases")) {
        sendPacket("database");
        sendPacket(dir.getPath());
        continue;
      }
      sendPacket("collection");
      sendPacket(dir.getPath());
      String isCollectionThere = getMessage();
      if (Objects.equals(isCollectionThere, "false")) {
        int fileCount = Objects.requireNonNull(dir.listFiles()).length;
        sendPacket(String.valueOf(fileCount));
        for (File file : Objects.requireNonNull(dir.listFiles())) {
          sendFile(file.getPath());
        }
      } else if (Objects.equals(isCollectionThere, "true")) {
        int versionNum = getFileVersionNumber(dir.getPath() + "/index.txt");

        sendPacket(String.valueOf(versionNum));
        String response = getMessage();
        if (response.equals("!equal")) {
          int fileCount = Objects.requireNonNull(dir.listFiles()).length;
          sendPacket(String.valueOf(fileCount));
          for (File file : Objects.requireNonNull(dir.listFiles())) {
            sendFile(file.getPath());
          }
        }
      }
    }
    sendPacket("_DONE_");
    while (true) {
      String msg = getMessage();
      if (Objects.equals(msg, "_DONE_")) return;
      if (fileUtils.checkIfFileOrDirectoryExists(msg)) {
        sendPacket("YES");

      } else {
        sendPacket("NO");
      }
    }
  }

  private int getFileVersionNumber(String path) {
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

  private String getMessage() {
    try {
      Object obj = objectInputStream.readObject();
      Packet packet = (Packet) obj;

      return packet.getMessage();

    } catch (IOException | ClassNotFoundException e) {
      return null;
    }
  }

  private void sendObject(Object obj) {
    try {
      objectOutputStream.writeObject(obj);
      objectOutputStream.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void sendPacket(String msg) {
    try {
      objectOutputStream.writeObject(new Packet(msg));
      objectOutputStream.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
