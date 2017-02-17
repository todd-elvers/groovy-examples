package te.groovy.examples.gia2.Ch3_SimpleDataTypes

class Money {
    private int amount
    private String currency

    // Overrides the '.equals()' and '==' operators    
    boolean equals (Object other) {
        if (!other)                     return false
        if (! other instanceof Money)   return false
        if (currency != other.currency) return false
        if (amount   != other.amount)   return false
        return true
    }
   
    // Overrides the '.plus()' and '+' operators
    Money plus (Money other) {
        if (!other) return null
        if (other.currency != currency) {
            throw new IllegalArgumentException("cannot add $other.currency to $currency")
        }
        
        return new Money(amount: amount + other.amount, currency: currency)
    }
    
    int hashCode() {
        return amount.hashCode() + currency.hashCode()        
    }
}
    
def buck = new Money(amount: 1, currency: 'USD')

assert buck 
assert buck.equals(new Money(amount:1, currency: 'USD'))
assert buck             == new Money(amount: 1, currency: 'USD')   
assert buck + buck      == new Money(amount: 2, currency: 'USD')
assert buck.plus(buck)  == new Money(amount: 2, currency: 'USD')