package simkit.animate;

import simkit.actions.ActionIntrospector;
import simkit.actions.ActionUtilities;
import java.beans.Beans;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.SimEventSource;

/**
 * 
 * @author ahbuss
 */

public class VCRControlPanel extends JPanel {
    
    private VCRController controller;
    private JTextField simTimeLabel;
    private JButton toggleVerbose;
    private JButton[] buttons;
    private Map[] decorate;
    private boolean labelVisible;
    
    private static final String[] VERBOSE_LABEL = new String[] {"To Verbose", "To Quiet"};
    private static final String[] VERBOSE_TOOLTIP = new String[] {"Press to switch to Verbose mode",
    "Press to turn off Verbose mode"};
    
    public VCRControlPanel() {
        this(new PingThread());
    }
    
    public VCRControlPanel(VCRController controller) {
        
        this.controller = controller;
        if (Beans.isInstanceOf(controller, simkit.SimEventSource.class)) {
            ((SimEventSource) controller).addSimEventListener(new UpdateTimeLabel());
        }
        
        Action[] actions = ActionIntrospector.getActions(controller, new String[] {"pause",  "stop", "step", "start"});
        
        buttons = ActionUtilities.createButtons(actions);
        for (JButton button : buttons) {
            this.add(button);
        }
        
        setupTimLabel();
        toggleVerbose = ActionUtilities.createButton(this, "toggleVerbose");
        int index = Schedule.isVerbose() ? 1 : 0;
        toggleVerbose.setText(VERBOSE_LABEL[index]);
        toggleVerbose.setToolTipText(VERBOSE_TOOLTIP[index]);
        
    }
    
    protected void setupTimLabel() {
        simTimeLabel = new JTextField(Schedule.getSimTimeStr());
        simTimeLabel.setColumns(Schedule.getSimTimeStr().length() + 2);
        simTimeLabel.setEditable(false);
        simTimeLabel.setBorder(null);
        simTimeLabel.setOpaque(false);
        simTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        simTimeLabel.setToolTipText("Current value of Simulated time");
        
        this.add(simTimeLabel);
    }
    
    public void addVerboseButton() {
        this.add(toggleVerbose, 0);
    }
    
    public void removeVerboseButton() {
        this.remove(toggleVerbose);
    }
    
    public void toggleVerbose() {
        Schedule.setVerbose(!Schedule.isVerbose());
        int index = Schedule.isVerbose() ? 1 : 0;
        toggleVerbose.setText(VERBOSE_LABEL[index]);
        toggleVerbose.setToolTipText(VERBOSE_TOOLTIP[index]);
    }
    
    public VCRController getController() { return controller; }
    
    public void setLabelVisible(boolean v) {
        labelVisible = v;
    }
    
    public boolean isLabelVisible() { return labelVisible; }
    
    public void updateTime() {
        this.simTimeLabel.setText(Schedule.getSimTimeStr());
        this.revalidate();
    }
    
    public class UpdateTimeLabel extends SimEntityBase {
        
        @Override
        public void reset() {
            super.reset();
            updateTime();
        }
        
        public void doPing() {
            updateTime();
        }
        
    }
}