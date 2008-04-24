/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simkit.util;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import simkit.SimEntityBase;

/**
 *
 * @author kirk
 */
public class JackcessTableReadTest extends TestCase {
    
    public JackcessTableReadTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    
    public void testOpenMDBFile() {
        Database db = null;
        Table t = null;
        try {
            File baseDir = new File("");
            System.err.println("baseDir is: " + baseDir.toString());
            String baseDirPath = baseDir.getAbsolutePath();
            System.err.println("baseDir aboslute path is: " + baseDirPath.toString());
            File file = new File(baseDirPath + "/tests/datafiles/SampleInput.mdb");
            db = Database.open(file);
//            System.out.println(db.toString());
//            t = db.getTable("SensorType");
//            System.out.println(db.getAccessControlEntries().display());
//            System.out.println(t.display());
//            System.out.println(db.getSystemCatalog().getName());
        } catch (IOException ex) {
            Logger.getLogger(JackcessTableReadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
            
//            System.out.println(Database.open(new File("my.mdb")).getTable("MyTable").display());
        Set<String> tables = db.getTableNames();

        
        
        for (String tableName : tables) {
            try {
                System.out.println("Table:  >" + tableName + "<");
                if ("".equals(tableName)) {
                    System.err.println("skipping table with empty name");
                    continue;
                }
                t = db.getTable(tableName);
            } catch (IndexOutOfBoundsException ex) {
                // can't seem to find a better way to check for empty tables
                continue;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

//            try {
//                System.out.println(t.display());
//            } catch (IOException ex) {
//                System.err.println("table display pooped out");
//            }
        }
        
//        try{
//            Table sensorTable = db.getTable("Sensor");
//            List<Column> columns = sensorTable.getColumns();
//            System.out.println(columns);
//            
//            
//            sensorTable.addRow(null, "KIRK_SENSOR", "ARNIE_MOVER");
//            System.out.println(sensorTable.display());
//        } catch (Exception e) {
//            System.err.println("Table add threw an exception");
//            throw new RuntimeException(e);
//        }
        

    }
       
    public void testProblematicTable() {
        Database db = null;
        Table t = null;
        try {
            File baseDir = new File("");
            System.err.println("baseDir is: " + baseDir.toString());
            String baseDirPath = baseDir.getAbsolutePath();
            System.err.println("baseDir aboslute path is: " + baseDirPath.toString());
            File file = new File(baseDirPath + "/tests/datafiles/SampleInput.mdb");
            db = Database.open(file);
            t = db.getTable("CVO");
        } catch (IOException ex) {
//          Logger.getLogger(JackcessTableReadTest.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
        }
    }
    
//    public void testWriteToTableWithAutonumberedColumn() {
//        Database db = null;
//        Table t = null;
//        try {
//            File baseDir = new File("");
//            System.err.println("baseDir is: " + baseDir.toString());
//            String baseDirPath = baseDir.getAbsolutePath();
//            System.err.println("baseDir aboslute path is: " + baseDirPath.toString());
//            File file = new File(baseDirPath + "/tests/datafiles/SampleInput.mdb");
//            db = Database.open(file);
//            t = db.getTable("CVO");
//        } catch (IOException ex) {
////          Logger.getLogger(JackcessTableReadTest.class.getName()).log(Level.SEVERE, null, ex);
//            System.err.println(ex);
//        }
//    }
            
    public void testDeleteRow() {
        Database db = null;
        Table t = null;
        try {
            File baseDir = new File("");
            System.err.println("baseDir is: " + baseDir.toString());
            String baseDirPath = baseDir.getAbsolutePath();
            System.err.println("baseDir aboslute path is: " + baseDirPath.toString());
            File file = new File(baseDirPath + "/tests/datafiles/SampleInput.mdb");
            db = Database.open(file);
            t = db.getTable("Mover");
            System.out.println(t.display());

        } catch (IOException ex) {
//          Logger.getLogger(JackcessTableReadTest.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
        }
        
        try {
            System.out.println("There are " + t.getRowCount() + " rows.");
            Iterator<Map<String,Object>> i = t.iterator();
            while(i.hasNext()){
                Map<String,Object> record = i.next();
                Set<String> keys = record.keySet();
                for (String key: keys) {
                    System.out.println(key + "\t\t\t" + record.get(key));
                }
            }
        } catch (Exception ex){
            System.err.println(ex);
            throw new RuntimeException(ex);
        }
    }
            
}
