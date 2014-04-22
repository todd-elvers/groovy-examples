package groovyInAction.Ch8_DynamicProgramming

Object.metaClass.methodMissing = { String name, Object args ->
    assert name.startsWith('findBy')
    assert args.size() == 1

    // Cache method for future calls
    println("[MetaClass] Caching method '${name}' for future calls.")
    Object.metaClass."$name" = { value ->
        delegate.find { it[name.toLowerCase()-'findby'] == value }
    }

    // Invoke the method
    delegate."$name"(args[0])
}

def data = [
        [name: 'moon',      au: '0.0025'],
        [name: 'sun',       au: 1],
        [name: 'neptune',   au: 30]
]

assert data.findByName('moon')      // Non-cached
assert data.findByName('sun')       // Cached
assert data.findByAu(1)             // Non-cached
assert data.findByAu(30)            // Cached