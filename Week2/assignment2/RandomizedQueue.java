import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;
import java.util.Iterator;
// import java.lang.NullPointerException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] randQueue;
    private int n = 0;

    // construct an empty randomized queue
    public RandomizedQueue()                 
    {
        randQueue = (Item[]) new Object[2];
        n = 0;      
    }

    // is the queue empty?
    public boolean isEmpty()                 
    {
        return n == 0;
    }

    // return the number of items on the queue
    public int size()                        
    {
        return n;
    }

    // resize the underlying array
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = randQueue[i];
        }
        randQueue = temp;
    }

    // add the item
    public void enqueue(Item item)           
    {
        if (item == null) throw new NullPointerException();
        if (n == randQueue.length) resize(2*randQueue.length);      // double the size if necessary
        randQueue[n] = item;                                        // randQueue has index from 0..N-1
        n++;
    }

    // remove and return a random item
    public Item dequeue()                    
    {
        if (isEmpty()) throw new NoSuchElementException();
        int idxDequeue = StdRandom.uniform(n);
        Item item = randQueue[idxDequeue];
        randQueue[idxDequeue] = randQueue[n-1];                     // replace the dequeue item by the last item in the queue
        randQueue[n-1] = null;
        n--;
        if (n > 0 && n == randQueue.length/4) resize(randQueue.length/2);
        return item;
    }

    // return (but do not remove) a random item
    public Item sample()                     
    {
        if (isEmpty()) throw new NoSuchElementException();
        return randQueue[StdRandom.uniform(n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()         
    {
        return new ArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] iteratorQueue;

        public ArrayIterator() {
            iteratorQueue = (Item[]) new Object[n];
            for (int id = 0; id < n; id++) {
                iteratorQueue[id] = randQueue[id];
            }

            StdRandom.shuffle(iteratorQueue);
        }

        public boolean hasNext()  { return i < n;                               }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = iteratorQueue[i];
            i++;
            return item;
        }
    }

    public static void main(String[] args)
    {
        RandomizedQueue<Integer> intq = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            intq.enqueue(i);
        }

        for (int i : intq) {
            StdOut.println(i);
        }
    }
}