package documents.entities;

import java.io.Serializable;

public class Packet implements Serializable {

    private final String message;
    private static final long serialVersionUID = 3567653491060394677L;

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "message='" + message + '\'' +
                '}';
    }

    public Packet(String message) {
        this.message = message;
    }
}
