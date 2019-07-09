package te.groovy.examples.new_version_features.v2_5

import groovy.transform.AutoFinal

/*** @AutoFinal adds the `final` keyword to all arguments of a method/constructor/closure/etc. ***/ 

@AutoFinal
class Person {
    String firstName
    
    Person(String firstName) {
        this.firstName = firstName
    }
    
    void setFirstName(String firstName) {
        this.firstName = firstName
    } 
}

// The above class will become the following:
class PersonAfterAutoFinal {
    String firstName

    PersonAfterAutoFinal(final String firstName) {
        this.firstName = firstName
    }

    void setFirstName(final String firstName) {
        this.firstName = firstName
    }
}




