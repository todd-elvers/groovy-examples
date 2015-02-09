package levenshtein.implementations

import groovy.transform.CompileStatic
import groovy.transform.Memoized

@CompileStatic
class RecursiveGroovyLevenshteinDistanceCalculator {

    /**
     * Recursive implementation of levenshtein distance that is memoized to prevent re-calculating
     * any given levenshtein distance more than once.
     *
     * (Implementation taken from: http://en.wikipedia.org/wiki/Levenshtein_distance)
     */
    int calculate(String string1, String string2) {
        calculateLevenshteinDistance(string1, string1.length(), string2, string2.length())
    }

    @Memoized
    private int calculateLevenshteinDistance(String a, int i, String b, int j) {
        // If either string is empty, the levenshtein distance is the length of the other string
        if (Math.min(i, j) == 0) return Math.max(i, j)

        /**
         * Return minimum levenshtein distance achieved from:
         *  - deleting one character from a
         *  - deleting one character from b
         *  - deleting one character from both a & b
         */
        minimum(
                calculateLevenshteinDistance(a, i - 1,  b,  j    ) + 1,
                calculateLevenshteinDistance(a,     i,  b,  j - 1) + 1,
                calculateLevenshteinDistance(a, i - 1,  b,  j - 1) + indicatorFunction(a,i,b,j)
        )
    }

    /**
     * Returns 0 iff the last character of a is the same as
     * the last character of b, 1 otherwise.
     */
    private static int indicatorFunction(String a, int i, String b, int j) {
        (a[i - 1] == b[j - 1]) ? 0 : 1
    }

    private static int minimum(int a, int b, int c) {
        Math.min(Math.min(a, b), c)
    }
}
