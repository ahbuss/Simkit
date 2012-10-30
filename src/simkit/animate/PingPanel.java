package simkit.animate;

import java.awt.FlowLayout;
import javax.swing.*;
import simkit.Schedule;
import simkit.SimEvent;
import simkit.SimEventListener;
import simkit.actions.GenericAction;

public class PingPanel extends JPanel implements SimEventListener {

    private JTextField simTimeLabel;
    private JButton rewind;
    private JButton start;
    private JButton step; // 22 Nay 2004 - kas - added
    private JButton stop;
    private JButton pause;
    private JButton resume;
    private JButton exit;
    private JButton toggleVerbose;
    
    private VCRController vcrController;
    private static String[] verboseLabel = new String[]{"To Verbose", "To Quiet"};
    private static String[] verboseTooltip = new String[]{"Press to switch to Verbose mode",
        "Press to turn off Verbose mode"};

    public PingPanel() {
        this(new PingThread());
    }

    public PingPanel(VCRController controller) {
        this.setVcrController(vcrController);
        rewind = new JButton(new ImageIcon(
                Thread.currentThread().getContextClassLoader().getResource("simkit/animate/icons/Rewind24.gif")));
        rewind.addActionListener(new GenericAction(controller, "rewind"));
        rewind.setToolTipText("Rewind");

        start = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("simkit/animate/icons/Play24.gif")));
        start.addActionListener(new GenericAction(controller, "start"));
        start.setToolTipText("Start Pinging");

        // 22 May 2004 - kas - added
        step = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("simkit/animate/icons/StepForward24.gif")));
        step.addActionListener(new GenericAction(controller, "step"));
        step.setToolTipText("Step the simulation");
        // 22 May 2004 - kas - end added

        stop = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("simkit/animate/icons/Pause24.gif")));
        stop.addActionListener(new GenericAction(controller, "stop"));
        stop.setToolTipText("Stop Pinging");

        pause = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("simkit/animate/icons/Stop24.gif")));
        pause.addActionListener(new GenericAction(controller, "pause"));
        pause.setToolTipText("Pause Simulation");

        exit = new JButton("Exit");
        exit.addActionListener(new GenericAction(this, "exit"));
        exit.setToolTipText("Exit...Duh!");

        toggleVerbose = new JButton(
                new GenericAction(this, "toggleVerbose"));
        toggleVerbose.setText(verboseLabel[0]);
        toggleVerbose.setToolTipText(verboseTooltip[0]);


        simTimeLabel = new JTextField(Schedule.getSimTimeStr());
        simTimeLabel.setColumns(Schedule.getSimTimeStr().length() + 2);
        simTimeLabel.setEditable(false);
        simTimeLabel.setBorder(null);
        simTimeLabel.setOpaque(false);
        simTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        simTimeLabel.setToolTipText("Current value of Simulated time");

        JPanel buttons = new JPanel();
        this.setLayout(new FlowLayout());
        this.add(rewind);
        this.add(stop);
        this.add(pause);
        this.add(step); // kas
        this.add(start);
        this.add(simTimeLabel);
        
        controller.addSimEventListener(this);

        Schedule.reset();
    }

    public void exit() {
        int userSelects =
                JOptionPane.showConfirmDialog(SwingUtilities.getRoot(PingPanel.this),
                "Do you really want to quit?");
        if (userSelects == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    public void addVerboseButton() {
        this.add(toggleVerbose, 0);
    }

    public void removeVerboseButton() {
        this.remove(toggleVerbose);
    }

    public void rewind() {
        Schedule.stopSimulation();
        Schedule.reset();
        this.simTimeLabel.setText(Schedule.getSimTimeStr());
        this.getRootPane().repaint();
    }

    public void processSimEvent(SimEvent e) {
        if (e.getEventName().equals("Ping")) {
            this.simTimeLabel.setText(Schedule.getSimTimeStr());
            this.simTimeLabel.setColumns(simTimeLabel.getText().length() + 2);
        }
    }

    public void addExitLabel() {
        this.add(exit);
    }

    public void removeExitLabel() {
        this.remove(exit);
    }

    public void toggleVerbose() {
        Schedule.setVerbose(!Schedule.isVerbose());
        int index = Schedule.isVerbose() ? 1 : 0;
        toggleVerbose.setText(verboseLabel[index]);
        toggleVerbose.setToolTipText(verboseTooltip[index]);
    }

    /**
     * @return the vcrController
     */
    public VCRController getVcrController() {
        return vcrController;
    }

    /**
     * @param vcrController the vcrController to set
     */
    public void setVcrController(VCRController vcrController) {
        this.vcrController = vcrController;
    }
}