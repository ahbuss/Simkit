package simkit.data;

import java.lang.reflect.*;

/**
 *  A factory class that produces RandomNumber and RandomVariate instances.
 *  Still needs some work...
 *
 *  @author Arnold Buss
 *  @deprecated This class has been replaced by <CODE>simkit.random.RandomVariateFactory</CODE>
 *           for obtaining instances of <CODE>RandomVariate</CODE> and
 *           <CODE>simkit.random.RandomNumberFactory</CODE> for obtaining instances
 *           of <CODE>RandomNumber</CODE>
 *  @see simkit.random.RandomVariateFactory
 *  @see simkit.random.RandomVariate
 *  @see simkit.random.RandomNumberFactory
 *  @see simkit.random.RandomNumber
**/

public class RandomFactory {

  private static final String DEFAULT_PACKAGE = "simkit.data";
  private static final String DEFAULT_RANDOM_NUMBER_CLASS = "RandomStream";
  private static final long DEFAULT_SEED = 2116429302L;

  protected RandomFactory() {
  }

  public static RandomNumber getRandomNumber() {
      return getRandomNumber(DEFAULT_SEED);
  }

  public static RandomNumber getRandomNumber(long seed) {
      return getRandomNumber(DEFAULT_PACKAGE,DEFAULT_RANDOM_NUMBER_CLASS, seed);
  }

  public static RandomNumber getRandomNumber(String packageName, String className) {
      return getRandomNumber(packageName, className, DEFAULT_SEED);
  }

  public static RandomNumber getRandomNumber(String className) {
      return getRandomNumber(null, className);
  }

  public static RandomNumber getRandomNumber(String className, long seed) {
      return getRandomNumber(null, className, seed);
  }

  public static RandomNumber getRandomNumber(String packageName, String className, long seed) {
    RandomNumber rn = null;
    try {
      String fullName = ((packageName == null) || packageName.equals("")) ? className : packageName + '.' + className;
      Class c = Class.forName(fullName);
      Constructor construct = c.getConstructor( new Class[] {Long.TYPE});
       rn = (RandomNumber) construct.newInstance(new Object[]{new Long(seed)});
    }
    catch (ClassNotFoundException e) {System.err.println(e); e.printStackTrace(System.err);}
    catch (NoSuchMethodException e) {System.err.println(e); e.printStackTrace(System.err);}
    catch (IllegalAccessException e) {System.err.println(e); e.printStackTrace(System.err);}
    catch (InstantiationException e) {System.err.println(e); e.printStackTrace(System.err);}
    catch (InvocationTargetException e) {System.err.println(e.getTargetException()); e.printStackTrace(System.err);}
    return rn;
  }

  public static RandomVariate getRandomVariate(String packageName, String name, Object[] params,
    long seed) {
    RandomVariate rv = getRandomVariate(packageName, name, params);
    rv.setSeed(seed);
    return rv;
  }

  public static RandomVariate getRandomVariate(String packageName, String name, double param, long seed) {
      return getRandomVariate(packageName, name, new Object[] {new Double(param)}, seed);
  }

  public static RandomVariate getRandomVariate(String name, double param, long seed) {
      if (name.indexOf('.') == -1) {   // only class name, use default package
        return getRandomVariate(DEFAULT_PACKAGE, name, new Object[] {new Double(param)}, seed);
      }
      else {                         // contains package -- assume fully qualified
        return getRandomVariate(null, name, new Object[] {new Double(param)}, seed);
      }
  }

  public static RandomVariate getRandomVariate(String packageName, String name, Object[] params) {
      RandomVariate rv = null;
      String fullName = (packageName == null || packageName.equals("")) ? name : packageName + "." + name;
      fullName += (fullName.endsWith("Variate")) ? "" : "Variate";
      try {
          Class c = Class.forName(fullName);
          Constructor construct = c.getConstructor(new Class[]{java.lang.Object[].class});
          rv = (RandomVariate) construct.newInstance(new Object[]{params});
      }
      catch (ClassNotFoundException e) {/*System.err.println(e); e.printStackTrace(System.err);*/}
      catch (NoSuchMethodException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (IllegalAccessException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (InstantiationException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (InvocationTargetException e) {System.err.println(e.getTargetException()); e.printStackTrace(System.err);}
      return rv;
  }

  public static RandomVariate getRandomVariate(String packageName, String name, Object[] params, RandomNumber rng) {   
      RandomVariate rv = null;
      String fullName = (packageName == null || packageName.equals("")) ? name : packageName + "." + name;
      fullName += (fullName.endsWith("Variate")) ? "" : "Variate";
      try {
          Class c = Class.forName(fullName);
          Constructor construct = c.getConstructor(new Class[]{java.lang.Object[].class,
                                         simkit.data.RandomNumber.class});
          rv = (RandomVariate) construct.newInstance(new Object[]{params, rng});
      }
      catch (ClassNotFoundException e) {/*System.err.println(e); e.printStackTrace(System.err);*/}
      catch (NoSuchMethodException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (IllegalAccessException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (InstantiationException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (InvocationTargetException e) {System.err.println(e.getTargetException()); e.printStackTrace(System.err);}
      return rv;
  }

  public static RandomVariate getRandomVariate(String name, double parameter) {
      return getRandomVariate(name, new Object[] {new Double(parameter)});
  }

  public static RandomVariate getRandomVariate(String name, double param1, double param2) {
      return getRandomVariate(name, new Object[] {new Double(param1), new Double(param2)});
  }

  public static RandomVariate getRandomVariate(String name, double param1, double param2, long seed) {
      return getRandomVariate(name, new Object[] {new Double(param1), new Double(param2)}, seed);
  }

  public static RandomVariate getRandomVariate(String name) {
      return getRandomVariate(DEFAULT_PACKAGE, name);
  }

  public static RandomVariate getRandomVariate(String name, Object[] params) {
      RandomVariate rv = getRandomVariate(null, name, params);
      return (rv != null) ? rv : getRandomVariate(DEFAULT_PACKAGE, name, params);
  }

  public static RandomVariate getRandomVariate(String name, Object[] params, long seed) {
    if (name.indexOf('.') == -1) {    // no package - use default
        return getRandomVariate(DEFAULT_PACKAGE, name, params, seed);
    }
    else {  // has package name -- assume fully qualified
        return getRandomVariate(null, name, params, seed);
    }
  }

  public static RandomVariate getRandomVariate(String name, double param, RandomNumber rng) {
       RandomVariate rv = getRandomVariate(null, name, new Object[] {new Double(param)}, rng);
       return (rv != null) ? rv : getRandomVariate(DEFAULT_PACKAGE, name, new Object[] {new Double(param)}, rng);
  }

  public static RandomVariate getRandomVariate(String name, double param1, double param2, RandomNumber rng) {
       RandomVariate rv = getRandomVariate(null, name, new Object[] {new Double(param1), new Double(param2)}, rng);
       return (rv != null) ? rv : getRandomVariate(DEFAULT_PACKAGE, name, new Object[] {new Double(param1), new Double(param2)}, rng);
  }

  public static RandomVariate getRandomVariate(String name, Object[] params, RandomNumber rng) {
      RandomVariate rv = getRandomVariate(null, name, params, rng);
      return (rv != null) ? rv : getRandomVariate(DEFAULT_PACKAGE, name, params, rng);
  }

  public static RandomVariate getRandomVariate(String packageName, String name) {
      return getRandomVariate(packageName, name, new Object[]{});
  }

  private static Class[] getSignature(Object[] params) {
    Class[] signature = null;
    if (params != null) {
      signature = new Class[params.length];
      for (int i = 0; i < signature.length; i++) {
        signature[i] = params[i].getClass();
      }
    }
    return signature;
  }

/**
 *  Returns a RandomNumber instance that 
 *
**/
    public static RandomNumber getAntithetic(RandomNumber rv) {
        return getAntithetic(rv, true);
    }

    public static RandomNumber getAntithetic(RandomNumber rv, boolean synch) {
        return synch ?
                new Antithetic(rv) :
                new Antithetic(getRandomNumber(rv.getClass().getName(), rv.getSeed()));
    }
}
