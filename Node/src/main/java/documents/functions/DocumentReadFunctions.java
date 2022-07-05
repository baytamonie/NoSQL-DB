package documents.functions;

import cache.LRUCache;
import utilities.DocumentUtils;
import utilities.FileUtils;

import javax.print.Doc;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface DocumentReadFunctions  {

     LRUCache lruCache = LRUCache.getInstance();

     FileUtils fileUtils = FileUtils.getInstance();
     DocumentUtils documentsUtils = DocumentUtils.getInstance();
     Object execute();


}
