
package simkit.viskit;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * some prototyping
 */
 
 public class EventGraphFrame extends JFrame
 {
   // Constants--menu item positions.
   
   // File menu
   private final static int NEW_EVENT_GRAPH_INDEX = 0;
   private final static int NEW_ASSEMBLY_INDEX    = 1;
   private final static int OPEN_INDEX            = 2;
   private final static int SAVE_INDEX            = 3;
   private final static int SAVE_AS_INDEX         = 4;
   private final static int GENERATE_JAVA         = 5;
   
   // Edit menu
   private final static int CUT_INDEX           = 0;
   private final static int COPY_INDEX          = 1;
   private final static int PASTE_INDEX         = 2; // index 3 is the menu separator
   private final static int ADD_STATE_INDEX     = 4;
   private final static int ADD_PARAMETER_INDEX = 5;
   
   // Modes we can be in--selecting items, adding nodes to canvas, drawing arcs, etc.
   public final static int SELECT_MODE     = 0;
   public final static int ADD_NODE_MODE   = 1;
   public final static int ARC_MODE        = 2;
   public final static int CANCEL_ARC_MODE = 3;
   public final static int VIEW_ONLY_MODE  = 4;
   
   /** Menu bar for the application */
   private JMenuBar menuBar;
   
   // We keep a reference to the menus in case we have to turn menu
   // items on or off
   
   /** File menu */
   private JMenu    fileMenu;
   
   /** edit menu */
   private JMenu    editMenu;
   
   /** Simulation menu */
   private JMenu simulationMenu;
   
   /** State variables pane */
   private JPanel stateVariablesPanel;
   
   /** Parameters pane */
   private JPanel parametersPanel;
   
   /** Panel that holds both state variables and parameters panels */
   private JPanel stateAndParametersPanel;
   
   /** canvas/drawing pane */
   private JPanel graphPane;
   
   /** main split pane, with left-hand drawing pane & state variables on right */
   private JSplitPane stateDrawingSplit;
   
   /** Secondary split pane, with state variables on the top and parameters on the bottom */
   private JSplitPane stateParameterSplit;
   
   /** Toolbar for dropping icons, connecting, etc. */
   private JToolBar toolBar;
   
   /** Model for the state variables table */
   private StateVariableTableModel stateVariableTableModel;
   
   /** Parameters table model */
   private ParameterTableModel parameterTableModel;
   
   /** Button group that holds the mode buttons. */
   private ButtonGroup modeButtonGroup;
   
   // Mode buttons on the toolbar
   private JToggleButton selectMode;
   private JToggleButton addMode;
   private JToggleButton arcMode;
   private JToggleButton cancelArcMode;
   
   
   /** Whether or not we can edit this event graph */
    private boolean isEditable;

   /**
    * Constructor; lays out initial GUI objects
    */
   public EventGraphFrame()
   {
     this.initUI();
     this.setSize(700, 400);
     this.setEditable(true);
   }
   
   /**
    * Constructor; takes the reader as an argument, which is used
    * to hide the details of XML reading. The EventGraphReader
    * is responsible for parsing the XML file; we query that
    * object to retrieve the data and fill out the parameter and
    * state variable data.
    *
    * @param reader Object responsible for supplying us with data
    * @param pIsEditable true if this event graph is editable
    */
   public EventGraphFrame(EventGraphXMLReader reader, boolean pIsEditable)
   {
     this();         // Run the standard constructor
     
     this.setEditable(pIsEditable);
   }
   
   /**
    * Sets whether or not we can edit this event graph. Also modifies
    * the GUI to the desired state.
    */
   public void setEditable(boolean pIsEditable)
   {
     isEditable = pIsEditable;
     
     selectMode.setEnabled(isEditable);
     addMode.setEnabled(isEditable);
     arcMode.setEnabled(isEditable);
     cancelArcMode.setEnabled(isEditable);
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
     
     if(selectMode.isSelected() == true) return SELECT_MODE;
     if(addMode.isSelected() == true)    return ADD_NODE_MODE;
     if(arcMode.isSelected() == true)    return ARC_MODE;
     if(arcMode.isSelected() == true)    return CANCEL_ARC_MODE;
     
     // If none of them are selected we're presumably in the "view-only" mode.
     
     return VIEW_ONLY_MODE;
   }
   
   /**
    * Returns an array of strings that contain all the existing parameter and
    * state variable names. This can be used to make sure we don't create
    * duplicate names.
    */
   private java.util.List getExistingNames()
   {
     java.util.List parameterNames, stateVariableNames, allNames;
     
     parameterNames     = parameterTableModel.getNames();
     stateVariableNames = stateVariableTableModel.getNames();
     allNames = new Vector();
     
     allNames.addAll(parameterNames);
     allNames.addAll(stateVariableNames);
     
     return allNames;
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
     graphPane = new JPanel();
     
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
     stateDrawingSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphPane, stateParameterSplit);

     top.add(stateDrawingSplit, BorderLayout.CENTER);
     
     this.getContentPane().add(top);
     stateDrawingSplit.setDividerLocation(500);
     stateParameterSplit.setDividerLocation(150);
   }
   
   /**
    * Open a file. Puts up a standard file dialog.
    */
   private void fileOpen()
   {
     JFileChooser fileChooser = new JFileChooser();
     // Add a subclass of FileFilter here to limit the types of files
     // that are choosable.
     
     int returnValue = fileChooser.showOpenDialog(this);
     if(returnValue ==  JFileChooser.APPROVE_OPTION) // User selected OK
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
     if(parameterDialog.getButtonChosen() == ParameterDialog.CANCEL_CHOSEN)
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
       // Set up file menu
     fileMenu = new JMenu("File");
     fileMenu.add(new JMenuItem("New Event Graph"));
     fileMenu.add(new JMenuItem("New Assembly"));
     fileMenu.add(new JMenuItem("Open"));
     fileMenu.add(new JMenuItem("Save"));
     fileMenu.add(new JMenuItem("Save as..."));
     fileMenu.add(new JMenuItem("Generate Java Class"));
     
     // Assorted event handling for the above menu items
     JMenuItem aMenuItem = fileMenu.getItem(OPEN_INDEX);
     aMenuItem.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
          fileOpen();
       }
     });
     
     // New EventGraph action
     aMenuItem = fileMenu.getItem(NEW_EVENT_GRAPH_INDEX);
     aMenuItem.addActionListener(new ActionListener()
     {
         public void actionPerformed(ActionEvent e)
         {
             EventGraphFrame eventGraph = new EventGraphFrame();
         }
     });
     
     // Set up edit menu
     editMenu = new JMenu("Edit");
     editMenu.add(new JMenuItem("Cut"));
     editMenu.add(new JMenuItem("Copy"));
     editMenu.add(new JMenuItem("Paste"));
     editMenu.addSeparator();
     editMenu.add(new JMenuItem("Add State Variable..."));
     editMenu.add(new JMenuItem("Add Parameter..."));
     
     // Assorted action listeners for the edit menu items
     
     // Add parameter dialog
     aMenuItem = editMenu.getItem(ADD_PARAMETER_INDEX);
     aMenuItem.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         addParameterDialog();
       }
      });
      
      // Add state variable dialog
     aMenuItem = editMenu.getItem(ADD_STATE_INDEX);
     aMenuItem.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
         addStateVariableDialog();
       }
      });
      
     
     // Set up simulation menu for controlling the simulation
     simulationMenu = new JMenu("Simulation");
     simulationMenu.add(new JMenuItem("Event List..."));

     // Create a new menu bar and add the menus we created above to it
     menuBar = new JMenuBar();
     menuBar.add(fileMenu);
     menuBar.add(editMenu);
     menuBar.add(simulationMenu);
     this.setJMenuBar(menuBar);
  }
  
  private void setupToolbar()
  {
      modeButtonGroup = new ButtonGroup();
      
     // Set up toolbar
     toolBar = new JToolBar();

     // Buttons for what mode we are in
     selectMode = new JToggleButton(new ImageIcon(EventGraphFrame.class.getResource("images/select.jpg" )));
     selectMode.setToolTipText("Select items on the graph");

     addMode = new JToggleButton(new ImageIcon(EventGraphFrame.class.getResource("images/node.jpg" )));
     addMode.setToolTipText("Add new nodes to the event graph");

     arcMode = new JToggleButton(new ImageIcon(EventGraphFrame.class.getResource("images/connect.jpg" )));
     arcMode.setToolTipText("Connect nodes with scheduling arcs");

     cancelArcMode = new JToggleButton(new ImageIcon(EventGraphFrame.class.getResource("images/cancel.jpg" )));
     cancelArcMode.setToolTipText("Connect nodes with a canceling arc");
     
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
    * Entry point.
    */
   public static void main(String args[])
   {
     EventGraphFrame gui = new EventGraphFrame();
     gui.setVisible(true);
   }
   
}
   
   
   
