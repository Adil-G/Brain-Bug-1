package school.assignments;

import java.io.PrintStream;
import java.util.*;

/**
 * Created by swaggerton on 29/11/15.
 */
public class BinarySearchTree<E extends java.lang.Comparable<? super E>> {
    private Node<E> root;
    public BinarySearchTree()
    {
        this.root = null;
    }
    private static class Node<E>
    {
        private Node<E> left;
        private Node<E> right;
        private E data;
        public Node(E dat, Node<E> leftSub, Node<E> rightSub)
        {
            this.data = dat;
            this.right = rightSub;
            this.left = leftSub;
        }
    }
    public void add(E value)
    {
        root = addNode(root, value);
    }
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        infixPrint(root, sb);
        return sb.toString().trim();
    }

    // Prints the elements in infix order.
    private void infixPrint(Node<E> root, StringBuffer sb)
    {
        if (root != null)
        {
            infixPrint(root.left, sb);
            sb.append(root.data + " ");
            infixPrint(root.right, sb);
        }
    }
    private Node<E> addNode(Node<E> root, E value)
    {
        Node<E> result = null;
        if(root == null)
        {
            root = new Node(value, null, null);
            result = root;
        }
        else if(root.data.compareTo(value)>0)
        {
            root.left = addNode(root.left, value);
            result = root;
        }
        else
        {
            root.right = addNode(root.right, value);
            result = root;
        }
        return result;
    }
    public int size()
    {
        return size(root);
    }
    private E largestValue(Node<E> root)
    {
        if (root.right == null) // Base case, this node has largest.
        {
            return root.data;
        }
        else // Recursive case, keep going right.
        {
            return largestValue(root.right);
        }
    }
    private int size(Node<E> root)
    {
        if(root == null)
        {
            return 0;
        }
        return size(root.left) + 1 + size(root.right);
    }
    /*
               public E largestValue()
               {
                   return largestValue(root);
               }
               private E largestValue(Node<E> root)
               {
                   E result = null;
                   if (root.right == null) // Base case, this node has largest.
                   {
                       result = root.data;
                   }
                   else // Recursive case, keep going right.
                   {
                       result = largestValue(root.right);
                   }
                   return result;
               }

                  public int size()
                  {

                  }
                  private int size(Node n, int count)
                  {
                      // BASE CASE
                      if(n == null)
                      {
                          return 0;
                      }
                      int left = size(n.left, count);
                      int right = size(n.right, count);
                      return left + right;
                  }
                  */
    private Node<E> remove( Comparable x, Node<E> t )
    {
        if( t == null )
            return t;   // Item not found; do nothing
        if( x.compareTo( t.data ) < 0 )
            t.left = remove( x, t.left );
        else if( x.compareTo( t.data ) > 0 )
            t.right = remove( x, t.right );
        else if( t.left != null && t.right != null ) // Two children
        {
            t.data = findMin( t.right ).data;
            t.right = remove( t.data, t.right );
        }
        else
            t = ( t.left != null ) ? t.left : t.right;
        return t;
    }
    public void remove(E value)
    {
        root = removeNode(root, value);
    }

    // Solves 'remove' recursively.
    private Node<E> removeLargestNode(Node<E> root)
    {
        Node<E> result = null;
        if (root.right == null) // Case 1 or 2 (i.e., 0 or 1 child)
        {
            result = root.left; // null if Case 1, not null if Case 2
        }
        else
        {
            root.right = removeLargestNode(root.right);
            result = root;
        }
        return root;
    }
    private Node<E> removeLargestNode2(Node<E> root)
    {
        Node<E> result = null;
        if(root.right == null)
        {
            root  = root.left;
            result = root;
        }
        else
        {
            //recursive case go right
            root.right = removeLargestNode(root.right);
            result = root;
        }
        return result;
    }
    private Node<E> removeNode1(Node<E> root, E value)
    {
        Node<E> result = null;

        if(root != null && root.data.compareTo(value) == 0)
        {
            if(root.left == null)
            {
                result = root.right;
            }
            if(root.right == null)
            {
                result = root.left;
            }
            else
            {
                root.data = largestValue(root.left);
                root.left = removeLargestNode(root.left);
            }
        }
        else if(root.data.compareTo(value)>0)
        {
            // recursive case, go left
            root.left = removeNode(root.left, value);
            result = root;

        }
        else
        {
            // recursive case, go riht
            root.right = removeNode(root.right, value);
            result = root;
     }
        return result;
    }
    private Node<E> removeNode(Node<E> root, E value)
    {
        Node<E> result = null;
        if (root != null && root.data.compareTo(value) == 0)
        // Base case, remove this node.
        {
            if (root.left == null) // Case 1 or 2 (i.e., 0 or 1 child)
            {
                result = root.left; // null if Case 1, not null if Case 2
            }
            else if (root.right == null) // Case 2 (i.e., 1 child on left)
            {
                result = root.left;
            }
            else // Case 3 (i.e., 2 children)
            {
                root.data = largestValue(root.left);
                root.left = removeLargestNode(root.left);
            }
        }
        else if (root.data.compareTo(value) > 0) // Recursive case, go left.
        {
            root.left = removeNode(root.left, value);
            result = root;
        }
        else // Recursive case, go right.
        {
            root.right = removeNode(root.right, value);
            result = root;
        }
        return result;
    }
    private void enq(Node<E> root)
    {

        Queue<Node<E>> e  = new LinkedList<Node<E>>();
        while(!e.isEmpty())
        {
            Node<E> n = null;
            n = e.poll();
            if(n.left != null)
            {
                e.offer(n.left);
            }
            if(n.right != null)
            {
                e.offer(n.right);
            }
        }
    }
    private Node<E> removeLargestNode3(Node<E> root)
    {
        Node<E> result = null;
        if(root.right == null)
        {
            // base case get left
            result = root.left;
        }
        else
        {
            // recursive case, keep going right
            root.right = removeLargestNode3(root.right);

        }
        return result;
    }
    public Node<E> findMin(Node<E> root)
    {
        if(root == null)
        {
            return null;
        }
        if(root.left == null)
        {
            return root;
        }
        else
        {
            return findMin(root.left);
        }

    }
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        PrintStream output = System.out;
        BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();

        output.println("Enter a list of non-negative integers. Enter -1 to end.");
        for (int i = input.nextInt(); i != -1; i = input.nextInt())
        {
            bst.add(i);
        }

        output.println("\nIn sorted order:");
        output.println(bst.toString() + "\n");
        output.println("\nLargest:");
        output.println(bst.largestValue(bst.root));
        output.println("\nsize:");
        output.println(bst.size());
        bst.removeLargestNode3(bst.root);
        output.println(bst.toString() + "\n");
        //bst.largestValue(bst.root.left);

    }
}
