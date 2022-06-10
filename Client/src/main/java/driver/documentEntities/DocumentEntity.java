package driver.documentEntities;

public abstract class DocumentEntity implements Comparable<DocumentEntity> {

  public abstract DocumentEntityType getEntityType();

  protected abstract int compareToSameType(DocumentEntity entity);

  @Override
  public int compareTo(DocumentEntity entity) {
    if (this == entity) {
      return 0;
    }
    if (getEntityType() != entity.getEntityType()) {
      return Integer.compare(
          getEntityType().getDocumentType(), entity.getEntityType().getDocumentType());
    }
    return compareToSameType(entity);
  }
}

