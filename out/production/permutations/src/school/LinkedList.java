package school;

import java.util.NoSuchElementException;

/**
 * Created by swaggerton on 15/11/15.
 */
public class LinkedList<E>
{
    private static class Node<E>
    {
        public E data;
        public Node<E> next;
        public Node(E data, Node<E> next)
        {
            this.data = data;
            this.next = next;
        }
    }
    private Node<E> head;
    // the number of elements in the list
    private int count;
    public LinkedList()
    {
        head = null;
        count = 0;
    }
    public void addToFront(E e)
    {
        head = new Node<E>(e, head);
        count++;
    }
    public void remove(E e)
    {
        Node<E> previous = null;
        Node <E>current = head;
        while (current != null)
        {
            if(current.data.equals(e))
            {
                current = current.next;
                if(previous == null)
                {
                    previous = current;
                }
                else
                {
                    previous.next = current;
                    count--;
                }
            }
            else
            {
                previous = current;
                current = current.next;
            }
        }
        count--;
    }
    // size() returns the number of elements in this list.
    public int size()
    {
        return count;
    }
    public void clear()
    {
        head = null;
        count = 0;
    }
    // iterator() returns a new iterator that has not yet returned any of
    // the elements of this list.
    public Iterator iterator()
    {
        return new Iterator();
    }
    public class Iterator
    {
        // you'll need to add fields here
        public Node<E> cursor;
        // The constructor initializes a new iterator that has not yet
        // returned any of the elements of the list.
        public Iterator()
        {
            cursor = head;
        }
        // hasNext() returns true if there are more elements in the list
        // that have not been returned, and false if there are no more
        // elements.
        public boolean hasNext()
        {
            if(cursor.next == null)
            {
                return true;
            }
            return false;
        }
        public E next()
        {
            if(hasNext())
            {
                throw new NoSuchElementException("No next element");
            }
            E toReturn = cursor.data;
            cursor = cursor.next;
            return toReturn;
        }
    }
    public static int length (Node l)
    {
        if (l == null)
            return 0;
        else
            return 1 + length(l.next);
    }
}