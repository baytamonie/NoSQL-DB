package documents.entities;

public class ObjectPacket {

    private final String message;
    private final Object object;

    public String getMessage() {
        return message;
    }

    public Object getObject() {
        return object;
    }

    public ObjectPacket(String message, Object object) {
        this.message = message;
        this.object = object;
    }
}
