package simkit.actions.test;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ImageView extends JScrollPane {

    private ImageIcon icon;
    private Image originalImage;
    private Dimension originalSize;
    private JPanel panel;
    private JLabel iconLabel;
    public ImageView(ImageIcon i, BoundedRangeModel model) {
         this.icon = i;
         this.originalImage = icon.getImage();
         panel = new JPanel();
         panel.setLayout(new BorderLayout());
         iconLabel = new JLabel(icon);
         iconLabel.setBorder(null);
         panel.add(iconLabel, BorderLayout.CENTER);
         originalSize = new Dimension();

         setViewportView(panel);
         originalSize.width = icon.getIconWidth();
         originalSize.height = icon.getIconHeight();
         model.addChangeListener(
             new ChangeListener() {
                 public void stateChanged(ChangeEvent e) {
                     BoundedRangeModel mod =
                                   (BoundedRangeModel) e.getSource();
//                      if (!mod.getValueIsAdjusting()) {
                         double multiplier = (double) mod.getValue() /
                                   (mod.getMaximum() - mod.getMinimum());
                         multiplier = Math.max(multiplier, 0.1);

                         Image scaled = originalImage.getScaledInstance(
                                            (int) (originalSize.width * multiplier),
                                            (int) (originalSize.height * multiplier),
                                            Image.SCALE_AREA_AVERAGING
                                        );
                         icon.setImage(scaled);
                         panel.revalidate();
//                         iconLabel.repaint();    // This is necessary because
                                                 // Swing is not hearing the
                                                 // JPanel.revalidate() in the
                                                 // previous line...feature or bug??
//                      }
                 }
             }
         );

    }

    public JLabel getIconLabel() {return iconLabel;}
} 