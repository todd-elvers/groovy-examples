package te.groovy.examples.gia2.Ch8_DynamicProgramming

/**
 * Intercept, Cache & Invoke
 *
 * This is an efficient paradigm for adding dynamic functionality to your
 * code without paying as much of a performance price for it.
 *
 * The paradigm is simple:
 *      When a method is called that does not exist on an object:
 *          1.) Create the desired method
 *          2.) Add the method to the object (so it will be available in the future)
 *          3.) Invoke the method
 */


// Intercept the call to a method that does not exist on 'Object'
Object.metaClass.methodMissing = { String name, Object args ->
    assert name.startsWith('findBy')
    assert args.size() == 1

    // Cache method for future calls
    println("[MetaClass] Caching method '${name}' for future calls.")
    Object.metaClass."$name" = { value ->
        String propertyName = name.toLowerCase()-'findby'
        delegate.find { it[propertyName] == value }
    }

    // Invoke the method
    delegate."$name"(args[0])
}

Object data = [
        [name: 'moon',      au: '0.0025'],
        [name: 'sun',       au: 1],
        [name: 'neptune',   au: 30]
]

assert data.findByName('moon')      // Not-cached
assert data.findByName('sun')       // Cached
assert data.findByAu(1)             // Not-cached
assert data.findByAu(30)            // Cached