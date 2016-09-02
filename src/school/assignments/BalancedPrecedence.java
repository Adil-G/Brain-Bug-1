package school.assignments;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by swaggerton on 29/11/15.
 */
public class BalancedPrecedence
{
    private static final String OPEN = "([{";
    private static final String CLOSE = ")]}";
    private static Stack<Character> precedenceOps;

    public static void main(String[] args) {
        PrintStream output = System.out;
        output.println("Enter expression:");
        Scanner input = new Scanner(System.in);
        String expression = input.nextLine();
        input.close();

        // Your code goes here.
        ;
        precedenceOps = new Stack<Character>();
        boolean overlaping = false;
        boolean Imbalanced = false;
        for (int index = 0; index < expression.length(); index++)
        {
            char current  = expression.charAt(index);
            System.out.println(current);
            if(current == '(' || current == '[' || current == '{')
            {
                //System.out.println("push--> " + current);
                precedenceOps.push(current);
            }
            else if(current == ')'&& !precedenceOps.isEmpty())
            {
                boolean isMatch =false;
                char other = precedenceOps.pop();
                //System.out.println("POPED: " + other)
                //System.out.println("pop <-- " + other + " == " + current);
                if(other != '(')
                {
                    System.out.println("Overlapping!");
                    overlaping = true;
                }
            }
            else if(current == ']'&& !precedenceOps.isEmpty())
            {
                boolean isMatch =false;
                char other = precedenceOps.pop();
                //System.out.println("POPED: " + other)
                //System.out.println("pop <-- " + other + " == " + current);
                if(other != '[')
                {
                    System.out.println("Overlapping!");
                    overlaping = true;
                }
            }
            else if(current == '}'&& !precedenceOps.isEmpty())
            {
                boolean isMatch =false;
                char other = precedenceOps.pop();
                //System.out.println("POPED: " + other)
                //System.out.println("pop <-- " + other + " == " + current);
                if(other != '{')
                {
                    System.out.println("Overlapping!");
                    overlaping = true;
                }
            }
            else if(precedenceOps.isEmpty())
            {
                System.out.println("Unballanced!");
                Imbalanced = true;
            }

        }
        if(!precedenceOps.isEmpty())
        {
            System.out.println("Unballanced!");
            Imbalanced = true;
        }
        System.out.println("___________________________________");
        if(Imbalanced)
        {
            System.out.println("Unballanced!");
        }
        else if(overlaping)
        {
            System.out.println("Overlapping!");
        }
        else
        {
            System.out.println("Balanced!");
        }
    }

}