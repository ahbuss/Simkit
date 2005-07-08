
package simkit.util;

import junit.framework.*;

public class Hashtable2Test extends TestCase {
    protected Hashtable2 table;

    public void setUp() {
        table = new Hashtable2();
    }

    public void tearDown() {
        table = null;
    }

    protected void loadTable() {
        table.put("first_1", "second_1", "1_1");
        table.put("first_2", "second_2", "2_2");
        table.put("first_1", "second_2", "1_2");
    }

    public void testGet() {
        loadTable();
        assertEquals("1_1", table.get("first_1", "second_1"));
        assertEquals("1_2", table.get("first_1", "second_2"));
        assertEquals("2_2", table.get("first_2", "second_2"));
        assertEquals(null, table.get("first_2", "second_1"));
    }
    
    public void testRemoval() {
        loadTable();
        assertEquals("1_1", table.remove("first_1", "second_1"));
        assertEquals("Fails due to bug 513", null, table.get("first_1", "second_1"));
    }

    public void testToArray() {
        loadTable();
        Object[][] array = table.toArray();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.println("i= " + i + ", j= " + j + " => " + array[i][j]);
            }
        }
        assertTrue(true);
    }

        
        
        
 }
