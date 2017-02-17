package te.groovy.examples.gia2.Ch4_CollectionDatatypes

import static Sorter.*

// Note: This implementation does NOT ignore duplicate entries in 'list'
class Sorter {
    static def quickSort(list) {
        if(list.size() < 2) return list
        def pivot  = list[list.size().intdiv(2)]
        def left   = list.findAll{ it < pivot }
        def middle = list.findAll{ it == pivot}
        def right  = list.findAll{ it > pivot }
        return (quickSort(left) + middle + quickSort(right))
    }
}
assert quickSort([2,1])         == [1,2]
assert quickSort([3,1,2,2])     == [1,2,2,3]
assert quickSort('Todd Elvers') == ' ETddelorsv'.toList()