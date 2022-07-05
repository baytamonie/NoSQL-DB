package cache;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache implements Cache{

    class DoublyListNode{
        String key;
        JSONObject value;
        DoublyListNode prev;
        DoublyListNode next;
    }

    private  final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String,DoublyListNode> cache;
    private final int capacity = 5;
    private DoublyListNode head,tail;

    @Override
    public JSONObject get(String key){
        lock.readLock().lock();
        try{
            DoublyListNode node = cache.get(key);
            if(node == null)
                return null;
            moveToHead(node);
            return node.value;
        }catch (Exception e){
            return null;
        }finally{
            lock.readLock().unlock();
        }

    }

    @Override
    public void put(String key, JSONObject value){
        if(key == null || value == null)
            return;
        DoublyListNode node = cache.get(key);
        lock.writeLock().lock();
        try{
            if(node == null){
                DoublyListNode newNode = new DoublyListNode();
                newNode.key = key;
                newNode.value = value;
                cache.put(key,newNode);
                addNodeToHead(newNode);
                if(cache.size()>capacity){
                    DoublyListNode tail = removeTailNode();
                    cache.remove(tail.key);
                }
            }
            else{
                node.value = value;
                moveToHead(node);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally{
            lock.writeLock().unlock();
        }

    }

    public static LRUCache getInstance(){
        return lruCache;
    }
    private  static final LRUCache lruCache = new LRUCache();
    private LRUCache (){
        cache = new HashMap<>();
        head = new DoublyListNode();
        tail = new DoublyListNode();
        head.next = tail;
        tail.prev = head;

    }

    private  void addNodeToHead(DoublyListNode node){
        if(node == null)
            throw new IllegalArgumentException();
        DoublyListNode previous = head;
        DoublyListNode next = head.next;
        previous.next = node;
        node.prev = previous;
        next.prev = node;
        node.next = next;
    }

    private void removeNode(DoublyListNode node){
        if(node == null)
            throw new IllegalArgumentException();
        DoublyListNode previous = node.prev;
        DoublyListNode next = node.next;

        previous.next = next;
        next.prev = previous;

        node.prev = null;
        node.next = null;
    }

    private void moveToHead(DoublyListNode node){
        if(node == null)
            throw new IllegalArgumentException();
        removeNode(node);
        addNodeToHead(node);
    }

    private DoublyListNode removeTailNode(){
        DoublyListNode res = tail.prev;
        removeNode(res);
        return  res;
    }


}
