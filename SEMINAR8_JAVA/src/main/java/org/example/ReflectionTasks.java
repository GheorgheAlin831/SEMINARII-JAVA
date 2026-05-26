package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

// Demo class
class Student {

    private String name;
    private int age;

    public Student() {
        this.name = "Unknown";
        this.age = 0;
    }

    public Student(String name) {
        this.name = name;
        this.age = 18;
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void sayHello() {
        System.out.println("Hello, my name is " + name + ".");
    }

    private void secretMethod() {
        System.out.println("This is a private method.");
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + "}";
    }
}

public class ReflectionTasks {

    public static void main(String[] args) {

        try {

            // Demo 1: Print class information

            Class<?> studentClass = Student.class;

            System.out.println("Class information:");
            System.out.println("Class name: " + studentClass.getName());
            System.out.println("Package name: " + studentClass.getPackageName());
            System.out.println("Superclass: " + studentClass.getSuperclass().getName());

            System.out.println("Implemented interfaces:");

            Class<?>[] interfaces = studentClass.getInterfaces();

            if (interfaces.length == 0) {
                System.out.println("No interfaces implemented.");
            } else {

                for (Class<?> inter : interfaces) {
                    System.out.println(inter.getName());
                }
            }

            System.out.println();

            // Demo 2: List declared fields

            System.out.println("Declared fields:");

            Field[] fields = studentClass.getDeclaredFields();

            for (Field field : fields) {

                System.out.println(
                        Modifier.toString(field.getModifiers()) + " "
                                + field.getType().getSimpleName() + " "
                                + field.getName()
                );
            }

            System.out.println();

            // Demo 3: List declared methods

            System.out.println("Declared methods:");

            Method[] methods = studentClass.getDeclaredMethods();

            for (Method method : methods) {

                System.out.print(
                        Modifier.toString(method.getModifiers()) + " "
                                + method.getReturnType().getSimpleName() + " "
                                + method.getName() + "("
                );

                Class<?>[] parameterTypes = method.getParameterTypes();

                for (int i = 0; i < parameterTypes.length; i++) {

                    System.out.print(parameterTypes[i].getSimpleName());

                    if (i < parameterTypes.length - 1) {
                        System.out.print(", ");
                    }
                }

                System.out.println(")");
            }

            System.out.println();

            // Demo 4: Create object dynamically

            Constructor<?> defaultConstructor =
                    studentClass.getConstructor();

            Object studentObject =
                    defaultConstructor.newInstance();

            System.out.println("Created object:");
            System.out.println(studentObject);

            System.out.println();

            // Demo 5: Call public method

            Method sayHelloMethod =
                    studentClass.getMethod("sayHello");

            System.out.println("Calling public method:");

            sayHelloMethod.invoke(studentObject);

            System.out.println();

            // Demo 6: Access and modify private field

            Field nameField =
                    studentClass.getDeclaredField("name");

            nameField.setAccessible(true);

            System.out.println("Original private field:");
            System.out.println(nameField.get(studentObject));

            nameField.set(studentObject, "Ana");

            System.out.println("Modified object:");
            System.out.println(studentObject);

            System.out.println();

            // Demo 7: Call private method

            Method secretMethod =
                    studentClass.getDeclaredMethod("secretMethod");

            secretMethod.setAccessible(true);

            System.out.println("Calling private method:");

            secretMethod.invoke(studentObject);

            System.out.println();

            // Demo 8: Constructor selection

            Constructor<?> constructor1 =
                    studentClass.getConstructor();

            Constructor<?> constructor2 =
                    studentClass.getConstructor(String.class);

            Constructor<?> constructor3 =
                    studentClass.getConstructor(String.class, int.class);

            Object student1 =
                    constructor1.newInstance();

            Object student2 =
                    constructor2.newInstance("Maria");

            Object student3 =
                    constructor3.newInstance("George", 22);

            System.out.println("Objects created with different constructors:");

            System.out.println(student1);
            System.out.println(student2);
            System.out.println(student3);

            System.out.println();

            // Demo 9: Simple object inspector

            System.out.println("Object inspector:");

            inspect(student3);

            System.out.println();

            // Demo 10: JSON serializer

            System.out.println("JSON serializer:");

            String json = toJson(student3);

            System.out.println(json);

            System.out.println();

            // Demo 11: CSV mapper

            System.out.println("CSV mapper:");

            String[] columns = {"name", "age"};
            String[] values = {"Elena", "25"};

            Student csvStudent =
                    csvToObject(Student.class, columns, values);

            System.out.println(csvStudent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Demo 9: Object inspector

    public static void inspect(Object obj) {

        try {

            Class<?> clazz = obj.getClass();

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);

                System.out.println(
                        field.getName() + " = " + field.get(obj)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Demo 10: JSON serializer

    public static String toJson(Object obj) {

        try {

            Class<?> clazz = obj.getClass();

            Field[] fields = clazz.getDeclaredFields();

            StringBuilder json = new StringBuilder();

            json.append("{");

            for (int i = 0; i < fields.length; i++) {

                fields[i].setAccessible(true);

                json.append("\"")
                        .append(fields[i].getName())
                        .append("\":");

                Object value = fields[i].get(obj);

                if (value instanceof String) {

                    json.append("\"")
                            .append(value)
                            .append("\"");

                } else {

                    json.append(value);
                }

                if (i < fields.length - 1) {
                    json.append(",");
                }
            }

            json.append("}");

            return json.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{}";
    }

    // Demo 11: CSV mapper

    public static <T> T csvToObject(
            Class<T> clazz,
            String[] columns,
            String[] values
    ) {

        try {

            T object =
                    clazz.getConstructor().newInstance();

            for (int i = 0; i < columns.length; i++) {

                Field field =
                        clazz.getDeclaredField(columns[i]);

                field.setAccessible(true);

                if (field.getType() == int.class) {

                    field.set(
                            object,
                            Integer.parseInt(values[i])
                    );

                } else if (field.getType() == String.class) {

                    field.set(object, values[i]);
                }
            }

            return object;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}