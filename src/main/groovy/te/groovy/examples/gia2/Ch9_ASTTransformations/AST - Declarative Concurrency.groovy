package te.groovy.examples.gia2.Ch9_ASTTransformations

import groovy.transform.AutoClone
import groovy.transform.Synchronized
import groovy.transform.WithReadLock
import groovy.transform.WithWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.concurrent.locks.ReadWriteLock

import static groovy.transform.AutoCloneStyle.SIMPLE


/***********************/
/*** Synchronization ***/
/***********************/

// Method-level synchronizing is generally considered bad practice (see 257/778)
// Synchronization is a low level, primitive operation.

// @Synchronized wraps methods in synchronized blocks, without exposing it at the method-level
class SynchronizedPhoneBook {
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


// If you want to limit the scope of your synchronized block, then provide
// a name for the lock using the default annotation parameter and write the
// synchronized block yourself when needed:
class SynchronizedPhoneBookWithLock {
    private final phoneNumbers = [:]
    private final lock = new Object[0]

    @Synchronized('lock')
    def getPhoneNumber(key) {
        phoneNumbers[key]
    }

    def addPhoneNumber(key, value) {
        synchronized (lock) {
            phoneNumbers[key] = value
        }
    }
}



/************************/
/*** Read/write locks ***/
/************************/

class ReadWriteLockingPhoneBook {
    private final phoneNumbers = [:]

    @WithReadLock
    String getPhoneNumber(key) {
        phoneNumbers[key]
    }

    @WithWriteLock
    void addPhoneNumber(key, value) {
        phoneNumbers[key] = value
    }
}

// The above is equivalent to the below

class ReadWriteLockingPhoneBookEquivalent {

    private final ReadWriteLock $reentrantlock = new ReentrantReadWriteLock()
    private final phoneNumbers = [:]

    String getPhoneNumber(String key) throws Exception {
        $reentrantlock.readLock().lock()
        try {
            return phoneNumbers[key]
        } finally {
            $reentrantlock.readLock().unlock()
        }
    }

    void addPhoneNumber(key, value) throws Exception {
        $reentrantlock.writeLock().lock()
        try {
            phoneNumbers[key] = value
        } finally {
            $reentrantlock.writeLock().unlock()
        }
    }
}




/*********************/
/*** Clone-ability ***/
/*********************/

@AutoClone(style = SIMPLE)
class CloneablePerson {
    String firstName, lastName
    Date birthday
}

// Annotating a class with @AutoClone(style = SIMPLE) logically produces code similar to:
class CloneablePersonEquivalent implements Cloneable {
    String firstName, lastName
    Date birthday

    protected ClonablePersonEquivalent(CloneablePersonEquivalent other) throws CloneNotSupportedException {
        firstName = other.firstName
        lastName = other.lastName
        birthday = (Date) other.birthday.clone()
    }

    Object clone() throws CloneNotSupportedException {
        new CloneablePersonEquivalent(this)
    }
}



/*******************/
/*** Externalize ***/
/*******************/

// Annotating a class with @AutoExternalize automatically generates .readExternal(ObjectInput)
// and .writeExternal(ObjectOutput) similar to:
class ExternalizedPerson implements Externalizable {
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
        birthday = oin.readObject() as Date
    }
}

println("")