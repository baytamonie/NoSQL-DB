package documents.entities;

import java.io.Serializable;



public class Packet implements Serializable {

    private final String message;
    private static final long serialVersionUID = 1;
    public String getMessage() {
        return message;
    }

    public Packet(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "message='" + message + '\'' +
                '}';
    }
}
