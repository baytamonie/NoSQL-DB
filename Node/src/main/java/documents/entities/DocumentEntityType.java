package documents.entities;

public enum DocumentEntityType {
    NULL(1),
    BOOL(2),
    INT(3),
    DECIMAL(4),
    STRING(5),
    ARRAY(6),
    OBJECT(7);

    private final int documentType;

    DocumentEntityType(int documentType) {
        this.documentType = documentType;
    }

    public int getDocumentType() {
        return documentType;
    }
}
