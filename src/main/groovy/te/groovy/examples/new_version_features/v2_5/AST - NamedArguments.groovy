package te.groovy.examples.new_version_features.v2_5

import groovy.transform.NamedDelegate
import groovy.transform.NamedParam
import groovy.transform.NamedVariant

import static groovy.test.GroovyAssert.shouldFail

// Base class
class Conference {
    String name, location
}

class ConferenceFactory {

    // First parameter's properties will be the named arguments we can use.
    // This AST adds a separate version of that method that accepts a map,
    // and uses the values in that map to construct an instance of what is
    // annotated with `@NamedDelegate` and then calls this method with it.
    @NamedVariant
    static Conference copy(@NamedDelegate Conference conf) {
        return new Conference(name: conf.name, location: conf.location)
    }

    // Same example as above but when we use this method below it will expose
    // the fact that there is actually a secondary method that's inserted that
    // accepts a Map and eventually calls this method
    @NamedVariant
    static Conference copy(boolean upper, @NamedDelegate Conference conf) {
        return upper
                ? new Conference(name: conf.name.toUpperCase(), location: conf.location.toUpperCase())
                : new Conference(name: conf.name, location: conf.location)
    }


    // Example of named argument support w/ validation using `@NamedParam`
    @NamedVariant
    static Conference make(@NamedParam(required = true) String name, @NamedParam String location) {
        return new Conference(name: name, location: location)
    }
}



// All properties of the type of the argument (Conference) can be used as named arguments.
def strangeLoop = ConferenceFactory.copy(name: 'StrangeLoop', location: 'St. Louis')

assert strangeLoop.name == 'StrangeLoop'
assert strangeLoop.location == 'St. Louis'



// Parameter upper in original method definition is not part of the named arguments.
def uberConf = ConferenceFactory.copy(name: 'UberConf', true, location: 'Colorado')

assert uberConf.name == 'UBERCONF'
assert uberConf.location == 'COLORADO'



// We hit a generated `make` method that parses & validates the input parameters
// and then calls the actual `make` method we created in the factory above
def awsReinvent = ConferenceFactory.make(name: 'AWS reInvent', location: 'Las Vegas')
assert awsReinvent.name == 'AWS reInvent'
assert awsReinvent.location == 'Las Vegas'

// If the validation in the generated `make` method fails then an exception is thrown.
def required = shouldFail(AssertionError) {
    ConferenceFactory.make(location: 'Copenhagen')
}

assert required.message.contains("Missing required named argument 'name'.")