package handlers;

import controller.Controller;
import documents.entities.Node;
import documents.entities.Packet;
import documents.functions.DatabaseFunctionsFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NodeHandler implements Runnable {

  private final Socket socket;
  private ObjectInputStream objectInputStream;
  private final DatabaseFunctionsFactory functionsFactory;

  public NodeHandler(Socket socket, ObjectInputStream objectInputStream) {
    this.socket = socket;
    this.objectInputStream = objectInputStream;
    functionsFactory = new DatabaseFunctionsFactory();
  }
  public void getNodePortNumber(){
    try {
      Packet portNumberPacket = (Packet) objectInputStream.readObject();
      Node node = new Node(Integer.valueOf(portNumberPacket.getMessage()));
      System.out.println("Node received. Node is at port: " + node.getPort());
      Controller.nodes.add(node);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

  }
  public void updateLoadForANode(Node node){
    Packet loadString = null;
    try {
      loadString = (Packet) objectInputStream.readObject();
      node.setLoad(Integer.valueOf(loadString.getMessage()));
      System.out.println("load updated for node " + node.getPort());


    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void run() {
    Node node = null;
    try {
      getNodePortNumber();
      while (!socket.isClosed()) {
        Packet packet = (Packet) objectInputStream.readObject();
        switch (packet.getMessage()) {
          case "load":
            updateLoadForANode(node);
            break;
          case "CreateDataBase":

        }

      }
    } catch (Exception e) {
      e.printStackTrace();
      if(node!=null)
        Controller.nodes.remove(node);
        System.out.println("node "+node.getPort()+" disconnected");
    }
  }
}
