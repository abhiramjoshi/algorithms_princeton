import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        double i = 0;
        double p;
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            i++;
            p = 1 / i;
            if (StdRandom.bernoulli(p)) {
                champion = word;
            }
        }
        System.out.println(champion);
    }
}
