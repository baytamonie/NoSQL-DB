package handlers;

import controller.Controller;
import databaseTransfer.HorizontalScaling;
import documents.entities.Node;
import documents.entities.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NodeHandler implements Runnable {
  ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
  private final Socket socket;
  private final ObjectInputStream objectInputStream;
  private final ObjectOutputStream objectOutputStream;
  private  static HorizontalScaling horizontalScaling;
  private Node node;

  public NodeHandler(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
    this.socket = socket;
    this.objectInputStream = objectInputStream;
    this.objectOutputStream = objectOutputStream;

  }
  public synchronized void getNodePortNumber(){
    try {
      Packet portNumberPacket = (Packet) objectInputStream.readObject();
      this.node = new Node(Integer.parseInt(portNumberPacket.getMessage()),socket,objectInputStream,objectOutputStream);
      System.out.println("Node received. Node is at port: " + node.getPort());
      horizontalScaling = new HorizontalScaling(objectInputStream,objectOutputStream);
      Controller.addNode(node);
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

  }
  public static synchronized void refreshNode( )  {
    try{
      horizontalScaling.refreshNode();
    }
 catch (Exception e){
      e.printStackTrace();
 }
  }

  @Override
  public void run() {

    try {
      getNodePortNumber();
      refreshNode();
      while(!socket.isClosed()){

      }
    } catch (Exception e) {
      e.printStackTrace();
      if(node!=null){
        Controller.removeNode(node);
        System.out.println("node "+node.getPort()+" disconnected");
      }
      try {
        socket.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }

    }
  }
}
