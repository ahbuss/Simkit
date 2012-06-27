package simkit.actions.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import simkit.actions.MyFrame;


public class MultipleViews extends MyFrame {
    
    private DefaultBoundedRangeModel model;
    private JSlider slider;
    private JLabel readOut;
    private ImageIcon image;
    private ImageView imageView;
    private JPanel topPanel;
    
    public MultipleViews() {
        super("Multiple Views");
        
        model = new DefaultBoundedRangeModel(100, 0, 0, 100);
        slider = new JSlider(model);
        readOut = new JLabel("100%");
        image = new ImageIcon(MultipleViews.class.getResource("simkit/actions/icons/scenarioMap3.gif"));
        imageView = new ImageView(image, model);
        setStatus("100%");
        
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Set Image Size"));
        topPanel.add(slider);
        topPanel.add(readOut);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        
        getContentPane().add(imageView, BorderLayout.CENTER);
        topPanel.revalidate();
        
        this.setSize(image.getIconWidth() + 40, image.getIconHeight() + 100);
        
        model.addChangeListener(
        new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                String s =  ((BoundedRangeModel)e.getSource()).getValue() + "%";
                readOut.setText(s);
                readOut.revalidate();
                imageView.repaint();
                setStatus(s);
            }
        }
        );
    }
    
    public static void main(String[] args) {
        new MultipleViews().setVisible(true);;
    }
}