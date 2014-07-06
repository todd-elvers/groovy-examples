package groovyInAction.Ch5_Closures


// Methods and Closures differences:
//    - Closures are objects and thus created at runtime
//    - Methods are created at class-generation time
//    - N instances of a given closure may exist at any point
//    - Only 1 instance of a method may exist at any point


/**************************/
/*** CLOSURE PARAMETERS ***/
/**************************/

// Reacting to closure parameter count or type
class ClosureInfoPrinter{
    def countParameters(Closure closureToCall) {
        closureToCall.getParameterTypes().size()
    }
    def getParameterTypes(Closure closureToCall) {
        closureToCall.getParameterTypes()
    }
}
def cip = new ClosureInfoPrinter()
assert cip.countParameters { one, two -> } == 2
assert cip.getParameterTypes { String one, two -> } == [String.class, Object.class]



/*****************************/
/*** MULTIMETHODS CLOSURES ***/
/*****************************/
class MultiMethodExample {
    String mysteryMethod(Object obj) {
        "Object"
    }

    int mysteryMethod(String string) {
        string.length()
    }

    int mysteryMethod(List list) {
        list.size()
    }

    int mysteryMethod(int x, int y) {
        x+y
    }
}

MultiMethodExample multiMethodExample = new MultiMethodExample()
Closure multi = multiMethodExample.&mysteryMethod

Object object = new Object()
Object stringObj = 'string arg'
Object listObj = ['list', 'of', 'values']
Object intObj1 = 6
Object intObj2 = 8

// Q: How does groovy know which method to call since they're all of type 'Object'?
// A: These objects are all of the same static type, but differ in their dynamic type.
//    Since groovy dispatches on the dynamic type.
assert multi(object)           == "Object"
assert multi(stringObj)        == 10
assert multi(listObj)          == 3
assert multi(intObj1, intObj2) == 14


// Selective method overriding via MultiMethods
class Equalizer {
    boolean equals(Equalizer equalizer) {
        true
    }
}

Object equalizer = new Equalizer()
Object       obj = new Object()

assert  new Equalizer().equals(equalizer)
assert !new Equalizer().equals(obj)