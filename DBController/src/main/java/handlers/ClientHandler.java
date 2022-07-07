package handlers;

import controller.Controller;
import databaseTransfer.HorizontalScaling;
import documents.entities.Client;
import documents.entities.Node;
import documents.entities.Packet;
import documents.functions.DatabaseFunctionsFactory;
import documents.functions.DatabaseWriteFunction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ClientHandler implements Runnable {

  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  private final ObjectOutputStream objectOutputStream;
  private final ObjectInputStream objectInputStream;
  private final Socket client;
  private  final Client c;
  public ClientHandler(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket socket, int id) {
    this.objectOutputStream = objectOutputStream;
    this.objectInputStream = objectInputStream;
    this.client = socket;
     c = new Client(id);
    Controller.clients.add(c);
  }

  private String getMsg() {
    try {
      return ((Packet) objectInputStream.readObject()).getMessage();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  private void sendMsg(String msg) {
    try {
      objectOutputStream.writeObject(new Packet(msg));
    } catch (Exception e) {
      e.printStackTrace();

    }
  }
  public static synchronized  void refreshAllNodes(){
      List<Node> nodes  = Controller.getNodes();
    for(Node n: nodes){
      System.out.println("NODE TO REFRESH : "+n.getPort());
      ObjectOutputStream objectOutputStream1 = n.getObjectOutputStream();
      if(objectOutputStream1!=null){
        HorizontalScaling horizontalScaling = new HorizontalScaling(n.getObjectInputStream(),n.getObjectOutputStream());
        horizontalScaling.refreshNode();
      }
    }

  }
  @Override
  public void run() {

    if (Controller.getNodes().isEmpty()) {
      System.out.println("No available nodes right now to serve the client");
    } else {
      try {
        lock.writeLock().lock();
        Collections.sort(Controller.getNodes());
        for (Node node : Controller.getNodes()) {
          System.out.println("NODE " + node.getPort() + "    " + node.getLoad());
        }
        System.out.println("Client connected at node " + (Controller.getNodes().get(0).getPort()));
        objectOutputStream.writeObject(
            new Packet(String.valueOf(Controller.getNodes().get(0).getPort())));
        Controller.getNodes().get(0).incrementLoad();
        Controller.clientsMapper.put(c.getId(),Controller.getNodes().get(0));
        lock.writeLock().unlock();
      } catch (IOException e) {
        System.out.println("Error connecting client to node");
        return;
      }
      DatabaseFunctionsFactory databaseFunctionsFactory =
          new DatabaseFunctionsFactory(objectInputStream);
      while (!client.isClosed()) {
        try{
          String command = getMsg();
          if(command==null){
            try{
              Controller.clients.remove(c);
              Controller.clientsMapper.get(c.getId()).decrementLoad();
              System.out.println("client disconnected");
              client.close();
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }
          DatabaseWriteFunction writeFunction = databaseFunctionsFactory.getDataBaseFunction(command);
          boolean didFunctionExecute;
          if(writeFunction!=null){
            didFunctionExecute = writeFunction.execute();
            sendMsg(String.valueOf(didFunctionExecute));
            refreshAllNodes();
            System.out.println("REFRESHED NODES");
            sendMsg("done");
          }
          else {
            sendMsg("false");
          }}
        catch (Exception e){
          e.printStackTrace();
          try{
            if(client.isClosed())
              continue;
            client.close();
            Controller.clients.remove(c);
            Controller.clientsMapper.get(c.getId()).decrementLoad();
            System.out.println("client disconnected");
          } catch (IOException ex) {
            Controller.clients.remove(c);
            Controller.clientsMapper.get(c.getId()).decrementLoad();
            System.out.println("client disconnected");
            ex.printStackTrace();
          }
        }


      }
    }
  }
}
