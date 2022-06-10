import documents.entities.DocumentEntity;
import documents.entities.IntegerEntity;
import org.junit.Test;

public class DocumentEntitiesTesting {

    @Test
    public void checkType(){
        IntegerEntity integerEntity = new IntegerEntity(10);
        System.out.println(integerEntity.getEntityType());
        DocumentEntity document = (DocumentEntity) integerEntity;
        System.out.println(document.getEntityType());
    }

}
