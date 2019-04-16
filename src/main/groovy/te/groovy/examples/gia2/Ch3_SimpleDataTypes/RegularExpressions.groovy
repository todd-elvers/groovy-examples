package te.groovy.examples.gia2.Ch3_SimpleDataTypes

import java.util.regex.Matcher



//    QUALIFIERS 
//    Let o specify the number of occurances (?=once or more| *=zero or more| +=one or more)
 
//    Greedy    :    Xo     X{n}     X{n,}     X{n,m}
//    Reluctant :    Xo?    X{n}?    X{n,}?    X{n,m}?
//    Possessive:    Xo+    X{n}+    X{n,}+    X{n,m}?



// MATCHING W/ GROUPING
println "GROUPING"
String testString = """\
Today's date is 12/1/2013.
Alternatively it's 12/01/2013.
"""
Matcher matcher = (testString =~ /(.*?)(\d{1,2}\/\d{1,2}\/\d{2,4})(.*?)/)

printMatcherResults(matcher)





/**********************/
/* MATCHER ALGORITHMS */
/**********************/

// GREEDY ALGORITHM
// 1 - Consume entire string
// 2 - Attempt match, if successful return result
// 3 - Otherwise back off one letter at a time and retry match
//     until a match is found or string is exhausted
println "\nGREEDY ALGORITHM"
testString = "xfooxxxxxxfoo"
matcher = (testString =~ /.*foo/)
printMatcherResults(matcher)

// RELUCTANT ALGORITHM
// 1 - Consume nothing
// 2 - Attempt match, if successful store result
// 3 - Then consume one more letter of the string and retry match
//     until match is found or entire string is exhausted
println "\nRELUCTANT ALGORITHM"
testString = "xfooxxxxxxfoo"
matcher = (testString =~ /.*?foo/)
printMatcherResults(matcher)

// POSSESSIVE ALGORITHM
// Allows for faster failing & prevents regex engine from trying all permutations
// 1 - Consume entire string
// 2 - Attempt match, if successful return result
// 3 - Otherwise, return null
println "\nPOSSESSIVE ALGORITHM"
testString = "xfooxxxxxxfoo"
matcher = (testString =~ /.*+foo/)
printMatcherResults(matcher)


// LocalASTTransformationExample of difference between GREEDY and POSSESSIVE:
//    For string (quotes are part of string) "abc and regex "[^"]*"
//    POSSESSIVE would... 
//        1 - Match first " of the regex to first " of the string
//        2 - Match letters abc to [^"]*
//        3 - Fail to match the last " in the regex
//        4 - No match found, end of search.
//    GREEDY would...
//        1 - Match first " of the regex to first " of the string
//        2 - Match letters abc to [^"]*
//        3 - Fail to match " to the string, backtrack to [^"]* and give up match, resulting in ab
//        4 - Fail to match " to c, so [^"]* would backtrack, resulting in a
//        5 - Fail to match " to b, so [^"]* would backtrack, resulting in the empty string
//        6 - Fail to match " to a, no other backtracks exist
//        7 - No match found, end of search.

// Another quick example:
//    href="(.*)"  [GREEDY algorithm] matches till the last " in the string
//    href="(.*?)" [RELUCTANT algorithm] matches till the next " in the string



/**********************/
/******* EXTRAS *******/
/**********************/

// Match words that start and end with the same letter
println "\nBACKMATCHING"
testString = "that dog is crzazy"
matcher = (testString =~ /\b(\w)\w*\1\b/)
printMatcherResults(matcher)

// String eachMatch() closure
BOUNDARY = /\b/
WORDS_ENDING_IN_AIN = /$BOUNDARY(\w*ain)$BOUNDARY/

def found = ""
testString = "The rain in Spain stays mostly in the plains"
testString.eachMatch(WORDS_ENDING_IN_AIN) { match ->
    found += match[0] + " "
}
assert found == "rain Spain "


// Matcher .each() closure
found = ""
(testString =~ WORDS_ENDING_IN_AIN).each{ match ->
    found += match[0] + " "
}
assert found == "rain Spain "    


// String .replaceALl() method + closure
found = testString.replaceAll(WORDS_ENDING_IN_AIN) { match ->
    match[0]-"ain"+"___"
}
assert found == "The r___ in Sp___ stays mostly in the plains"


// Patterns in grep() and switch()
assert (~/..../).isCase('bear')

switch('bear'){
    case ~/..../:
        assert true
        break
    default:
        assert false
}

def beasts = ['bear', 'wolf', 'tiger', 'regex']
beasts.grep(~/..../) == ['bear', 'wolf']



/*******************************/
/*** Efficient Matcher Impl. ***/
/*******************************/
def twister = 'she sells sea shells at the sea shore of seychelles' 

// Match words that start and end with the same letter 
def regex = /\b(\w)\w*\1\b/ 
def many  = 100 * 1000 

/* Slower Implementation */
// Pattern-creating and pattern-matching inside a loop
start = System.currentTimeMillis() 
many.times {     
    twister =~ regex
}
def creationAndMatching = System.currentTimeMillis() - start

/* Faster Implementation */
// Do pattern-creation once before loop, and do pattern-matching inside loop
start = System.currentTimeMillis() 
pattern = ~regex
many.times {     
    pattern.matcher(twister)
}
def onlyMatching = System.currentTimeMillis() - start  

assert creationAndMatching > onlyMatching * 2
println "\nEFFICIENCY"
println "\tPattern-creation + Pattern-matching inside loop = $creationAndMatching ms"
println "\t\t    Only Pattern-matching inside loop = $onlyMatching ms"
 






/**************************/
/**** HELPER FUNCTIONS ****/
/**************************/
void printMatcherResults(Matcher matcher) {
    if(matcher) {
        matcher.each { match ->
            println "\tEntire matching string:\n\t\t${match}"
            if(patternContainsAnyParenthesis(matcher.parentPattern)){
                println "\tCapture groups:"
                for(captureGroup in match){
                    println "\t\t${captureGroup}"
                }
            }
        }
    } else {
        println "\tNo matches found."
    }
}

boolean patternContainsAnyParenthesis(pattern){
    pattern.toString().contains("(") || pattern.toString().contains(")")
}