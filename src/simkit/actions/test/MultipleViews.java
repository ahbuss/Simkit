package simkit.actions.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.WindowListener;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import simkit.actions.AppCloser;
import simkit.actions.MyFrame;

public class MultipleViews extends MyFrame {

    private final DefaultBoundedRangeModel model;
    private final JSlider slider;
    private JLabel readOut;
    private final ImageIcon image;
    private ImageView imageView;
    private final JPanel topPanel;

    public MultipleViews() {
        super("Multiple Views");

        model = new DefaultBoundedRangeModel(100, 0, 0, 100);
        slider = new JSlider(model);
        readOut = new JLabel("100%");
        image = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("simkit/actions/icons/scenarioMap3.gif"));
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
            @Override
            public void stateChanged(ChangeEvent e) {
                String s = ((BoundedRangeModel) e.getSource()).getValue() + "%";
                readOut.setText(s);
                readOut.revalidate();
                imageView.repaint();
                setStatus(s);
            }
        }
        );
    }

    public static void main(String[] args) {
        MultipleViews multipleViews = new MultipleViews();
        ImageIcon icon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("simkit/actions/icons/radar.gif"));
        multipleViews.setIconImage(icon.getImage());
        EventQueue.invokeLater(() -> {
            multipleViews.setVisible(true);
        });

    }
}
