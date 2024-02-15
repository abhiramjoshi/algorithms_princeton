import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Item[] s;
    private int N;
    private int F;
    // construct an empty deque
    public Deque() {
        s = (Item[]) new Object[16];
        N = s.length/2;
        F = s.length/2;
    }

    private void print() {
        System.out.print("[");
        System.out.print(" ");
        for (int i=F; i<N; i++) {
            System.out.print(this.s[i]);
            System.out.print(" ");
        }
        System.out.println("]");
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.F >= this.N;
    }

    // return the number of items on the deque
    public int size() {
        return this.N - this.F;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.F == 0) {
            growArray();
        }
        this.F--;
        this.s[this.F] = item;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.N == s.length) {
            growArray();
        } 
        this.s[this.N] = item;
        this.N++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        if (size() <= s.length/4) {
            shrinkArray();
        }
        this.F++;
        Item returnItem = this.s[this.F-1];
        this.s[this.F-1] = null;
        return returnItem; 
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        if (size() <= s.length/4) {
            shrinkArray();
        }
        this.N--;
        Item returnItem = this.s[this.N];
        this.s[this.N] = null;
        return returnItem;
    }

    private void growArray() {
        int new_L = this.s.length * 2;
        int new_F = (new_L - size())/2;
        int new_N = (new_L + size())/2;
        Item[] new_s = (Item[]) new Object[new_L]; 
        for (int i = 0; i < size(); i++) {
            new_s[new_F+i] = this.s[this.F+i];
        }
        this.s = new_s;
        this.F = new_F;
        this.N = new_N;
    }

    private void shrinkArray() {
        int new_L = this.s.length / 2;
        int new_F = (new_L - size())/2;
        int new_N = (new_L + size())/2;
        Item[] new_s = (Item[]) new Object[new_L]; 
        for (int i = 0; i < size(); i++) {
            new_s[new_F+i] = this.s[this.F+i];
        }
        this.s = new_s;
        this.F = new_F;
        this.N = new_N;
    }
    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }


    private class DequeIterator implements Iterator<Item> {
        int i = F;

        public boolean hasNext() { return i < N; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (i >= N) {
                throw new java.util.NoSuchElementException();
            }
            return s[i++]; 
        }
    }

    private int returnLength() {
        return this.s.length;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();
        for (int i = 0; i < 8; i++) {
            d.addLast(i);
            d.print();
            System.out.println(d.returnLength());
        }
        
        for (int i = 8; i < 17; i++) {
            d.addFirst(i);
            d.print();
            System.out.println(d.returnLength());
        }
        
        for (int i = 0; i <26; i++) {
            d.removeLast();
            d.print();
            System.out.println(d.returnLength());
        }
        
    }
}