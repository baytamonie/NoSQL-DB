package documents.entities;

public class Node implements Comparable<Node> {

    private final int port;
    private int load;
    public Node(int port){
        load = 0;
        this.port = port;
    }
    public int getPort() {
        return port;
    }

    public int getLoad() {
        return load;
    }
    public void setLoad(int load){
        this.load = load;
    }

    @Override
    public int compareTo(Node node) {
        if(this.getLoad()<node.getLoad())
            return -1;
        if(this.getLoad()>node.getLoad())
            return 1;
        else return 0;

    }
}
