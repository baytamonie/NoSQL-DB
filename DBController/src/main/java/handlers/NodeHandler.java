package handlers;

import controller.Controller;
import databaseTransfer.HorizontalScaling;
import documents.entities.Node;
import documents.entities.Packet;
import documents.functions.DatabaseFunctionsFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NodeHandler implements Runnable {
  ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
  private final Socket socket;
  private ObjectInputStream objectInputStream;
  private final DatabaseFunctionsFactory functionsFactory;
  private final ObjectOutputStream objectOutputStream;
  private  static HorizontalScaling horizontalScaling;
  private Node node;

  public NodeHandler(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
    this.socket = socket;
    this.objectInputStream = objectInputStream;
    this.objectOutputStream = objectOutputStream;
    functionsFactory = new DatabaseFunctionsFactory(objectInputStream);
  }
  public synchronized void getNodePortNumber(){
    try {
      Packet portNumberPacket = (Packet) objectInputStream.readObject();
      this.node = new Node(Integer.parseInt(portNumberPacket.getMessage()),socket,objectInputStream,objectOutputStream);
      System.out.println("Node received. Node is at port: " + node.getPort());
      horizontalScaling = new HorizontalScaling(objectInputStream,objectOutputStream);
      Controller.nodes.add(node);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
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

  public synchronized void updateLoadForANode(Node node){
    try {
      System.out.println("UPDATING LOAD");
      Packet loadString = (Packet) objectInputStream.readObject();
      System.out.println(loadString);
      node.setLoad(Integer.parseInt(loadString.getMessage()));
      System.out.println("load updated for node " + node.getPort());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void run() {

    try {
      getNodePortNumber();
      refreshNode();
      while (!socket.isClosed()) {
//        Packet packet = (Packet) objectInputStream.readObject();
//        if ("load".equals(packet.getMessage())) {
//          updateLoadForANode(node);
//        }

        }
    } catch (Exception e) {
      e.printStackTrace();
      if(node!=null){
        Controller.nodes.remove(node);
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
