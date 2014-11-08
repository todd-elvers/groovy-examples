package newInGroovy2_3

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

    String drive() {
        // 'this' represents the implementing instance, not the trait
        String classCallingMethod = determineClassName(this.class.toString())
        "This ${classCallingMethod} can drive and has ${numberOfWheels()} wheels."
    }

    // Traits only support public and private methods
    // This method will not be available to the class implementing this trait
    private determineClassName(String classNameWithPackage){
        classNameWithPackage.tokenize('.')[-1]
    }
}

class Car implements DrivingAbility {
    int numberOfWheels(){
        return 4
    }
}

def car = new Car()
println car.drive()

