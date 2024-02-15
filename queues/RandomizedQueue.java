import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[16];
    }

    private void print() {
        System.out.print("[");
        System.out.print(" ");
        for (int i=0; i<N; i++) {
            System.out.print(this.s[i]);
            System.out.print(" ");
        }
        System.out.println("]");
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.N <= 0;
    }
    // return the number of items on the randomized queue
    public int size() {
        return this.N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.N >= s.length) {
            growArray();
        }
        this.s[this.N] = item;
        this.N++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        if (size() <= s.length/4) {
            shrinkArray();
        }
        int i = StdRandom.uniformInt(this.N);
        Item return_item = this.s[i];
        this.s[i] = this.s[this.N-1];
        this.s[this.N-1] = null;
        this.N--;
        return return_item;
    }

    private void growArray() {
        int new_L = this.s.length * 2;
        Item[] new_s = (Item[]) new Object[new_L];
        for (int i = 0; i < this.N; i++) {
            new_s[i] = this.s[i];
        }
        this.s = new_s;
    }

    private void shrinkArray() {
        int new_L = this.s.length / 2;
        Item[] new_s = (Item[]) new Object[new_L];
        for (int i = 0; i < this.N; i++) {
            new_s[i] = this.s[i];
        }
        this.s = new_s;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int i = StdRandom.uniformInt(this.N);
        return this.s[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        RandomizedQueue<Item> iter;
        public RandomizedQueueIterator() {
            iter = new RandomizedQueue<>();
            for (int i = 0; i < N; i++) {
                iter.enqueue(s[i]);
            }
        }
    
        public boolean hasNext() {
            return iter.size() > 0;
        }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (iter.isEmpty()) {
                throw new java.util.NoSuchElementException();
            }
            return iter.dequeue();
        }
    }

    private int returnLength() {
        return this.s.length;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // RandomizedQueue<Integer> d = new RandomizedQueue<>();
        // for (int i = 0; i < 18; i++) {
        //     d.enqueue(i);
        //     System.out.print(d.returnLength());
        //     d.print();   
        // }

        // for (int i = 0; i < 2; i++) {
        //     System.out.println(d.dequeue());
        //     System.out.print(d.returnLength());
        //     d.print();   
        // }

        // for (int i: d) {
        //     System.out.println(i);
        //     d.print();
        // }
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(561);
        queue.enqueue(958);
        queue.enqueue(698);
        Iterator<Integer> bob = queue.iterator();
        while(bob.hasNext()) {
            System.out.println(bob.next());
        }
        System.out.println("");
        queue.enqueue(788);
        queue.enqueue(126);
        queue.enqueue(307);
        queue.enqueue(125);
        queue.enqueue(341);
        queue.enqueue(157);
        Iterator<Integer> bob1 = queue.iterator();
        while(bob1.hasNext()) {
            System.out.println(bob1.next());
        }
    }

}