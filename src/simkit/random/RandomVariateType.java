package simkit.random;

import java.lang.reflect.Constructor;

public class RandomVariateType {

    private Class typeClass;

    protected RandomVariateType(String className) {

        try {
            typeClass = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + className);
        }
        try {
            typeClass.getConstructor(null);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No zero-parameter constructor for " +
                typeClass.getName());
        }

    }

    protected RandomVariateType(Class typeClass) {
        this.typeClass = typeClass;
    }

    public Class getTypeClass() { return typeClass; }
} 