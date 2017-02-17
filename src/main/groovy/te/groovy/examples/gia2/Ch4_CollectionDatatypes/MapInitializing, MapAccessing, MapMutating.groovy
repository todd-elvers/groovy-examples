package te.groovy.examples.gia2.Ch4_CollectionDatatypes

/******************************/
/***** MAP INITIALIZATION *****/
/******************************/

// When declaring a Map with String keys, the quotation
// marks are optional in declaration.
//
// NOTE: When you want to force a variable to be evaluated
// instead of using it as a key, put parenthesis around it 
def x = 'letterX'
assert ['x':1] == [x:1]
assert ['letterX':2] == [(x):2]



/*************************/
/***** MAP ACCESSING *****/
/*************************/

def map = [apple:1, 'a.b':2]

// Retrieving existing elements
assert map['apple']     == 1
assert map.get('apple') == 1
assert map.apple        == 1
assert map.'a.b'        == 2

// Attempting to retrieve non-existent elements
assert map['grape']     == null
assert map.grape        == null
assert map.get('grape') == null

// Retrieving element with a default value
// NOTE: This sets adds grape:0 and potato:1 to map
assert map.get('grape', 0)  == 0
assert map.get('potato', 1) == 1

// Assigning new values
map.grape = 10
assert map.grape == 10

map.each { key, value ->
    assert key instanceof String
    assert value instanceof Integer
}

map.each { entry ->
    assert entry.key instanceof String
    assert entry.value instanceof Integer
}



/************************/
/***** MAP MUTATING *****/
/************************/

map = [a:1, b:2, c:3]
assert map.subMap(['a','b']) == [a:1, b:2]
List addTo = []

// .collect() == .each{} except results are returned as List
def doubled = map.collect { entry -> entry.value *= 2 }

//.collect(Collection) == .each{} except it pushes results onto Collection
map.collect(addTo) { entry -> entry.value *= 2 }

assert doubled instanceof List
assert addTo instanceof List
assert doubled.every { it % 2 == 0 }
assert addTo.every { it % 2 == 0 }
    
