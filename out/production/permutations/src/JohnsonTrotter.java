/**
 * Created by adil on 05/07/15.
 */

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


public class JohnsonTrotter {

    static public void main(String[] argv)
    {
        List<List<Integer>> lst = new ArrayList<List<Integer>>();

        lst.add(Arrays.asList(1));
        lst.add(Arrays.asList(2));
        lst.add(Arrays.asList(1));

        List<List<Integer>> result = null;

        result = cartesian(lst);

        for (List<Integer> r : result) {
            for (Integer i : r) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    static public List<List<Integer>> cartesian(List<List<Integer>> list)
    {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        int numSets = list.size();
        Integer[] tmpResult = new Integer[numSets];

        cartesian(list, 0, tmpResult, result);

        return result;
    }

    static public void cartesian(List<List<Integer>> list, int n, Integer[] tmpResult, List<List<Integer>> result)
    {
        if (n == list.size()) {
            result.add(new ArrayList<Integer>(Arrays.asList(tmpResult)));
            return;
        }

        for (Integer i : list.get(n)) {
            tmpResult[n] = i;
            cartesian(list, n + 1, tmpResult, result);
        }
    }


}

