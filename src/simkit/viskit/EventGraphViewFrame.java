package simkit.viskit;

import simkit.viskit.mvc.mvcAbstractJFrameView;
import simkit.viskit.mvc.mvcModelEvent;
import simkit.viskit.jgraph.vGraphComponent;
import simkit.viskit.jgraph.vGraphModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.util.Vector;

/**
 * Main "view" of the Viskit app.
 */

public class EventGraphViewFrame extends mvcAbstractJFrameView
{
  // Modes we can be in--selecting items, adding nodes to canvas, drawing arcs, etc.
  public final static int SELECT_MODE = 0;
  public final static int ADD_NODE_MODE = 1;
  public final static int ARC_MODE = 2;
  public final static int CANCEL_ARC_MODE = 3;

  /**
   * Model/View/Controller segregation
   */
  // held in super class: private Controller controller;
  // held in super class: private Model      model;

  /**
   * Menu bar for the application
   */
  private JMenuBar menuBar;

  // We keep a reference to the menus in case we have to turn menu
  // items on or off

  /**
   * File menu
   */
  private JMenu fileMenu;

  /**
   * edit menu
   */
  private JMenu editMenu;

  /**
   * Simulation menu
   */
  private JMenu simulationMenu;

  /**
   * State variables pane
   */
  private JPanel stateVariablesPanel;

  /**
   * Parameters pane
   */
  private JPanel parametersPanel;

  /**
   * Panel that holds both state variables and parameters panels
   */
  private JPanel stateAndParametersPanel;

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
   * Model for the state variables table
   */
  private StateVariableTableModel stateVariableTableModel;

  /**
   * Parameters table model
   */
  private ParameterTableModel parameterTableModel;

  /**
   * Button group that holds the mode buttons.
   */
  private ButtonGroup modeButtonGroup;

  // Mode buttons on the toolbar
  private JToggleButton selectMode;
  private JToggleButton addMode;
  private JToggleButton arcMode;
  private JToggleButton cancelArcMode;


  /**
   * Whether or not we can edit this event graph
   */
  private boolean isEditable;

  /**
   * Constructor; lays out initial GUI objects
   */
  public EventGraphViewFrame(Model mod, Controller ctrl)
  {
    super("Viskit");

    this.initMVC(mod,ctrl);   // set up mvc linkages
    this.initUI();            // build widgets

    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    this.setLocation((d.width - 800) / 2, (d.height - 600) / 2);
    this.setSize(800, 600);

    this.setEditable(true);

    // Won't want to do this in final version...need to check for save
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  /**
   * Constructor; takes the reader as an argument, which is used
   * to hide the details of XML reading. The EventGraphReader
   * is responsible for parsing the XML file; we query that
   * object to retrieve the data and fill out the parameter and
   * state variable data.
   *
   * @param reader      Object responsible for supplying us with data
   * @param pIsEditable true if this event graph is editable
   */

  /*   rethink this
  public EventGraphViewFrame(EventGraphXMLReader reader, boolean pIsEditable)
  {
    this(null);         // Run the standard constructor

    this.setEditable(pIsEditable);
  }
  */

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
    if (addMode.isSelected() == true) return ADD_NODE_MODE;
    if (arcMode.isSelected() == true) return ARC_MODE;
    if (arcMode.isSelected() == true) return CANCEL_ARC_MODE;

    // If none of them are selected we're in serious trouble.

    return 0;
  }

  /**
   * Returns an array of strings that contain all the existing parameter and
   * state variable names. This can be used to make sure we don't create
   * duplicate names.
   */
  private java.util.List getExistingNames()
  {
    java.util.List parameterNames, stateVariableNames, allNames;

    parameterNames = parameterTableModel.getNames();
    stateVariableNames = stateVariableTableModel.getNames();
    allNames = new Vector();

    allNames.addAll(parameterNames);
    allNames.addAll(stateVariableNames);

    return allNames;
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
    graphPane = new vGraphComponent(new vGraphModel());

    // Set up JTable to hold state variable data
    stateVariableTableModel = new StateVariableTableModel();

    // combo box of types
    JComboBox types = new JComboBox();
    types.addItem("Integer");
    types.addItem("Float");
    types.addItem("Double");
    types.addItem("String");
    types.setEditable(true);  // Make this editable so they can add new, arbitrary types

    JTable stateVariableTable = new JTable(stateVariableTableModel);
    TableColumn column = stateVariableTable.getColumnModel().getColumn(1);
    column.setCellEditor(new DefaultCellEditor(types));

    stateVariablesPanel = new JPanel();
    JScrollPane stateVariablesScrollPane = new JScrollPane(stateVariableTable);
    stateVariablesPanel.setLayout(new BorderLayout());
    stateVariablesPanel.add(new JLabel("State Variables"), BorderLayout.NORTH);
    stateVariablesPanel.add(stateVariablesScrollPane, BorderLayout.CENTER);

    // Set up editor for the Name column. We check input so that we don't have
    // to variables each named the same thing, etc.

    //stateVariableTable.getModel().getColumn(0).setCellEditor(new VariableNameEditor());



    // Set up parameter table; the panel as a whole has a label at the top
    // and a scrollpane underneath.
    parametersPanel = new JPanel();
    parametersPanel.setLayout(new BorderLayout());
    parametersPanel.add(new JLabel("Parameters"), BorderLayout.NORTH);

    parameterTableModel = new ParameterTableModel();
    JTable parametersTable = new JTable(parameterTableModel);

    column = parametersTable.getColumnModel().getColumn(1);
    column.setCellEditor(new DefaultCellEditor(types));

    JScrollPane parametersScrollPane = new JScrollPane(parametersTable);
    parametersPanel.add(parametersScrollPane, BorderLayout.CENTER);


    // Split pane that has state variables on top, parameters on bottom.
    stateParameterSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, stateVariablesPanel, parametersPanel);

    // Split pane with the canvas on the left and a split pane with state variables and parameters on the right.
    stateDrawingSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(graphPane), stateParameterSplit);

    top.add(stateDrawingSplit, BorderLayout.CENTER);

    this.getContentPane().add(top);
    stateDrawingSplit.setDividerLocation(500);
    stateParameterSplit.setDividerLocation(150);
  }

  /**
   * Open a file. Puts up a standard file dialog.
   * Make public so controller can use it.  Controller decides what should be done,
   * uses this method as part of the current "view".
   * See Controller.OpenAction to see a fuller exposition of what we will normally
   * want to do on an "open".
   */
  public void fileOpen()
  {
    JFileChooser fileChooser = new JFileChooser();
    // Add a subclass of FileFilter here to limit the types of files
    // that are choosable.

    int returnValue = fileChooser.showOpenDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) // User selected OK
    {
      File selectedFile = fileChooser.getSelectedFile();
      System.out.println("Chose file " + selectedFile.toString());
    }
  }

  /**
   * run the add parameter dialog
   */
  public void addParameterDialog()
  {
    ParameterDialog parameterDialog = new ParameterDialog(this, this.getExistingNames());
    String parameterName;
    String parameterType;

    parameterDialog.setVisible(true);

    // User hit cancel? Bail without adding anything.
    if (parameterDialog.getButtonChosen() == ParameterDialog.CANCEL_CHOSEN)
      return;

    // Retrieve the parameter name and type from the dialog. These items
    // were error-checked in the parameter dialog.

    parameterName = parameterDialog.getParameterName();
    parameterType = parameterDialog.getParameterType();

    // Add to the model
    Parameter newParameter = new Parameter(parameterName, parameterType);
    parameterTableModel.addParameter(newParameter);
  }

  /**
   * run the add state variable dialog
   */
  public void addStateVariableDialog()
  {
    StateVariableDialog stateVariableDialog = new StateVariableDialog(this, this.getExistingNames());
    String name;
    String type;
    String initialValue;

    stateVariableDialog.setVisible(true);

    // User hit cancel? Bail without adding anything.
    if(stateVariableDialog.getButtonChosen() == StateVariableDialog.CANCEL_CHOSEN)
      return;

    // Retrieve the state variable name and type from the dialog. These items
    // were error-checked in the parameter dialog.

    name = stateVariableDialog.getStateVariableName();
    type = stateVariableDialog.getType();
    initialValue = stateVariableDialog.getInitialValue();

    // Add to the model
    StateVariable stateVariable = new StateVariable(name, type, initialValue);
    stateVariableTableModel.addStateVariable(stateVariable);
  }

  /**
   * Do menu layout work here.
   */
  private void setupMenus()
  {
    Controller controller = (Controller)getController();

    // Set up file menu
    fileMenu = new JMenu("File");

    fileMenu.add(buildMenuItem(controller.NEWEVENTGRAPH,    "New Event Graph"));
    fileMenu.add(buildMenuItem(controller.NEWASSEMBLY,      "New Assembly"));
    fileMenu.add(buildMenuItem(controller.OPEN,             "Open"));
    fileMenu.add(buildMenuItem(controller.SAVE,             "Save"));
    fileMenu.add(buildMenuItem(controller.SAVEAS,           "Save as..."));
    fileMenu.add(buildMenuItem(controller.GENERATEJAVACLASS,"Generate Java Class"));
    fileMenu.addSeparator();;
    fileMenu.add(buildMenuItem(controller.QUIT,"Quit"));

    // Set up edit menu
    editMenu = new JMenu("Edit");
    editMenu.add(buildMenuItem(controller.CUT,  "Cut"));
    editMenu.add(buildMenuItem(controller.COPY, "Copy"));
    editMenu.add(buildMenuItem(controller.PASTE,"Paste"));
    editMenu.addSeparator();
    editMenu.add(buildMenuItem(controller.ADDSTATEVARIABLE,"Add State Variable..."));
    editMenu.add(buildMenuItem(controller.ADDPARAMETER,    "Add Parameter..."));

    // Set up simulation menu for controlling the simulation
    simulationMenu = new JMenu("Simulation");
    simulationMenu.add(buildMenuItem(controller.EVENTLIST,"Event List..."));

    // Create a new menu bar and add the menus we created above to it
    menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    menuBar.add(simulationMenu);
    this.setJMenuBar(menuBar);
  }

  private JMenuItem buildMenuItem(Action a, String txt)
  {
    JMenuItem mi = new JMenuItem(a);
    mi.setText(txt);
    return mi;
  }

  private JToggleButton makeJTButton(Action a, String icPath, String tt)
  {
    JToggleButton jtb = new JToggleButton(a);
    jtb.setIcon(new ImageIcon(ClassLoader.getSystemResource(icPath)));
    jtb.setToolTipText(tt);
    return jtb;
  }

  private void setupToolbar()
  {
    modeButtonGroup = new ButtonGroup();

    // Set up toolbar
    toolBar = new JToolBar();

    Controller controller = (Controller)getController();

    // Buttons for what mode we are in
    selectMode = makeJTButton(controller.SELECTMODE,
                              "images/select.jpg",
                              "Select items on the graph");

    addMode    = makeJTButton(controller.NEWNODEMODE,
                              "images/node.jpg",
                              "Add new nodes to the event graph");

    arcMode    = makeJTButton(controller.ARCMODE,
                              "images/connect.jpg",
                              "Connect nodes with scheduling arcs");

    cancelArcMode = makeJTButton(controller.CANCELARCMODE,
                                 "images/cancel.jpg",
                                 "Connect nodes with a canceling arc");

    modeButtonGroup.add(selectMode);
    modeButtonGroup.add(addMode);
    modeButtonGroup.add(arcMode);
    modeButtonGroup.add(cancelArcMode);

    // Make selection mode the default mode
    selectMode.setSelected(true);

    toolBar.add(selectMode);
    toolBar.add(addMode);
    toolBar.add(arcMode);
    toolBar.add(cancelArcMode);
  }

  /**
   * Returns the model that controls the parameter table
   */
  public ParameterTableModel getParameterTableModel()
  {
    return parameterTableModel;
  }

  /**
   * Returns the model that controls the table with state variables.
   */
  public StateVariableTableModel getStateVariableTableModel()
  {
    return stateVariableTableModel;
  }


  /**
   * This is where the "master" model (simkit.viskit.Model) updates the view.
   * @param event
   */
  public void modelChanged(mvcModelEvent event)
  {
    System.out.println("EventGraphViewFrame got report from model:");
    System.out.println("obj "+event.getSource());
    System.out.println("id "+event.getID());
    System.out.println("msg "+event.getActionCommand());

    // This is where updates to the view happen
    // Do changes to Swing widgets here...

    // Then, typically the jgraph component gets this information
    // to update the graph.
    this.graphPane.viskitModelChanged((ModelEvent)event);
  }

}



