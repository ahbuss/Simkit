package simkit.random;


/**
* Holds the Class of a RandomVariate. Can be used to find the Class of a RandomVariate
* from its Class name.
* 
* @version  $Id$
*/
public class RandomVariateType {

/**
* The Class of the RandomVariate.
**/
    private Class<?> typeClass;

/** 
* Creates a new RandomVariateType for the given class name.
* @throws IllegalArgumentException If a Class with the given class name is not found,
* or if the Class does not have a default constructor.
**/
    protected RandomVariateType(String className) {

        try {
            typeClass = Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + className);
        }
        try {
            typeClass.getConstructor((Class[])null);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No zero-parameter constructor for " +
                typeClass.getName());
        }

    }

/**
* Creates a new RandomVariateType for the given Class.
**/
    protected RandomVariateType(Class typeClass) {
        this.typeClass = typeClass;
    }

/**
* Returns the Class of the RandomVariate that is of this RandomVariateType.
**/
    public Class getTypeClass() { return typeClass; }
} 
