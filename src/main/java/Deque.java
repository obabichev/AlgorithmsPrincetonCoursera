import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private static final int INITIAL_SIZE = 10;

    private Item[] items;

    private int left = INITIAL_SIZE / 2;
    private int right = INITIAL_SIZE / 2;

    public Deque() {
        setItems((Item[]) new Object[INITIAL_SIZE]);
    }

    private void setItems(Item[] items) {
        this.items = items;
    }

    public boolean isEmpty() {
        return left == right;
    }

    public int size() {
        return right - left;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        left--;
        items[left] = item;
        resize();
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        items[right] = item;
        right++;
        resize();
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        final Item item = items[left];
        items[left] = null;
        left++;
        resize();
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        right--;
        final Item item = items[right];
        items[right] = null;
        resize();
        return item;
    }

    private void resize() {
        if (right > items.length * 3 / 4 || left < items.length / 4) {
            resize(items.length * 2);
            return;
        }
        if ((left > items.length / 2 || right < items.length / 2) && items.length > INITIAL_SIZE) {
            resize(items.length / 2);
        }
    }

    private void resize(int length) {
        final Object[] newItems = new Object[length];
        int newLeft = length / 2 - size() / 2;
        int newRight = newLeft + size();
        for (int i = 0; i < size(); i++) {
            newItems[newLeft + i] = items[left + i];
        }
        setItems((Item[]) newItems);
        left = newLeft;
        right = newRight;
    }

    private void print() {
        System.out.printf(items.length - size() + " ");
        for (Item item : items) {
            System.out.printf(item + " ");
        }
        System.out.println();
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private int current = left;

        @Override
        public boolean hasNext() {
            return current != right;
        }

        @Override
        public Item next() {
            if (current >= right) {
                throw new NoSuchElementException();
            }
            return items[current++];
        }
    }

    public static void main(String[] args) {
        System.out.println("HI");

        final Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 20; i++) {
            deque.addLast(i);
            deque.print();
        }

        for (Integer integer : deque) {
            System.out.printf(integer + "#");
        }
    }
}
