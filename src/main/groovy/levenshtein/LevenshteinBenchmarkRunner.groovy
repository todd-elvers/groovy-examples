package levenshtein

import groovyx.gbench.BenchmarkBuilder
import levenshtein.implementations.DamerauJavaLevenshteinCalculator
import levenshtein.implementations.IterativeJavaLevenshteinDistanceCalculator
import levenshtein.implementations.RecursiveGroovyLevenshteinDistanceCalculator
import levenshtein.implementations.RecursiveJavaLevenshteinDistanceCalculator

class LevenshteinBenchmarkRunner {

    static void main(String[] args) {

        def recGroovyCalculator = new RecursiveGroovyLevenshteinDistanceCalculator()
        def recJavaCalculator = new RecursiveJavaLevenshteinDistanceCalculator()
        def iterJavaCalculator = new IterativeJavaLevenshteinDistanceCalculator()
        def damerauCalculator = new DamerauJavaLevenshteinCalculator()

        String string1 = "SpaghettiTastesGreatEvenLate"
        String string2 = "JavaDoesTasteGreatButIt'sJustNotGroovyEnough"
        
        def benchmarkResult = new BenchmarkBuilder().run(verbose: true) {
            'Groovy: recursive Levenshtein algorithm using @Memoized' {
                recGroovyCalculator.calculate(string1, string2)
            }
            'Java: recursive Levenshtein algorithm' {
                recJavaCalculator.calculate(string1, string2)
            }
            'Java: iterative Levenshtein algorithm' {
                iterJavaCalculator.calculate(string1, string2)
            }
            'Java: Damerau Levenshtein algorithm' {
                // Time & space complexity: O(n*m)
                damerauCalculator.calculate(string1, string2)
            }
        }

        benchmarkResult.prettyPrint()
    }

}
