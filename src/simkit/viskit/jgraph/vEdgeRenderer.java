package simkit.viskit.jgraph;

import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;

import java.awt.*;

/**
 * OPNAV N81-NPS World-Class-Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA
 * User: mike
 * Date: Feb 24, 2004
 * Time: 2:32:30 PM
 */

/**
 * The guy that actually paints edges.
 */
public class vEdgeRenderer extends EdgeRenderer
{
  /**
   * Override only to turn on antialiasing in edge painting
   * @param g passed-in graphics context
   */
  public void paint(Graphics g)
  {
    ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    super.paint(g);
  }

  /**
   * Override only to use a different way of positioning the label on the edge.  The default method
   * doesn't do such a good job on quadradic edges.
   * @param view EdgeView for this edge
   * @return Center point of label
   */
  public Point getLabelPosition(EdgeView view)
  {
    super.getLabelPosition(view);  // invcase of side effects, but forget the return
    
    Rectangle tr = getPaintBounds(view);
    return new Point(tr.x+tr.width/2, tr.y+tr.height/2); // just use the center of the clip
  }
}
