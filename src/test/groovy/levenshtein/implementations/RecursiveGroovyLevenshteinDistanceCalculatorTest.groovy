package levenshtein.implementations

import spock.lang.Specification
import spock.lang.Unroll

class RecursiveGroovyLevenshteinDistanceCalculatorTest extends Specification {

    RecursiveGroovyLevenshteinDistanceCalculator calculator = []

    @Unroll
    def "calculate(\"#string1\", \"#string2\") == #expectedLevDist"() {
        expect:
            expectedLevDist == calculator.calculate(string1, string2)

        where:
            string1         | string2       | expectedLevDist
            ""              | ""            | 0
            "1"             | "1"           | 0
            "12"            | "12"          | 0
            "123"           | "123"         | 0
            "1234"          | "1234"        | 0
            "12345"         | "12345"       | 0
            "password"      | "password"    | 0
            ""              | "1"           | 1
            ""              | "12"          | 2
            ""              | "123"         | 3
            ""              | "1234"        | 4
            ""              | "12345"       | 5
            ""              | "password"    | 8
            "1"             | ""            | 1
            "12"            | ""            | 2
            "123"           | ""            | 3
            "1234"          | ""            | 4
            "12345"         | ""            | 5
            "password"      | ""            | 8
            "password"      | "1password"   | 1
            "password"      | "p1assword"   | 1
            "password"      | "pa1ssword"   | 1
            "password"      | "pas1sword"   | 1
            "password"      | "pass1word"   | 1
            "password"      | "passw1ord"   | 1
            "password"      | "passwo1rd"   | 1
            "password"      | "passwor1d"   | 1
            "password"      | "password1"   | 1
            "password"      | "assword"     | 1
            "password"      | "pssword"     | 1
            "password"      | "pasword"     | 1
            "password"      | "pasword"     | 1
            "password"      | "passord"     | 1
            "password"      | "passwrd"     | 1
            "password"      | "passwod"     | 1
            "password"      | "passwor"     | 1
            "password"      | "Xassword"    | 1
            "password"      | "pXssword"    | 1
            "password"      | "paXsword"    | 1
            "password"      | "pasXword"    | 1
            "password"      | "passXord"    | 1
            "password"      | "passwXrd"    | 1
            "password"      | "passwoXd"    | 1
            "password"      | "passworX"    | 1
            "12345678"      | "23456781"    | 2
            "12345678"      | "34567812"    | 4
            "12345678"      | "45678123"    | 6
            "12345678"      | "56781234"    | 8
            "12345678"      | "67812345"    | 6
            "12345678"      | "78123456"    | 4
            "12345678"      | "81234567"    | 2
            "12"            | "21"          | 2
            "123"           | "321"         | 2
            "1234"          | "4321"        | 4
            "12345"         | "54321"       | 4
            "123456"        | "654321"      | 6
            "1234567"       | "7654321"     | 6
            "12345678"      | "87654321"    | 8
            "Mississippi"   | "ippississiM" | 6
            "eieio"         | "oieie"       | 2
            "brad+angelina" | "brangelina"  | 3
            "?e?uli?ka"     | "e?uli?ka"    | 1
    }
}