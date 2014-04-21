package groovyInAction.Ch4_CollectionDatatypes
/************/
/** RANGES **/
/************/

// Use with switch() and grep()
def age = 50
switch(age) {
    case  0..20: assert false; break
    case 21..40: assert false; break
    case 41..90: assert true; break
    default: assert false
}

def ages = [20,36,42,56]
assert ages.grep(30..50) == [36,42]


// Any datatype can be a range if it satisfies the following:
//    The type implements 'next' and 'previous' (ie ++ and --)
//    The type implements java.lang.Comparable (ie implements .compareTo())
class Weekday implements Comparable {
    static final DAYS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    private int index
    
    Weekday(String day){
        index = DAYS.indexOf(day)
    }
    
    Weekday next(){
        new Weekday(DAYS[(index+1) % DAYS.size()])
    }
    Weekday previous(){
        new Weekday(DAYS[index-1])
    }
    
    int compareTo(Object someWeekday){
        this.index <=> someWeekday.index
    }
    
    String toString(){
        DAYS[index]
    }
}

def mon = new Weekday('Mon')
def fri = new Weekday('Fri')

def weekdays = ""
for(weekday in mon..fri){
    weekdays += weekday.toString() + " "
}
assert weekdays == "Mon Tue Wed Thu Fri "