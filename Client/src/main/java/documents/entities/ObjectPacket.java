package documents.entities;

public class ObjectPacket {

    private final String message;
    private final String object;

    public ObjectPacket(String message, String object) {
        this.message = message;
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public String getObject() {
        return object;
    }
}
