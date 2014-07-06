package groovyInAction.Ch5_Closures

/***************************************/
/*** Closures: this, delegate, owner ***/
/***************************************/
class Mother {
    int fieldVariable = 1
    
    int method() {
        return 2
    }
    
    Closure birth(param) {
        def localVariable = 3
        def closure = { caller ->
            [fieldVariable, 
            method(), 
            localVariable, 
            param, 
            caller,       
            this,         // refers to the enclosing class where a Closure is defined 
            owner,        // the enclosing object ('this' or a surrounding Closure)
            delegate]     // by default, same as owner, but changeable sometimes    
        }
        return closure
    }    
}

Mother mom = new Mother()
def closure = mom.birth(4)
def context = closure.call(this)

assert context[0..3] == [1,2,3,4]
assert context[-4].class.name.contains("(Ch 5) - Closures")
assert context[-3] instanceof Mother
assert context[-2] instanceof Mother
assert context[-1] instanceof Mother 

def firstClosure = mom.birth(4)
def secondClosure = mom.birth(4)
assert !firstClosure.is(secondClosure)



// Another example
class Class1 {

    def closure = {
        println "    this: ${this.class.name}"       // Will print 'Class1'
        println "delegate: ${delegate.class.name}"   // Will print 'Class1'
    
        def nestedClosure = {        
            println "   owner: ${owner.class.name}"  // Will print 'Class1$_closure1'
        }
            
        nestedClosure()
    }
    
}

closure = new Class1().closure
closure.delegate = this                    // Overrides delegate to be this filename
closure()