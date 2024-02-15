import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]); 
        RandomizedQueue<String> r = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            r.enqueue(word);
        }

        for (int i = 0; i < k; i++) {
            System.out.println(r.dequeue());
        }
    }
}
