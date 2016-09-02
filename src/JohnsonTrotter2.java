import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*************************************************************************
 *  Compilation:  javac Perm.java
 *  Execution:    java Permutations N k
 *
 *  Generate permutations by transposing adjacent elements using the
 *  Johnson-Trotter algorithm.
 *
 *  This program is a Java version based on the program SJT.c
 *  writen by Frank Ruskey.
 *
 *     http://theory.cs.uvic.ca/inf/perm/PermInfo.html
 *
 *  % java JohnsonTrotter 3
 *  012   (2 1)
 *  021   (1 0)
 *  201   (2 1)
 *  210   (0 1)
 *  120   (1 2)
 *  102   (0 1)
 *
 *************************************************************************/


public class JohnsonTrotter2 {
static List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
    public static void perm(int N) {
        int[] p   = new int[N];     // permutation
        int[] pi  = new int[N];     // inverse permutation
        int[] dir = new int[N];     // direction = +1 or -1
        for (int i = 0; i < N; i++) {
            dir[i] = -1;
            p[i]  = i;
            pi[i] = i;
        }
        perm(0, p, pi, dir);
        System.out.printf("   (0 1)\n");
    }

    public static void perm(int n, int[] p, int[] pi, int[] dir) {
        String permIt = "";

        // base case - print out permutation
        if (n >= p.length) {
            for (int i = 0; i < p.length; i++) {
                System.out.print(p[i]);
                permIt+=p[i]+",";
            }
            ArrayList<String> perms = new ArrayList<String>(Arrays.asList(permIt.split(",")));
            list.add(perms);
            return;
        }

        perm(n+1, p, pi, dir);
        for (int i = 0; i <= n-1; i++) {

            // swap 
            System.out.printf("   (%d %d)\n", pi[n], pi[n] + dir[n]);
            int z = p[pi[n] + dir[n]];
            p[pi[n]] = z;
            p[pi[n] + dir[n]] = n;
            pi[z] = pi[n];
            pi[n] = pi[n] + dir[n];

            perm(n+1, p, pi, dir);
        }
        dir[n] = -dir[n];
    }


    public static void main(String[] args) {
        int N = 3;
        perm(N);
        System.out.println(list);
    }
}
