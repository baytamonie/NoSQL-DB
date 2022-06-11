package handlers;

import controller.Controller;
import documents.entities.Node;
import documents.entities.Packet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NodeHandler implements Runnable {

  private final Socket socket;
  private ObjectInputStream objectInputStream;

  public NodeHandler(Socket socket, ObjectInputStream objectInputStream) {
    this.socket = socket;
    this.objectInputStream = objectInputStream;
  }

  @Override
  public void run() {
    Node node = null;
    try {
      Packet portNumberPacket = (Packet) objectInputStream.readObject();
      node = new Node(Integer.valueOf(portNumberPacket.getMessage()));
      System.out.println("Node received. Node is at port: " + node.getPort());
      Controller.nodes.add(node);
      while (!socket.isClosed()) {
        Packet packet = (Packet) objectInputStream.readObject();
        switch (packet.getMessage()) {
          case "load":
            Packet loadString = (Packet) objectInputStream.readObject();
            node.setLoad(Integer.valueOf(loadString.getMessage()));
            System.out.println("load updated for node " + node.getPort());
            break;
        }

      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getCause());
      if(node!=null)
        Controller.nodes.remove(node);
        System.out.println("node "+node.getPort()+" disconnected");
    }
  }
}
