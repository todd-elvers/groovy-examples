package te.groovy.examples.new_version_features.v2_3

/*
    Trait features:
        - Composition of behaviors
        - Runtime implementation of interfaces
        - Behavior overriding
        - Compatibility with static type checking/compilation

 */

trait DrivingAbility {
    // Abstract method must be implemented in class implementing this trait
    abstract int numberOfWheels()

    // Traits only support public and private methods
    // Private methods will not be visible to the implementing class
    String drive() {
        // 'this' represents the implementing instance, not the trait
        "This ${this.class.simpleName} can drive and has ${numberOfWheels()} wheels."
    }
}

class Car implements DrivingAbility {
    int numberOfWheels(){
        return 4
    }
}

def car = new Car()
assert car.drive() == "This Car can drive and has 4 wheels."

