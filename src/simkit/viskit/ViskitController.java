package simkit.viskit;

import java.util.Vector;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Mar 19, 2004
 * Time: 9:00:57 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ViskitController
{
  void addSimParameter();
  void addStateVariable();
  void arcEdit(SchedulingEdge ed);
  void canArcEdit(CancellingEdge ed);
  void copy();
  void cut();
  void deleteSimParameter();
  void deleteStateVariable();
  void eventList();
  void generateJavaClass();
  void newArc(Object[] nodes);
  void newAssembly();
  void newCancelArc(Object[] nodes);
  void newEventGraph();
  void newNode();
  void newNode(Point p);
  void nodeEdit(EventNode node);
  void open();
  void paste();
  void quit();
  void save();
  void saveAs();
  void selectNodeOrEdge(Vector v);
}
