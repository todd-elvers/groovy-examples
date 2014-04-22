package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations

import groovy.transform.Synchronized
import groovy.transform.WithReadLock
import groovy.transform.WithWriteLock
import groovy.util.logging.Log


/***********************/
/*** Synchronization ***/
/***********************/

// Method-level synchronizing is generally considered bad practice (see 257/778)
// Synchronization is a low level, primitive operation.

// @Synchronized wraps methods in synchronized blocks, without exposing it at the method-level
class Person6 {
    private final phoneNumbers = [:]

    @Synchronized
    def getPhoneNumber(key) {
        phoneNumbers[key]
    }

    @Synchronized
    def addPhoneNumber(key, value) {
        phoneNumbers[key] = value
    }
}

def p = new Person6()

// If you want to limit the scope of your synchronized block, then provide
// a name for the lock using the default annotation parameter and write the
// synchronized block yourself when needed:
@Log
class Person7 {
    private final phoneNumbers = [:]
    private final lock = new Object[0]

    @Synchronized('lock')
    def getPhoneNumber(key) {
        phoneNumbers[key]
    }

    def addPhoneNumber(key, value) {
        log.info("Adding phone number $value")
        synchronized (lock) {
            phoneNumbers[key] = value
        }
    }
}



/************************/
/*** Read/write locks ***/
/************************/

class Person8 {
    private final phoneNumbers = [:]

    @WithReadLock
    def getPhoneNumber(key) {
        phoneNumbers[key]
    }

    @WithWriteLock
    def addPhoneNumber(key, value) {
        phoneNumbers[key] = value
    }
}



/*********************/
/*** Clone-ability ***/
/*********************/

// Annotating a class with @AutoClone logically produces code similar to:
class Person10 implements Cloneable {
    String firstName, lastName
    Date birthday

    protected Person10(Person10 other) throws CloneNotSupportedException {
        firstName = other.firstName
        lastName = other.lastName
        birthday = other.birthday.clone()
    }

    Object clone() throws CloneNotSupportedException {
        new Person10(this)
    }
}



/*******************/
/*** Externalize ***/
/*******************/

// Annotating a class with @AutoExternalize automatically generates .readExternal(ObjectInput)
// and .writeExternal(ObjectOutput) similar to:
class Person11 implements Externalizable {
    String firstName, lastName
    Date birthday

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(firstName)
        out.writeObject(lastName)
        out.writeObject(birthday)
    }

    public void readExternal(ObjectInput oin) {
        firstName = oin.readObject()
        lastName = oin.readObject()
        birthday = oin.readObject()
    }
}