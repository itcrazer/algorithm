package zcom.fanjg.lru;

import java.util.HashMap;

/**
 * @author fjg_w
 * 该类通过双向链表 + hashmap的方式实现LRU算法
 */
public class LruAlgorithm<k,v> {
    /**
     * 双向链表的数据结构
     * @param <k>
     * @param <v>
     */
    class Node <k,v>{
        private k  key;
        private v  value;
        private Node<k,v> pre;
        private Node<k,v> next;

        public k getKey() {
            return key;
        }

        public void setKey(k key) {
            this.key = key;
        }

        public v getValue() {
            return value;
        }

        public void setValue(v value) {
            this.value = value;
        }

        public Node<k, v> getPre() {
            return pre;
        }

        public void setPre(Node<k, v> pre) {
            this.pre = pre;
        }

        public Node<k, v> getNext() {
            return next;
        }

        public void setNext(Node<k, v> next) {
            this.next = next;
        }
    }

    private HashMap<k,Node<k,v>> data = new HashMap<>();
    private Node<k,v> head;
    private Node<k,v> tail;
    private int size = 0;
    private int capacity = 4;
    /**
     * 设置
     * @param key
     * @param value
     * @return
     */
    public boolean set(k key,v value){
        if(data.containsKey(key)){
            Node<k, v> node = data.get(key);
            node.setValue(value);
            pushFront(node);
        }else{
            size ++;
            Node<k,v> newNode = new Node<>();
            newNode.setValue(value);
            newNode.setKey(key);
            if(size > capacity){
                executeLRU();
            }
            data.put(key,newNode);
            newNode.setNext(head);
            if(head != null){
                head.setPre(newNode);
            }else{
                tail = newNode;
            }
            head = newNode;
        }
        return true;
    }

    /**
     * 执行LRU算法
     */
    private void executeLRU(){
        Node<k,v> old = tail;
        tail = tail.getPre();
        tail.setNext(null);
        data.remove(old.getKey());
        size -- ;
    }

    /**
     * 获取算法
     * @param key
     * @return
     */
    public v get(k key){
        if(data.containsKey(key)){
            Node<k, v> node = data.get(key);
            pushFront(node);
            return node.getValue();
        }
        return null;
    }

    /**
     * 元素链表位置前置
     * 链表中某个数据元素的移动操作
     * @param current
     */
    private void pushFront(Node<k,v> current){
        /**
         * 如果链表为空或者长度为1 则不做任何操作
         */
        if (head == null || size == 1) {
            return;
        }
        //否则将当前节点移动至head位置，操作过程分为两步，
        // 1：将当前节点从当前位置删除，
        // 2：将当前节点移动至head
        if(current == tail){
            tail = current.pre;
        }
        current.getPre().setNext(current.getNext());
        if(current.getNext() != null){
            current.getNext().setPre(current.getPre());
        }

        current.setNext(head);
        current.setPre(null);
        head.setPre(current);
        head = current;


    }

    public static  void main(String[] args){
        LruAlgorithm<String,String> lru = new LruAlgorithm<>();
        lru.set("1","1");
        lru.set("2","2");
        lru.get("1");
        lru.set("3","3");
        lru.get("2");
        lru.set("4","4");
        lru.set("5","5");
        LruAlgorithm.Node t = lru.head;
        while (t != null){
            System.out.println(t.getKey());
            t = t.next;
        }
    }

}


