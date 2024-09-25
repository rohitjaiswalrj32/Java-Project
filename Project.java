
package Array;

public class Project { 

    public static void main(String[] args) throws CloneNotSupportedException {
        Person originalPerson = new Person("Alice", 30);
        Person clonedPerson = (Person) originalPerson.clone();

        System.out.println("Original Person: " + originalPerson);
        System.out.println("Cloned Person: " + clonedPerson);
    }
}

class Person implements Cloneable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        
        Person clone = (Person) super.clone();

        

        return clone;
    }
}
