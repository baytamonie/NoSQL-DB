package documents.entities;


public class IdsObject {

    private final long begin;
    private final long end;
    private final String id;

    public IdsObject(long begin, long end, String id) {
        this.begin = begin;
        this.end = end;
        this.id = id;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "IdsDocument{" +
                "begin=" + begin +
                ", end=" + end +
                ", id='" + id + '\'' +
                '}';
    }
}
