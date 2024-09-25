public class Person implements Cloneable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Override the clone() method
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Create a shallow copy using super.clone()
        Person clone = (Person) super.clone();

        // If necessary, modify the cloned object's fields for deep copying
        // For example, if the object contains mutable references, you might need
        // to clone those references as well to avoid sharing them with the original

        return clone;
    }

    // Getters and setters
    // ...
}

// Usage:
Person originalPerson = new Person("Alice", 30);
Person clonedPerson = (Person) originalPerson.clone();

System.out.println("Original Person: " + originalPerson);
System.out.println("Cloned Person: " + clonedPerson);
