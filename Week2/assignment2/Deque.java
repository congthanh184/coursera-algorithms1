import edu.princeton.cs.algs4.StdOut;
// import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;
import java.util.NoSuchElementException;
// import java.lang.NullPointerException;

public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int n = 0;  // length of queue

    private class Node {
        private Item item;
        private Node next, previous;
    }

    public Deque() { }

    public boolean isEmpty() 
    {   return n == 0;  }

    public int size() 
    {   return n;   }

    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException();
        Node oldfirst = first;

        first = new Node();
        first.item = item;

        first.previous = null;
        if (oldfirst == null) {
            first.next = null;
            last = first;
        }
        else {
            first.next = oldfirst;
            oldfirst.previous = first;          
        }
        n++;
    }

    public void addLast(Item item) {
        if (item == null) throw new NullPointerException();
        Node oldlast = last;

        last = new Node();
        last.item = item;

        last.next = null;
        if (oldlast == null) {
            last.previous = null;
            first = last;
        }
        else {
            last.previous = oldlast;
            oldlast.next = last;
        }
        n++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item removeitem = first.item;
        first = first.next;
        if (first != null) { first.previous = null; }  
        else { last = null; }      
        n--;
        return removeitem;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item removeitem = last.item;
        last = last.previous;
        if (last != null) { last.next = null; }        
        else { first = null; }
        n--;
        return removeitem;
    }


    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()    { return current != null; }
        public void remove()        { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public static void main(String[] args) {
        // Deque<String> deque = new Deque<String>();
        // while (!StdIn.isEmpty()) {
        //     String s = StdIn.readString();
        //     if (s.equals("-"))  StdOut.print(deque.removeFirst());
        //     else                deque.addFirst(s);
        // }

        // for (String s : deque) {
        //     StdOut.println(s);
        // }

        Deque<Integer> q = new Deque<Integer>();
        q.addFirst(3);
        StdOut.println(q.removeFirst());
        q.addLast(4);
        StdOut.println(q.removeFirst());
    }
}