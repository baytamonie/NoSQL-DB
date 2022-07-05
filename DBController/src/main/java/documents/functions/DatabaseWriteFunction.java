package documents.functions;

import utils.DocumentsUtils;
import utils.FileUtils;
import utils.IdsUtils;
import utils.IndexingUtils;

public interface DatabaseWriteFunction {

    FileUtils fileUtils = FileUtils.getInstance();
    IdsUtils idsUtils = IdsUtils.getInstance();
    IndexingUtils indexingUtils = IndexingUtils.getInstance();
    DocumentsUtils documentsUtils = DocumentsUtils.getInstance();

    boolean execute();
}
