package school.Test4.LinkedListTests;

import sun.reflect.generics.tree.Tree;

import java.util.Stack;

/**
 * Created by swaggerton on 26/11/15.
 */
public class BinaryTree {
    public static class TreeNode
    {
        int data;
        TreeNode left;
        TreeNode right;
        TreeNode(int data)
        {
            this.data=data;
        }
    }
    public void postorderIter(TreeNode root)
    {
        if(root == null) return;

        Stack<TreeNode> s = new Stack<TreeNode>();
        TreeNode current = root;

        while(true)
        {
            if(current != null)
            {
                if(current.right != null)
                {
                    s.push(current.right);
                }
                s.push(current);
                current = current.left;
                continue;
            }
            // USUALLY FALSE
            if(s.isEmpty())
            {
                return;
            }
            // ALWAYS
            current = s.pop();

            if(current.right != null
                    && !s.isEmpty()
                    && current.right == s.peek())
            {
                s.pop();
                s.push(current);
                current = current.right;
            }
            else
            {
                System.out.println(current.data);
                current = null;
            }

        }
    }
    public void preOrder(TreeNode root)
    {
        if(root!=null) {
            System.out.println(root.data);
            preOrder(root.left);
            preOrder(root.right);
        }
    }
    public void preOrderItt(TreeNode root)
    {
        if(root == null)
        {
            return;
        }

        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while(!stack.empty())
        {
            TreeNode n = stack.pop();
            System.out.println(n.data);

            if(n.right !=null)
            {
                stack.push(n.right);
            }
            if(n.left != null)
            {
                stack.push(n.left);
            }
        }
    }
    public static void main(String[] args)
    {
        BinaryTree bi=new BinaryTree();
        // Creating a binary tree
        TreeNode rootNode=createBinaryTree();
        System.out.println("Using Recursive solution:");

        bi.preOrder(rootNode);

        System.out.println();
        System.out.println("-------------------------");
        System.out.println("Using Iterative solution:");

        bi.postorderIter(rootNode);
    }
    // RECURSIVE

    public static TreeNode createBinaryTree()
    {

        TreeNode rootNode =new TreeNode(40);
        TreeNode node20=new TreeNode(20);
        TreeNode node10=new TreeNode(10);
        TreeNode node30=new TreeNode(30);
        TreeNode node60=new TreeNode(60);
        TreeNode node50=new TreeNode(50);
        TreeNode node70=new TreeNode(70);

        rootNode.left=node20;
        rootNode.right=node60;

        node20.left=node10;
        node20.right=node30;

        node60.left=node50;
        node60.right=node70;

        return rootNode;
    }

    }
