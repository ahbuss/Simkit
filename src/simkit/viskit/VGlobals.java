package simkit.viskit;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Apr 5, 2004
 * Time: 3:20:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class VGlobals
{
  private static VGlobals me;
  public static synchronized VGlobals instance()
  {
    if(me == null) {
      me = new VGlobals();
    }
    return me;
  }
  private VGlobals()
  {

  }
  public String [] getTypes()
  {
    return defaultTypes;
  }

  private String [] defaultTypes = {
    "int",
    "float",
    "double",
    "short",
    "java.lang.String"
  };

  public void addType(String ty)
  {
    String[] newArr = new String[defaultTypes.length+1];
    System.arraycopy(defaultTypes,0,newArr,0,defaultTypes.length);
    newArr[newArr.length-1] = ty;
    defaultTypes = newArr;
    cbMod = new DefaultComboBoxModel(defaultTypes);
  }
  ComboBoxModel cbMod = new DefaultComboBoxModel(defaultTypes);
  public ComboBoxModel getTypeCBModel()
  {
    return cbMod;
  }

  public void setStateVarsList(Collection svs)
  {
    stateVars = svs;
  }
  private Collection stateVars;
  public ComboBoxModel getStateVarsCBModel()
  {
    return new DefaultComboBoxModel(new Vector(stateVars));
  }

}
