package documents.entities;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Node implements Comparable<Node> {

  private final int port;

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
    this.objectInputStream = objectInputStream;
    this.objectOutputStream = objectOutputStream;
  }

  public int getPort() {
    return port;
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

  public synchronized void setLoad(int load) {
    this.load = load;
  }

  public synchronized void incrementLoad() {
    load++;
  }

  public synchronized void decrementLoad() {
    if (load > 0) load--;
  }

  @Override
  public int compareTo(Node node) {
    return Integer.compare(this.getLoad(), node.getLoad());
  }
}
