package simkit.viskit.jgraph;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.JGraph;

import javax.swing.*;
import java.io.Serializable;
import java.awt.*;
import java.util.Map;

/**
 * OPNAV N81-NPS World-Class-Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA
 * User: mike
 * Date: Feb 23, 2004
 * Time: 3:40:51 PM
 */

/*
 * @(#)VertexRenderer.java	1.0 1/1/02
 *
 * Copyright (c) 2001-2004, Gaudenz Alder
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of JGraph nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

/**
 * This renderer displays entries that implement the CellView interface
 * and supports the following attributes. If the cell view is not a leaf,
 * this object is only visible if it is selected.
 * <li>
 * GraphConstants.BOUNDS
 * GraphConstants.ICON
 * GraphConstants.FONT
 * GraphConstants.OPAQUE
 * GraphConstants.BORDER
 * GraphConstants.BORDERCOLOR
 * GraphConstants.LINEWIDTH
 * GraphConstants.FOREGROUND
 * GraphConstants.BACKGROUND
 * GraphConstants.VERTICAL_ALIGNMENT
 * GraphConstants.HORIZONTAL_ALIGNMENT
 * GraphConstants.VERTICAL_TEXT_POSITION
 * GraphConstants.HORIZONTAL_TEXT_POSITION
 * </li>
 *
 * @version 1.0 1/1/02
 * @author Gaudenz Alder
 */

public class vVertexRenderer
	extends JComponent    // JLabel jmb
	implements CellViewRenderer, Serializable {

	/** Use this flag to control if groups should appear transparent. */
	protected boolean hideGroups = true;

	/** Cache the current graph for drawing. */
	transient protected JGraph graph;

	/** Cache the current shape for drawing. */
	transient protected VertexView view;

	/** Cached hasFocus and selected value. */
	transient protected boolean hasFocus,
		selected,
		preview,
		opaque,
		childrenSelected;

	/** Cached default foreground and default background. */
	transient protected Color defaultForeground, defaultBackground, bordercolor;

	/** Cached borderwidth. */
	transient protected int borderWidth;

	/** Cached value of the double buffered state */
	transient boolean isDoubleBuffered = false;

	/**
	 * Constructs a renderer that may be used to render vertices.
	 */
	public vVertexRenderer() {
		defaultForeground = UIManager.getColor("Tree.textForeground");
		defaultBackground = UIManager.getColor("Tree.textBackground");
	}

	/**
	 * Constructs a renderer that may be used to render vertices.
	 */
	public vVertexRenderer(boolean hideGroups) {
		defaultForeground = UIManager.getColor("Tree.textForeground");
		defaultBackground = UIManager.getColor("Tree.textBackground");
		this.hideGroups = hideGroups;
	}

	/**
	 * Configure and return the renderer based on the passed in
	 * components. The value is typically set from messaging the
	 * graph with <code>convertValueToString</code>.
	 * We recommend you check the value's class and throw an
	 * illegal argument exception if it's not correct.
	 *
	 * @param   graph the graph that that defines the rendering context.
	 * @param   value the object that should be rendered.
	 * @param   selected whether the object is selected.
	 * @param   hasFocus whether the object has the focus.
	 * @param   isPreview whether we are drawing a preview.
	 * @return	the component used to render the value.
	 */
	public Component getRendererComponent(
		JGraph graph,
		CellView view,
		boolean sel,
		boolean focus,
		boolean preview) {
		this.graph = graph;
		isDoubleBuffered = graph.isDoubleBuffered();
		if (view instanceof VertexView) {
			this.view = (VertexView) view;
			setComponentOrientation(graph.getComponentOrientation());
			if (graph.getEditingCell() != view.getCell()) {
				Object label = graph.convertValueToString(view);
				if (label != null)
				;// jmb	setText(label.toString());
				else
					; //jmb setText(null);
			} else
				; // jmb setText(null);
			this.graph = graph;
			this.hasFocus = focus;
			this.childrenSelected =
				graph.getSelectionModel().isChildrenSelected(view.getCell());
			this.selected = sel;
			this.preview = preview;
			if (this.view.isLeaf() || !hideGroups)
				installAttributes(view);
			else {
				// jmb setText(null);
				setBorder(null);
				setOpaque(false);
				// jmb setIcon(null);
			}
			return this;
		}
		return null;
	}

	/**
	 * Install the attributes of specified cell in this
	 * renderer instance. This means, retrieve every published
	 * key from the cells hashtable and set global variables
	 * or superclass properties accordingly.
	 *
	 * @param   cell to retrieve the attribute values from.
	 */
	protected void installAttributes(CellView view) {
		Map map = view.getAllAttributes();
	// jmb	setIcon(GraphConstants.getIcon(map));
		setOpaque(GraphConstants.isOpaque(map));
		setBorder(GraphConstants.getBorder(map));
	// jmb	setVerticalAlignment(GraphConstants.getVerticalAlignment(map));
	// jmb	setHorizontalAlignment(GraphConstants.getHorizontalAlignment(map));
	// jmb	setVerticalTextPosition(GraphConstants.getVerticalTextPosition(map));
	// jmb	setHorizontalTextPosition(GraphConstants.getHorizontalTextPosition(map));
		bordercolor = GraphConstants.getBorderColor(map);
		borderWidth = Math.max(1, Math.round(GraphConstants.getLineWidth(map)));
		if (getBorder() == null && bordercolor != null)
			setBorder(BorderFactory.createLineBorder(bordercolor, borderWidth));
		Color foreground = GraphConstants.getForeground(map);
		setForeground((foreground != null) ? foreground : defaultForeground);
		Color background = GraphConstants.getBackground(map);
		setBackground((background != null) ? background : defaultBackground);
		setFont(GraphConstants.getFont(map));
	}

	/**
	 * Paint the renderer. Overrides superclass paint
	 * to add specific painting.
	 */
	public void paint(Graphics g) {
		try {
			//if (preview && !isDoubleBuffered)
			//	setOpaque(false);
			super.paint(g);   // jmb this will come down to paintCompoent
			paintSelectionBorder(g);
		} catch (IllegalArgumentException e) {
			// JDK Bug: Zero length string passed to TextLayout constructor
		}
	}
  Color circColor = new Color(255,255,204); // pale yellow
  Font myfont = new Font("Verdana",Font.PLAIN,10);

  // jmb
  protected void paintComponent(Graphics g)
  {
    Rectangle r = view.getBounds();
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(circColor);
    int myoff=2;
    g2.fillOval(myoff,myoff,r.width-2*myoff,r.height-2*myoff); // size of rect is 54,54
    g2.setColor(Color.darkGray);
    g2.drawOval(myoff,myoff,r.width-2*myoff,r.height-2*myoff);

    // Draw the text in the circle
    g2.setFont(myfont);         // uses component's font if not specified
    DefaultGraphCell cell = (DefaultGraphCell)view.getCell();
    String nm = cell.getUserObject().toString();
    FontMetrics metrics = g2.getFontMetrics();

    String[] lns = nm.split("\n");       // handle multi-line titles

    int hgt = metrics.getHeight();  // height of a line of text
    int ytop = 54/2 - (hgt * (lns.length-1)/ 2) + hgt/4;    // start y coord

    for(int i=0;i<lns.length;i++) {
      int xp = metrics.stringWidth(lns[i]); // length of string fragment
      int y = ytop + (hgt*i);
      g2.drawString(lns[i],(54-xp)/2,y);
    }
  }

  protected void paintBorder(Graphics g)
  {
    // jmb lose the rectangle super.paintBorder(g);
  }

	/**
	 * Provided for subclassers to paint a selection border.
	 */
	protected void paintSelectionBorder(Graphics g) {
		((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
		if (childrenSelected)
			g.setColor(graph.getGridColor());
		else if (hasFocus && selected)
			g.setColor(graph.getLockedHandleColor());
		else if (selected)
			g.setColor(graph.getHighlightColor());
		if (childrenSelected || selected) {
			Dimension d = getSize();
			g.drawRect(0, 0, d.width - 1, d.height - 1);
		}
	}

	/**
	 * Returns the intersection of the bounding rectangle and the
	 * straight line between the source and the specified point p.
	 * The specified point is expected not to intersect the bounds.
	 */
	public Point getPerimeterPoint(VertexView view, Point source, Point p) {
		Rectangle bounds = view.getBounds();
		int x = bounds.x;
		int y = bounds.y;
		int width = bounds.width;
		int height = bounds.height;
		int xCenter = (int) (x + width / 2);
		int yCenter = (int) (y + height / 2);
		int dx = p.x - xCenter; // Compute Angle
		int dy = p.y - yCenter;
		double alpha = Math.atan2(dy, dx);
		int xout = 0, yout = 0;
		double pi = Math.PI;
		double pi2 = Math.PI / 2.0;
		double beta = pi2 - alpha;
		double t = Math.atan2(height, width);
		if (alpha < -pi + t || alpha > pi - t) { // Left edge
			xout = x;
			yout = yCenter - (int) (width * Math.tan(alpha) / 2);
		} else if (alpha < -t) { // Top Edge
			yout = y;
			xout = xCenter - (int) (height * Math.tan(beta) / 2);
		} else if (alpha < t) { // Right Edge
			xout = x + width;
			yout = yCenter + (int) (width * Math.tan(alpha) / 2);
		} else { // Bottom Edge
			yout = y + height;
			xout = xCenter + (int) (height * Math.tan(beta) / 2);
		}
		return new Point(xout, yout);
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void validate() {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void revalidate() {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void repaint(long tm, int x, int y, int width, int height) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void repaint(Rectangle r) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	protected void firePropertyChange(
		String propertyName,
		Object oldValue,
		Object newValue) {
		// Strings get interned...
		if (propertyName == "text")
			super.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void firePropertyChange(
		String propertyName,
		byte oldValue,
		byte newValue) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void firePropertyChange(
		String propertyName,
		char oldValue,
		char newValue) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void firePropertyChange(
		String propertyName,
		short oldValue,
		short newValue) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void firePropertyChange(
		String propertyName,
		int oldValue,
		int newValue) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void firePropertyChange(
		String propertyName,
		long oldValue,
		long newValue) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void firePropertyChange(
		String propertyName,
		float oldValue,
		float newValue) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void firePropertyChange(
		String propertyName,
		double oldValue,
		double newValue) {
	}

	/**
	 * Overridden for performance reasons.
	 * See the <a href="#override">Implementation Note</a>
	 * for more information.
	 */
	public void firePropertyChange(
		String propertyName,
		boolean oldValue,
		boolean newValue) {
	}

	/**
	 * Returns the hideGroups.
	 * @return boolean
	 */
	public boolean isHideGroups() {
		return hideGroups;
	}

	/**
	 * Sets the hideGroups.
	 * @param hideGroups The hideGroups to set
	 */
	public void setHideGroups(boolean hideGroups) {
		this.hideGroups = hideGroups;
	}

}
