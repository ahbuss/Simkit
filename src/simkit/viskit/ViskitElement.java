package simkit.viskit;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Mar 19, 2004
 * Time: 2:10:07 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 *  Base class for the objects that get passed around between M, V and C.
 */
abstract public class ViskitElement
{
  public Object opaqueViewObject;       // private use of V
  public Object opaqueModelObject;      // for private use of M
  public Object opaqueControllerObject; // for private use of C
}
