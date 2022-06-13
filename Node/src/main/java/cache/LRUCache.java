package cache;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    class DoublyListNode{
        String key;
        JSONObject value;
        DoublyListNode prev;
        DoublyListNode next;
    }

    private final  Map<String,DoublyListNode> cache;
    private final int capacity;
    private DoublyListNode head,tail;

    public JSONObject get(String key){
        DoublyListNode node = cache.get(key);
        if(node == null)
            return null;
        moveToHead(node);
        return node.value;
    }

    public void put(String key, JSONObject value){
        DoublyListNode node = cache.get(key);
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


    public LRUCache (int capacity){
        cache = new HashMap<>();
        this.capacity = capacity;
        head = new DoublyListNode();
        tail = new DoublyListNode();
        head.next = tail;
        tail.prev = head;

    }

    private void addNodeToHead(DoublyListNode node){
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
