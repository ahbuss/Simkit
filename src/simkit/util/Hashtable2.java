package simkit.util;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 *  <P>A two-dimensional Hashtable, implemented as a Hashtable of Hashtables.
 *  Each item is accessible using two keys.  The first points to a Hashtable
 *  and the second is the key of that Hashtable that references the desired
 *  value.
 *  <P> 3 Nov 1999: Removed load() and save() methods -- now in a subclass called
 *      INIFileProperties.
 *  <P> Added remove() method. 
 *  <P>ToDo:
 *  <UL>
 *  <LI> Modify the undelying Map object to give better control on the order of keys.
 *  <LI> Specifically, Allow the keys to maintain the order in which they were
 *       input.
 *  <LI> Also, enable alphabetical or lexical ordering of keys.
 *  </UL>
 *
 * @version 0.4
 * @author Arnold Buss
**/

public class Hashtable2 extends HashMap {

/**
 *  Construct an empty Hashtable2
**/
	public Hashtable2() {
		super();
	}
	
/**
 *  Put a value into the Hashtable2 with given keys.  If the inner Hashtable
 *  does not exist, it is created.  If there is no corresponding property, it is
 *  created.  If the property already exists, it is overwritten.
 *  @param firstKey The first key of the property, normally the Block name
 *  @param secondKey The second key, normally the key of the property.
 *  @param value The value of the property. 
**/
	public void put(Object firstKey, Object secondKey, Object value) {
		Hashtable values;
		if (this.containsKey(firstKey)) {
			values = (Hashtable) this.get(firstKey);
		}
		else {
			values = new Hashtable(10);
            this.put(firstKey, values);
		}
		values.put(secondKey, value);
	}

/**
 *  Overrides the put(Object, Object) method of Properties.  The value must be
 *  a Map or else things will not work properly.
 *  @param key The outer key, normally the block name.
 *  @param value The value of the property, must be an instance of Map.
**/
    public Object put(Object key, Object value) {
        if (value instanceof Map) {
            return super.put(key, value);
        }
        else {
            throw new IllegalArgumentException("Hashtable2 can only accept Maps as values.");
        }
    }

/**
 *  @param firstKey The block name.
 *  @param secondKey The key of the property.
 *  @return The value of the property with key secondKey in block firstKey.
**/
	public Object get(Object firstKey, Object secondKey) {
		Map values;
		Object returnValue = null;
		if (this.containsKey(firstKey)) {
			values = (Map) this.get(firstKey);
			returnValue = values.get(secondKey);
		}
		return returnValue;
	}

/**
 *  @param firstKey Key to Map for removed value
 *  @param secondKey Key to removed value
 *  @return The removed value or null.
**/
    public Object remove(String firstKey, String secondKey) {
        Object removedValue = null;
        Object map = get(firstKey);
        if (map != null) {
            removedValue = ((Map) map).get(secondKey);
        }
        return removedValue;
    }

    public Object[][] toArray() {
        Object[][] temp = new Object[this.size()][];
        int k = -1;
        for (Iterator i = keySet().iterator(); i.hasNext(); ) {
            k++;
            temp[k] = new Object[2];
            Object key = i.next();
            temp[k][0] = key;
            temp[k][1] = this.get(key);
        }
        return temp;
    }

}
