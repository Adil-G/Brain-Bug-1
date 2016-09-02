package school.Test4.LinkedListTests;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by swaggerton on 15/11/15.
 */
public class LinkedList
{
    private int size;
    private Node head;

    private static class Node
    {
        private char data;
        private Node next;
        public Node(char c)
        {
            this.data = c;
            this.next = null;
        }
    }

    public LinkedList()
    {
        // CREATE EMPTY LSIT
        this.size = 0;
        this.head = null;
    }
    public void add(char c)
    {
        if(this.size == 0)
        {
            this.head = new Node(c);
        }
        else
        {
            LinkedList.add(c, this.head);
        }
        this.size++;
    }
    private static void add(char c, Node node)
    {
        if(node.next == null)
        {
            node.next = new Node(c);
        }
        else
        {
            add(c, node.next);
        }
    }
    public char get(int index)
    {
        if(index < 0 || index >= this.size)
        {
            throw new IndexOutOfBoundsException("Index: "+ index + ", Size: " + this.size);
        }

        return get(index, this.head);
    }
    private static char get(int index, Node node)
   {
       if(index == 0)
       {
           return node.data;
       }
       return get(index - 1, node.next);
   }
    public void set(int index, char c)
    {
        if(index < 0 || index >= this.size)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }

        set(index, this.head, c);
    }
    private static void set(int index, Node node, char c)
    {
        if(index == 0)
        {
            node.data = c;
            return;
        }

        set(index - 1, node.next, c);
    }
    public String toString()
    {
        if(this.head == null)
        {
            return "[" + "]";
        }
        return "[" + toString(this.head);
    }
    private static String toString(Node node)
    {
        if(node.next == null)
        {
            return String.valueOf(node.data) + "]";
        }
        return String.valueOf(node.data) + ", " + toString(node.next);
    }
    public boolean contains(char c)
    {
        if(this.head == null)
        {
            return false;
        }
        return contains(this.head, c);
    }
    private static boolean contains(Node node, char c)
    {
        if(node == null)
        {
            return false;
        }
        if(node.data == c)
        {
            return true;
        }

        return contains(node.next, c);
    }
    public int indexOf(char c)
    {
        if(this.head == null)
        {
            return -1;
        }
        return indexOf(0, c, this.head);
    }
    private static int indexOf(int index, char c, Node node)
    {
        if(node == null)
        {
            return -1;
        }
        if(node.data == c)
        {
            return index;
        }
        return indexOf(++index, c, node.next);
    }
    public void addFirst(char c)
    {
        Node node = new Node(c);
        node.next = this.head;
        this.head = node;
        this.size++;
    }
    private static void add(char c, int index, Node node)
    {
        if(index == 0)
        {
            Node newNode = new Node(c);
            newNode.next = node.next;
            node.next = newNode;
            //this.size++;
            return;
        }
        add(c, --index, node.next);
    }
    public void add(int index, char c)
    {
        if(index < 0 || index > this.size)
        {
            throw new IndexOutOfBoundsException();
        }
        if(index == 0)
        {
            this.addFirst(c);
        }
        else
        {
            add(c, --index, this.head);
            this.size++;
        }
    }
    public char removeFirst()
    {
        if(this.size==0)
        {
            throw new NoSuchElementException();
        }
        char currData = this.head.data;
        Node curr = this.head.next;
        this.head = curr;
        this.size--;
        return currData;
    }
    public char remove(int index)
    {
        if(this.head == null)
        {
            throw new NoSuchElementException();
        }
        if(index < 0 || index > this.size)
        {
            throw new IndexOutOfBoundsException();
        }
        if(index == 0)
        {
            return removeFirst();
        }
        char currData = remove(index -1, this.head);
        this.size--;

        return currData;
    }
    private static char remove(int index, Node prevNode)
    {

        if(index == 0)
        {
            char currData = prevNode.next.data;
            Node curr = prevNode.next.next;
            prevNode.next = curr;
            //this.size--;
            return currData;
        }
        return remove(index - 1, prevNode.next);
    }
    public char remove2(int index)
    {
        if(index < 0 || index >= this.size)
        {
            throw new IndexOutOfBoundsException();
        }
        if(index == 0)
        {
            return removeFirst();
        }
        else
        {
            this.size--;
            return remove2(--index, this.head);

        }
    }
    private static char remove2(int index, Node node)
    {
        if(index == 0)
        {
            char nodeData = node.next.data;
            node.next = node.next.next;
            return nodeData;
        }
        return remove2(--index, node.next);
    }
    public LinkedList h(LinkedList list)
    {
        if(this.size <=1)
        {
            return list;
        }
        return h(list, this.head, this.size);
    }
    private static LinkedList h(LinkedList list, Node n, int listIndex)
    {
        if(listIndex == 0)
        {
            return list;
        }
        if(n == null)
        {
            return null;
        }
        char datax = n.data;
        list.remove(listIndex);
        list.add(listIndex, datax);
        return h(list, n.next, listIndex - 1);
    }
    // PRACTICE lINKEDlISTiTERATOR
    private class LinkedListIterator implements  Iterator<Character> {
        private Node currNode;
        private Node prevNode;

        public LinkedListIterator() {
            this.currNode = null;
            this.prevNode = null;
        }

        @Override
        public boolean hasNext()
        {
            if(this.currNode == null)
            {
                return head != null;
            }
            return this.currNode.next != null;
        }

        @Override
        public Character next()
        {
            // THROW NMOSUCHELEMENT IF !HASNEXT()
            if(!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            // MAKE PREV EQUAL TO CURRENT
            this.prevNode = this.currNode;
            // IF CURRENT IS NULL, MAKE CURRENT EQUAL OT IT
            if(this.currNode == null)
            {
                this.currNode = head;
            }
            // IF CURRENT IS NOT NULL, MAKE IT EQUAL TO THE NEXT ELEMENT
            else
            {
                this.currNode = this.currNode.next;
            }
            // RETURN CURRENT DATA
            return this.currNode.data;
        }

        @Override
        public void remove()
        {
           // THROW ILLIGALSTATE IF CURRENT CURRENT EQUALS PREV
            if(this.currNode == this.prevNode)
            {
                throw new IllegalStateException();
            }
            // IF CURRENT IS EQUAL TO HEAD make head equal to next element
            if(this.currNode == head)
            {
                head = this.currNode.next;
            }
            // if current is not equal to head, make prev element equal to next element
            else
            {
                this.prevNode = this.currNode.next;
            }
            // make current equal to previous
            this.currNode = this.prevNode;
            // reduce size
            size--;
        }
    }

    /*
    private class LinkedListIterator implements Iterator<Character>
    {
        private Node currNode;
        private Node prevNode;

        public LinkedListIterator()
        {
            this.currNode = null;
            this.prevNode = null;
        }
        @Override
        public boolean hasNext() {
            if(this.currNode == null)
            {
                return head!=null;
            }
            return this.currNode.next != null;
        }

        @Override
        public Character next() {
            if(!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            this.prevNode = this.currNode;
            if(this.currNode == null)
            {
                this.currNode = head;
            }
            else
            {
                this.currNode = this.currNode.next;
            }
            return currNode.next.data;
        }
    }
    */
public static void main(String[] args)
{
    LinkedList ll = new LinkedList();

    ll.add('a');
    ll.add('d');
    ll.add('i');
    ll.add('l');
    ll.add(' ');
    ll.add('g');
    ll.add('a');
    ll.add('r');
    ll.add('a');
    ll.add('d');

    PrintStream c = System.out;
    c.println(ll.toString());
    c.println(ll.contains('b'));
    c.println(ll.indexOf('l'));
    ll.add(1, 'x');
    c.println(ll.toString());
    ll.removeFirst();
    ll.removeFirst();
    ll.removeFirst();
    ll.removeFirst();
    ll.remove(2);
    System.out.print(ll.remove2(1));
    c.println(ll.toString());

    c.println(ll.h(ll));
}
}
