package simkit.actions.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageView extends JScrollPane {

    private ImageIcon icon;
    private Image originalImage;
    private Dimension originalSize;
    private JPanel panel;
    private final JLabel iconLabel;
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
         model.addChangeListener(new ChangeListener() {
             @Override
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
         });

    }

    public JLabel getIconLabel() {return iconLabel;}
} 