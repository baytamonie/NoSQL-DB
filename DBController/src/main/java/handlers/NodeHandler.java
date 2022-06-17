package handlers;

import controller.Controller;
import documents.entities.Node;
import documents.entities.Packet;
import documents.functions.DatabaseFunctionsFactory;
import documents.functions.DatabaseWriteFunction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NodeHandler implements Runnable {

  private final Socket socket;
  private ObjectInputStream objectInputStream;
  private final DatabaseFunctionsFactory functionsFactory;
  private final ObjectOutputStream objectOutputStream;
  private Node node;

  public NodeHandler(Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
    this.socket = socket;
    this.objectInputStream = objectInputStream;
    this.objectOutputStream = objectOutputStream;
    functionsFactory = new DatabaseFunctionsFactory(objectInputStream);
  }
  public void getNodePortNumber(){
    try {
      Packet portNumberPacket = (Packet) objectInputStream.readObject();
      this.node = new Node(Integer.valueOf(portNumberPacket.getMessage()));
      System.out.println("Node received. Node is at port: " + node.getPort());
      Controller.nodes.add(node);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

  }
  public void updateLoadForANode(Node node){
    try {
      Packet loadString = (Packet) objectInputStream.readObject();
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

    try {
      getNodePortNumber();
      while (!socket.isClosed()) {
        Packet packet = (Packet) objectInputStream.readObject();
        switch (packet.getMessage()) {
          case "load":
            updateLoadForANode(node);
            break;
          default:
            DatabaseWriteFunction databaseWriteFunction = functionsFactory.getDataBaseFunction(packet.getMessage());
            if(databaseWriteFunction!=null){
            boolean didFunctionExecute =   databaseWriteFunction.execute();
              System.out.println(packet.getMessage());
            if(didFunctionExecute)
              objectOutputStream.writeObject(new Packet("true"));
            else
              objectOutputStream.writeObject(new Packet("false"));

            }
        }
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
        throw new RuntimeException(ex);
      }

    }
  }
}
