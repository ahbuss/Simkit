package simkit.viskit;

import simkit.viskit.jgraph.vGraphComponent;
import simkit.viskit.jgraph.vGraphModel;
import simkit.viskit.mvc.mvcAbstractJFrameView;
import simkit.viskit.mvc.mvcModelEvent;
import simkit.viskit.model.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import actions.*;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 12:52:59 PM
 */

/**
 * Main "view" of the Viskit app.  This class controls a 3-paneled JFrame showing a jgraph on the left and state
 * variables and sim parameters panels on the right, with menus and a toolbar.  To fully implement application-level MVC,
 * events like the
 * dragging and dropping of a node on the screen are first recognized in this class, but the GUI is not yet changed.
 * Instead, this class (the View) messages the controller class (Controller -- by means of the ViskitController i/f).
 * The controller then informs the model (Model), which then updates itself and "broadcasts" that fact.  This class is a model
 * listener, so it gets the report, then updates the GUI.  A round trip.
 */

public class EventGraphViewFrame extends mvcAbstractJFrameView implements ViskitView
{
  // Modes we can be in--selecting items, adding nodes to canvas, drawing arcs, etc.
  public final static int SELECT_MODE = 0;
  public final static int ADD_NODE_MODE = 1;
  public final static int ARC_MODE = 2;
  public final static int CANCEL_ARC_MODE = 3;

  /**
   * Menu bar for the application
   */
  private JMenuBar menuBar;

  // We keep a reference to the menus in case we have to turn menu
  // items on or off
  private JMenu fileMenu, editMenu, simulationMenu;

  /**
   * Two right panels
   */
  private JPanel stateVariablesPanel, parametersPanel;

  /**
   * canvas/drawing pane
   */
  private vGraphComponent graphPane;

  /**
   * main split pane, with left-hand drawing pane & state variables on right
   */
  private JSplitPane stateDrawingSplit;

  /**
   * Secondary split pane, with state variables on the top and parameters on the bottom
   */
  private JSplitPane stateParameterSplit;

  /**
   * Toolbar for dropping icons, connecting, etc.
   */
  private JToolBar toolBar;

  /**
   * Button group that holds the mode buttons.
   */
  private ButtonGroup modeButtonGroup;

  // Mode buttons on the toolbar
  private JToggleButton selectMode;
  private JLabel addMode;
  private JToggleButton arcMode;
  private JToggleButton cancelArcMode;

  /**
   * Whether or not we can edit this event graph
   */
  private boolean isEditable;

  /**
   * StateVariables panel
   */
  private JButton varMinusButt;
  private JButton varPlusButt;
  private JButton varEdButt;

  /**
   * SimParameters panel
   */
  private JButton parmMinusButt;
  private JButton parmPlusButt;
  private JButton parmEdButt;
  private ParametersPanel pp;
  private VariablesPanel vp;

  /**
   * Constructor; lays out initial GUI objects
   */
  public EventGraphViewFrame(Model mod, Controller ctrl)
  //====================================================
  {
    super("Viskit");

    this.initMVC(mod,ctrl);   // set up mvc linkages
    this.initUI();            // build widgets

   // parameterTableModel = new ParameterTableModel();
   // stateVariableTableModel = new StateVariableTableModel();

    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    this.setLocation((d.width - 800) / 2, (d.height - 600) / 2);
    this.setSize(800, 600);

    this.setEditable(true);

    // Won't want to do this in final version...need to check for save
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
  }

  /**
   * Sets whether or not we can edit this event graph
   */
  public void setEditable(boolean pIsEditable)
  {
    isEditable = pIsEditable;
  }

  /**
   * Returns whether or not we can edit this event graph
   */
  public boolean getIsEditable()
  {
    return isEditable;
  }

  /**
   * Returns the current mode--select, add, arc, cancelArc
   */
  public int getCurrentMode()
  {
    // Use the button's selected status to figure out what mode
    // we are in.

    if (selectMode.isSelected() == true) return SELECT_MODE;
    //if (addMode.isSelected() == true) return ADD_NODE_MODE;
    if (arcMode.isSelected() == true) return ARC_MODE;
    if (cancelArcMode.isSelected() == true) return CANCEL_ARC_MODE;
    // If none of them are selected we're in serious trouble.
    //assert false : "getCurrentMode()";
    System.err.println("assert false : \"getCurrentMode()\"");
    return 0;
  }

  /**
   * Initialize the MCV connections
   */
  private void initMVC(Model mod, Controller ctrl)
  {
    setModel(mod);
    setController(ctrl);
  }

  /**
   * Initialize the user interface
   */
  private void initUI()
  {
    this.setFont(new Font("Courier",Font.PLAIN,12));
    // Layout menus
    this.setupMenus();

    // Layout of toolbar
    this.setupToolbar();

    // Set up a top level pane that will be the content pane. This
    // has a border layout, and contains the toolbar on the top and
    // the main splitpane underneath.

    // top level panel
    JPanel top = new JPanel();
    top.setLayout(new BorderLayout());
    top.add(toolBar, BorderLayout.NORTH);

    // Set up the basic panes for the layouts
    vGraphModel mod = new vGraphModel();
    graphPane = new vGraphComponent(mod,this);
    mod.graph = graphPane;                               // todo fix this

    graphPane.addMouseListener(new vCursorHandler());
    try{
      graphPane.getDropTarget().addDropTargetListener(new vDropTargetAdapter());
    }
    catch(Exception e) {
      //assert false : "Drop target init. error";
      System.err.println("assert false : \"Drop target init. error\"");
    }

    // State variables area:
    stateVariablesPanel = new JPanel();
    stateVariablesPanel.setLayout(new BoxLayout(stateVariablesPanel,BoxLayout.Y_AXIS)); //new BorderLayout());
    stateVariablesPanel.add(Box.createVerticalStrut(5));

     JPanel p = new JPanel();
     p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
     p.add(Box.createHorizontalGlue());
      p.add(new JLabel("State Variables"));
     p.add(Box.createHorizontalGlue());
    stateVariablesPanel.add(p);

     vp = new VariablesPanel(300,85);
    stateVariablesPanel.add(vp);
    stateVariablesPanel.add(Box.createVerticalStrut(5));

    // Wire handlers for stateVariable adds, deletes and edits
    vp.addMinusListener(ActionIntrospector.getAction((ViskitController)getController(),"deleteStateVariable"));
    vp.addPlusListener (ActionIntrospector.getAction((ViskitController)getController(),"newStateVariable"));

    // Introspector can't handle a param to the method....?
    vp.addDoubleClickedListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        ((ViskitController)getController()).stateVariableEdit((vStateVariable)event.getSource());
      }
    });

    // Simulation parameters area
    parametersPanel = new JPanel();
    parametersPanel.setLayout(new BoxLayout(parametersPanel,BoxLayout.Y_AXIS)); //BorderLayout());
    parametersPanel.add(Box.createVerticalStrut(5));
     p = new JPanel();
     p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
     p.add(Box.createHorizontalGlue());
     p.add(new JLabel("Simulation parameters"));
     p.add(Box.createHorizontalGlue());
    parametersPanel.add(p);

      pp = new ParametersPanel(300);
    parametersPanel.add(pp);
    parametersPanel.add(Box.createVerticalStrut(5));

    // Wire handlers for parameter adds, deletes and edits
    pp.addMinusListener(ActionIntrospector.getAction((ViskitController)getController(),"deleteSimParameter"));
    pp.addPlusListener (ActionIntrospector.getAction((ViskitController)getController(),"newSimParameter"));

    // Introspector can't handle a param to the method....?
    pp.addDoubleClickedListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        ((ViskitController)getController()).simParameterEdit((vParameter)event.getSource());
      }
    });

    // Split pane that has state variables on top, parameters on bottom.
    stateParameterSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, stateVariablesPanel, parametersPanel);

    // Split pane with the canvas on the left and a split pane with state variables and parameters on the right.
    stateDrawingSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(graphPane), stateParameterSplit);
    top.add(stateDrawingSplit, BorderLayout.CENTER);

    this.getContentPane().add(top);
  }

  // The following is done solely to be able to set the splitpanes to a default position on startup.
  // Widgets have to be showing.
  private boolean firstShown = false;

  public void setVisible(boolean b)
  {
    super.setVisible(b);
    if(firstShown == false) {
      firstShown = true;
      stateDrawingSplit.setDividerLocation(0.5d);
      stateParameterSplit.setDividerLocation(0.5d);
    }
  }

  /**
   * run the add parameter dialog
   */
  public void addParameterDialog()
  {

    if( ParameterDialog.showDialog(this,this,null)) {      // blocks here
      ((ViskitController)getController()).newSimParameter(ParameterDialog.newName,
                                                          ParameterDialog.newType,
                                                          "new value here",
                                                          ParameterDialog.newComment);
    }
  }

  /**
   * run the add state variable dialog
   */
  public void addStateVariableDialog()
  {
    if( StateVariableDialog.showDialog(this,this,null)) {      // blocks here
      ((ViskitController)getController()).newStateVariable(StateVariableDialog.newName,
                                                          StateVariableDialog.newType,
                                                          "new value here",
                                                          StateVariableDialog.newComment);
    }
  }

  /**
   * Do menu layout work here.  These menus, and the toggle buttons which follow, make use of the "actions"
   * package, which
   */

  private void setupMenus()
  {
    ViskitController controller = (ViskitController)getController();
    int accelMod = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    // Set up file menu
    fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    fileMenu.add(buildMenuItem(controller,"newEventGraph",    "New Event Graph", new Integer(KeyEvent.VK_N),
                                                               KeyStroke.getKeyStroke(KeyEvent.VK_N,accelMod)));
    fileMenu.add(buildMenuItem(controller,"newAssembly",      "New Assembly", null,null));
    fileMenu.add(buildMenuItem(controller,"open",             "Open", new Integer(KeyEvent.VK_O),
                                                               KeyStroke.getKeyStroke(KeyEvent.VK_O,accelMod)));
    fileMenu.add(buildMenuItem(controller,"save",             "Save", new Integer(KeyEvent.VK_S),
                                                               KeyStroke.getKeyStroke(KeyEvent.VK_S,accelMod)));
    fileMenu.add(buildMenuItem(controller,"saveAs",           "Save as...", new Integer(KeyEvent.VK_A),null));
    fileMenu.add(buildMenuItem(controller,"generateJavaClass","Generate Java Class",new Integer(KeyEvent.VK_G),null));
    fileMenu.addSeparator();;
    fileMenu.add(buildMenuItem(controller,"quit",             "Exit",new Integer(KeyEvent.VK_X),null));

    // Set up edit menu
    editMenu = new JMenu("Edit");
    editMenu.setMnemonic(KeyEvent.VK_E);
    // the next three are disabled until something is selected
    editMenu.add(buildMenuItem(controller,"cut",  "Cut",  new Integer(KeyEvent.VK_T),
                                                   KeyStroke.getKeyStroke(KeyEvent.VK_X,accelMod)));
    editMenu.add(buildMenuItem(controller,"copy", "Copy", new Integer(KeyEvent.VK_C),
                                                   KeyStroke.getKeyStroke(KeyEvent.VK_C,accelMod)));
    editMenu.add(buildMenuItem(controller,"paste","Paste",new Integer(KeyEvent.VK_P),
                                                   KeyStroke.getKeyStroke(KeyEvent.VK_V,accelMod)));

    // These 3 start off being disabled, until something is selected
    ActionIntrospector.getAction(controller,"cut").setEnabled(false);
    ActionIntrospector.getAction(controller,"copy").setEnabled(false);
    ActionIntrospector.getAction(controller,"paste").setEnabled(false);

    editMenu.addSeparator();

    editMenu.add(buildMenuItem(controller,"newNode",         "Add Event",            new Integer(KeyEvent.VK_E),null));
    editMenu.add(buildMenuItem(controller,"newStateVariable","Add State Variable...",new Integer(KeyEvent.VK_S),null));
    editMenu.add(buildMenuItem(controller,"newSimParameter", "Add vParameter...",     new Integer(KeyEvent.VK_M),null));

    // Set up simulation menu for controlling the simulation
    simulationMenu = new JMenu("Simulation");
    simulationMenu.setMnemonic(KeyEvent.VK_S);
    simulationMenu.add(buildMenuItem(controller,"eventList","Event List...",new Integer(KeyEvent.VK_L),null));

    // Create a new menu bar and add the menus we created above to it
    menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    menuBar.add(simulationMenu);

    this.setJMenuBar(menuBar);
  }

  // Use the actions package
  private JMenuItem buildMenuItem(Object source, String method, String name, Integer mn, KeyStroke accel)
  {
    Action a = ActionIntrospector.getAction(source,method);
    Map map = new HashMap();
    if(mn != null)
      map.put(Action.MNEMONIC_KEY,mn);
    if(accel != null)
      map.put(Action.ACCELERATOR_KEY,accel);
    if(name != null)
      map.put(Action.NAME,name);
    if(!map.isEmpty())
      ActionUtilities.decorateAction(a,map);

    return ActionUtilities.createMenuItem(a);
  }

  private JToggleButton makeJTButton(Action a, String icPath, String tt)
  {
    JToggleButton jtb;
    if(a != null)jtb = new JToggleButton(a);
    else jtb = new JToggleButton();
    jtb.setIcon(new ImageIcon(ClassLoader.getSystemResource(icPath)));
    jtb.setToolTipText(tt);
    jtb.setBorder(BorderFactory.createEtchedBorder());
    jtb.setText(null);
    return jtb;
  }

  private JLabel makeJLabel(String icPath, String tt)
  {
    JLabel jlab = new JLabel(new ImageIcon(ClassLoader.getSystemResource(icPath)));
    jlab.setToolTipText(tt);
    return jlab;
  }

  private void setupToolbar()
  {
    modeButtonGroup = new ButtonGroup();
    toolBar = new JToolBar();

    // Buttons for what mode we are in

    addMode = makeJLabel("simkit/viskit/images/eventNode.png",
                         "Drag onto canvas to add new nodes to the event graph");
    addMode.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(4,4,4,4)));


    selectMode    = makeJTButton(null, "simkit/viskit/images/selectNode.png",
                                       "Select items on the graph");
    arcMode       = makeJTButton(null, "simkit/viskit/images/schedArc.png",
                                       "Connect nodes with scheduling arcs");
    cancelArcMode = makeJTButton(null, "simkit/viskit/images/canArc.png",
                                       "Connect nodes with a canceling arc");

    modeButtonGroup.add(selectMode);
    modeButtonGroup.add(arcMode);
    modeButtonGroup.add(cancelArcMode);

    // Make selection mode the default mode
    selectMode.setSelected(true);

    toolBar.add(new JLabel("Add: "));
    toolBar.add(addMode);
    toolBar.addSeparator(new Dimension(24,24));
    toolBar.add(new JLabel("Mode: "));
    toolBar.add(selectMode);
    toolBar.addSeparator(new Dimension(5,24));
    toolBar.add(arcMode);
    toolBar.addSeparator(new Dimension(5,24));
    toolBar.add(cancelArcMode);

    addMode.setTransferHandler(new TransferHandler("text"));
    addMode.addMouseListener(new DragMouseAdapter());

    // These buttons perform operations that are internal to our view class, and therefore their operations are
    // not under control of the application controller (Controller.java).  Small, simple anonymous inner classes
    // such as these have been certified by the Surgeon General to be only minimally detrimental to code health.

    selectMode.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        graphPane.setPortsVisible(false);
      }
    });
    arcMode.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        graphPane.setPortsVisible(true);
      }
    });
    cancelArcMode.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        graphPane.setPortsVisible(true);
      }
    });

  }

  // Some private classes to implement dnd and dynamic cursor update
  class vCursorHandler extends MouseAdapter
  {
    Cursor select;
    Cursor arc;
    Cursor cancel;

    vCursorHandler()
    {
      super();
      select    = Cursor.getDefaultCursor();
      //select    = new Cursor(Cursor.MOVE_CURSOR);
      arc       = new Cursor(Cursor.CROSSHAIR_CURSOR);

      Image img = new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/canArcCursor.png")).getImage();
      cancel    = Toolkit.getDefaultToolkit().createCustomCursor(img,new Point(0,0),"CancelArcCursor");
    }

    public void mouseEntered(MouseEvent e)
    {
      switch(getCurrentMode()) {
        case  SELECT_MODE:
          graphPane.setCursor(select);
          break;
        case  ARC_MODE:
          graphPane.setCursor(arc);
          break;
        case CANCEL_ARC_MODE:
          graphPane.setCursor(cancel);
          break;
        default:
          //assert false : "vCursorHandler";
          System.err.println("assert false : \"vCursorHandler\"");
      }
    }
  }

  // Two classes to support dragging and dropping on the graph
  class DragMouseAdapter extends MouseAdapter
  {
    public void mousePressed(MouseEvent e)
    {
      JComponent c = (JComponent) e.getSource();
      TransferHandler handler = c.getTransferHandler();
      handler.exportAsDrag(c, e, TransferHandler.COPY);
    }
  }
  
  class vDropTargetAdapter extends DropTargetAdapter
  {
    public void drop(DropTargetDropEvent dtde)
    {
      Point p = dtde.getLocation();  // subtract the size of the label
      p.x -= addMode.getWidth();
      p.y -= addMode.getHeight();

      ((ViskitController)getController()).newNode(p);
    }
  }

  // ViskitView-required methods:
  private JFileChooser jfc;
  public File openFileAsk()
  //-----------------------
  {
    if (jfc == null)
      jfc = new JFileChooser(System.getProperty("user.dir"));

    int retv = jfc.showOpenDialog(this);
    if (retv == JFileChooser.APPROVE_OPTION)
      return jfc.getSelectedFile();
    return null;
  }

  public File saveFileAsk()
  //-----------------------
  {
    if(jfc == null)
      jfc = new JFileChooser(System.getProperty("user.dir"));

    int retv = jfc.showSaveDialog(this);
    if(retv == JFileChooser.APPROVE_OPTION)
      return jfc.getSelectedFile();
    return null;
  }

  public void fileName(String s)
  //----------------------------
  {
    this.setTitle("Viskit: "+s);
  }

  public boolean doEditNode(EventNode node)
  //---------------------------------------
  {
    return EventInspectorDialog.showDialog(this,this.graphPane,node); // blocks
  }

  public boolean doEditEdge(SchedulingEdge edge)
  //--------------------------------------------
  {
    return EdgeInspectorDialog.showDialog(this,this.graphPane,edge); // blocks
  }

  public boolean doEditCancelEdge(CancellingEdge edge)
  //--------------------------------------------------
  {
    return EdgeInspectorDialog.showDialog(this,this.graphPane,edge); // blocks
  }

  public boolean doEditParameter(vParameter param)
  {
    return ParameterDialog.showDialog(this,this.graphPane,param);    // blocks
  }
  public boolean doEditStateVariable(vStateVariable var)
  {
    return StateVariableDialog.showDialog(this,this.graphPane,var);
  }

  public boolean doMetaGraphEdit(GraphMetaData gmd)
  //-----------------------------------------------
  {
    return MetaDataDialog.showDialog(this,this.graphPane,gmd);
  }

  public int genericAsk(String title, String msg)
  //---------------------------------------------
  {
    return JOptionPane.showConfirmDialog(this,msg,title,JOptionPane.YES_NO_OPTION);
  }

  public void genericErrorReport(String title, String msg)
  //-----------------------------------------------------
  {
    JOptionPane.showMessageDialog(this,msg,title,JOptionPane.ERROR_MESSAGE);
  }

  public String promptForStringOrCancel(String title, String message, String initval)
  //---------------------------------------------------------------------------------
  {
    return (String)JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE,
                                               null, null, initval);
  }

  /**
   * This is where the "master" model (simkit.viskit.model.Model) updates the view.
   * @param event
   */
  public void modelChanged(mvcModelEvent event)
  //-------------------------------------------
  {
    switch(event.getID())
    {
      // Changes the two side panels need to know about
      case ModelEvent.SIMPARAMETERADDED:
        pp.addRow(event.getSource());
        break;
      case ModelEvent.SIMPARAMETERDELETED:
        pp.removeRow(event.getSource());
        break;
      case ModelEvent.SIMPARAMETERCHANGED:
        pp.updateRow(event.getSource());
        break;

      case ModelEvent.STATEVARIABLEADDED:
        vp.addRow(event.getSource());
        break;
      case ModelEvent.STATEVARIABLEDELETED:
        vp.removeRow(event.getSource());
        break;
      case ModelEvent.STATEVARIABLECHANGED:
        vp.updateRow(event.getSource());
        break;

      case ModelEvent.NEWMODEL:
        vp.setData(null);
        pp.setData(null);
        // fall through

      // Changes the graph needs to know about
      default:
        this.graphPane.viskitModelChanged((ModelEvent)event);

    }
  }

}



