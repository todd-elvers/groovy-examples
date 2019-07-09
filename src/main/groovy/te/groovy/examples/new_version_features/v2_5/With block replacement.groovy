package te.groovy.examples.new_version_features.v2_5

class Book {
    String title
    String author
}

// The .with() method executes within the context of the object it was called on and
// has the unfortunate behavior of returning the last expression it evaluated. This
// forces us to do one of two things: either separate the instantiation and configuration
// of our object (which was the whole goal of the .with() call in the first place) into
// separate expressions or keeping them both on the same line but make sure the last line of
// the .with() call is `return it` or just `it`.  Here is an example of both syntax styles:
Book b1 = new Book()
b1.with {
    title = 'b1_title'
    author = 'b1_author'
}

Book b2 = new Book().with {
    title = 'b2_title'
    author = 'b2_author'
    return it
}

assert b1.title == 'b1_title'
assert b1.author == 'b1_author'
assert b2.title == 'b2_title'
assert b2.author == 'b2_author'



// Groovy 2.5 added the .tap() method, which in spirit is the same as the .with() method, except
// it always returns an implicit `it` for us, so we can avoid the implementation detail of having
// to be concerned with what the last expression in the method is.

Book b3 = new Book().tap {
    title = 'b3_title'
    author = 'b3_author'
}

assert b3.title == 'b3_title'
assert b3.author == 'b3_author'