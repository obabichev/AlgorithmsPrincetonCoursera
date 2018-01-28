import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private class Node {
        Node(Object data) {
            this.data = data;
        }

        Object data;
        Node next;
    }

    private int size = 0;
    private Node root = null;

    public RandomizedQueue() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item);
        node.next = root;
        root = node;
        size++;
    }

    private Node get(int index) {
        Node node = this.root;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    private void remove(int index) {
        if (index == 0) {
            root = root.next;
        } else {
            Node node = get(index - 1);
            node.next = node.next.next;
        }
        size--;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(size());
        return dequeue(index);
    }

    private Item dequeue(int index) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = (Item) get(index).data;
        remove(index);
        return item;
    }


    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(size());
        return (Item) get(index).data;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        Object[] items;

        int current = 0;

        RandomizedQueueIterator() {
            items = new Object[size()];
            Node node = root;
            for (int i = 0; i < size(); i++) {
                items[i] = node.data;
                node = node.next;
            }
            StdRandom.shuffle(items);
        }

        @Override
        public boolean hasNext() {
            return current < items.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (Item) items[current++];
        }
    }

    private void print() {
        Node node = root;
        for (int i = 0; i < size; i++) {
            System.out.printf(node.data + " ");
            node = node.next;
        }
        System.out.println();
    }


    public static void main(String[] args) {
        final RandomizedQueue<Integer> q = new RandomizedQueue<>();
        for (int i = 0; i < 5; i++) {
            q.enqueue(i);
        }
        q.print();

        q.dequeue(3);
        q.print();

        for (Integer integer : q) {
            System.out.printf(integer + " ");
        }
        System.out.println();

        for (Integer integer : q) {
            System.out.printf(integer + " ");
        }
        System.out.println();

        for (Integer integer : q) {
            System.out.printf(integer + " ");
        }
        System.out.println();
    }
}