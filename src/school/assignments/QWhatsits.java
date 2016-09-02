package school.assignments;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by swaggerton on 29/11/15.
 */
public class QWhatsits {

    public static void method()
    {
        PriorityQueue<String> q = new PriorityQueue<String>();

        q.offer("first");
        q.offer("second");
        q.offer("third");

        System.out.printf("%s", q);
        System.out.println();

        System.out.printf("%s ", q.peek());
        System.out.println();

        q.poll();
        System.out.printf("%s ", q);
    }
    public static void main(String[] args)
    {
        method();
    }
}
