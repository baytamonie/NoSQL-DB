package documents.entities;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Node implements Comparable<Node> {

  private final int port;
  private final Socket socket;
  private final ObjectInputStream objectInputStream;
  private final ObjectOutputStream objectOutputStream;
  private int load;

  public Node(
      int port,
      Socket socket,
      ObjectInputStream objectInputStream,
      ObjectOutputStream objectOutputStream) {
    load = 0;
    this.port = port;
    this.socket = socket;
    this.objectInputStream = objectInputStream;
    this.objectOutputStream = objectOutputStream;
  }

  public int getPort() {
    return port;
  }

  public Socket getSocket() {
    return socket;
  }

  public ObjectInputStream getObjectInputStream() {
    return objectInputStream;
  }

  public ObjectOutputStream getObjectOutputStream() {
    return objectOutputStream;
  }

  public int getLoad() {
    return load;
  }

  public void setLoad(int load) {
    this.load = load;
  }

  public void incrementLoad() {
    load++;
  }

  public void decrementLoad() {
    if (load > 0) load--;
  }

  @Override
  public int compareTo(Node node) {
    return Integer.compare(this.getLoad(), node.getLoad());
  }
}
