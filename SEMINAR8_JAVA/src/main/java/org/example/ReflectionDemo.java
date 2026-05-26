package org.example;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Demo annotation that can be read at runtime
@Retention(RetentionPolicy.RUNTIME)
@interface Info {
    String author();
    String version();
}

@Info(author = "Demo Author", version = "1.0")
class Person {

    private String name;
    private int age;

    public Person() {
        this.name = "Unknown";
        this.age = 0;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void sayHello() {
        System.out.println("Hello, my name is " + name + ".");
    }

    public String greet(String otherPerson) {
        return "Hello " + otherPerson + ", I am " + name + ".";
    }

    private void secretMethod() {
        System.out.println("This is a private method.");
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
public class ReflectionDemo {

    public static void main(String[] args) {
        try {
            // Demo 1: Get Class information at runtime
            Class<?> personClass = Person.class;

            System.out.println("Class name:");
            System.out.println(personClass.getName());
            System.out.println();

            // Demo 2: Read annotation data
            if (personClass.isAnnotationPresent(Info.class)) {
                Info info = personClass.getAnnotation(Info.class);

                System.out.println("Annotation data:");
                System.out.println("Author: " + info.author());
                System.out.println("Version: " + info.version());
                System.out.println();
            }

            // Demo 3: List declared fields
            System.out.println("Declared fields:");
            Field[] fields = personClass.getDeclaredFields();

            for (Field field : fields) {
                System.out.println(field.getType().getSimpleName() + " " + field.getName());
            }

            System.out.println();

            // Demo 4: List constructors
            System.out.println("Constructors:");
            Constructor<?>[] constructors = personClass.getDeclaredConstructors();

            for (Constructor<?> constructor : constructors) {
                System.out.println(constructor);
            }

            System.out.println();

            // Demo 5: List declared methods
            System.out.println("Declared methods:");
            Method[] methods = personClass.getDeclaredMethods();

            for (Method method : methods) {
                System.out.println(method.getReturnType().getSimpleName() + " " + method.getName());
            }

            System.out.println();

            // Demo 6: Create an object dynamically using a constructor
            Constructor<?> constructor = personClass.getConstructor(String.class, int.class);
            Object personObject = constructor.newInstance("Alice", 30);
            System.out.println("Created object:");
            System.out.println(personObject);
            System.out.println();

            // Demo 7: Call a public method dynamically
            Method sayHelloMethod = personClass.getMethod("sayHello");
            sayHelloMethod.invoke(personObject);

            System.out.println();

            // Demo 8: Call a public method with an argument and return value
            Method greetMethod = personClass.getMethod("greet", String.class);
            Object greeting = greetMethod.invoke(personObject, "Bob");

            System.out.println("Result from greet method:");
            System.out.println(greeting);
            System.out.println();

            // Demo 9: Access and modify a private field
            Field nameField = personClass.getDeclaredField("name");

            // Allows access to a private field
            nameField.setAccessible(true);

            System.out.println("Original private name field:");
            System.out.println(nameField.get(personObject));

            nameField.set(personObject, "Charlie");

            System.out.println("Modified object:");
            System.out.println(personObject);
            System.out.println();

            // Demo 10: Call a private method
            Method secretMethod = personClass.getDeclaredMethod("secretMethod");

            // Allows access to a private method
            secretMethod.setAccessible(true);

            System.out.println("Calling private method:");
            secretMethod.invoke(personObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}