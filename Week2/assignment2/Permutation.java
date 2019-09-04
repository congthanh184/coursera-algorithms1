import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();

        int k = Integer.parseInt(args[0]);
        int n = 0;

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            n++;
            if (k != 0) {
                if (queue.size() < k) { queue.enqueue(item); }
                else {
                    int threshold = (int)(((double)k/n) * 1000);
                    int randProb = StdRandom.uniform(1000);
                    if (randProb < threshold) { 
                        queue.dequeue(); 
                        queue.enqueue(item); 
                    }                     
                }
            }
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}