package simkit.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Two-dimensional LinkedHashMap.  Each value has a pair of keys.  The outer
 * Map consists of LinkedHashMaps keyed by the first key, and each corresponding
 * LinkedHashMap is keyed by the second key.  This is essentially a generic
 * version of, and replaces, the old Hashtable2.
 *
 * @author ahbuss
 * @version $Id: LinkedHashMap2.java 1000 2007-02-15 19:43:11Z ahbuss $
 **/
public class LinkedHashMap2<K1, K2, V> extends LinkedHashMap<K1, Map<K2, V>> {
    
    /**
     *  Constructs an empty LinkedHashMap.
     **/
    public LinkedHashMap2() {
        super();
    }
    
    /**
     *  Puts a value into the LinkedHashMap2 with given keys.  If the inner
     *  Map does not exist, it is created.  If there is no corresponding property,
     *  it is created.  If the property already exists, it is overwritten.
     *  @param key1 The first key of the property, such as the Block name
     *  @param key2 The second key, such as the key of the property.
     *  @param value The value of the property.
     **/
    public void put(K1 key1, K2 key2, V value) {
        if (this.containsKey(key1)) {
            this.get(key1).put(key2, value);
        } else {
            Map<K2, V> map = new LinkedHashMap<K2, V>();
            map.put(key2, value);
            this.put(key1, map);
        }
    }
    
    /**
     *  Gets the value of the property.
     *  @param key1 The outer key of the property
     *  @param key2 The inner key of the property.
     *  @return The value of the property with key secondKey in block firstKey.
     **/
    public V get(K1 key1, K2 key2) {
        V value = null;
        if (this.containsKey(key1)) {
            value = this.get(key1).get(key2);
        }
        return value;
    }
    /**
     *  Removes the property specified by the given keys.
     *  @param key1 Key to Map for removed value
     *  @param key2 Key to removed value
     *  @return The removed value or null if the property doesn't exist.
     **/
    public V remove(K1 key1, K2 key2) {
        V removedValue = null;
        Map<K2, V> map = get(key1);
        if (map != null) {
            removedValue = map.remove(key2);
        }
        return removedValue;
    }
    
    /**
     * Converts the LinkedHashMap2 to a n by 2 array of Objects.
     * Where n is the number of entries in the outer Map.
     * The array contains pairs of outer keys and inner Maps.
     * @return Contents as array of Objects
     **/
    public Object[][] toArray() {
        ArrayList<Object[]> contents = new ArrayList<Object[]>();
        for (K1 key1 : this.keySet()) {
            Map<K2, V> map = this.get(key1);
            contents.add(new Object[] { key1, map } );
        }
        return contents.toArray(new Object[this.size()][2]);
    }
}