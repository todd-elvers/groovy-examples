package groovyInAction.Ch7_DynamicObjectOrientation
/*******************************************/
/*** REFERENCING FIELD-VARIABLES BY NAME ***/
/*******************************************/
class Counter {
    int count
}

Counter counter = new Counter()
counter.count = 1
assert counter.count == 1

def fieldName = 'count'
counter[fieldName] = 2            // To override: void set(String name, Object value);
assert counter[fieldName] == 2    // To override: Object get(String name);



/*******************************************/
/*** DEFAULT VALUES IN METHOD PARAMETERS ***/
/*******************************************/
class Summer {
    def sumWithDefaults(a, b, c=0) {
        return a + b + c
    }
}

Summer summer = new Summer()
assert 2 == summer.sumWithDefaults(1,1) 
assert 3 == summer.sumWithDefaults(1,1,1)



/****************************/
/*** CONSTRUCTOR COERCION ***/
/****************************/
class VendorProduct {
    String vendorName, product
    
    VendorProduct(vendorName, product) {
        this.vendorName = vendorName
        this.product    = product
    }
}
 
def first = new VendorProduct('H&K', 'G36c')    // Explicit constructor coercion
def second = ['H&K', 'G36c'] as VendorProduct   // Enforced constructor coercion
VendorProduct third = ['H&K', 'G36c']           // Implicit constructor coercion