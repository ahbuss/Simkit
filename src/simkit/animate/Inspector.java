package simkit.animate;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import simkit.smd.Mover;

/**
 *
 * @author  ahbuss
 */
public class Inspector implements MouseListener {
    
    private final JTable table;
    private final JTextArea text;
    private final JScrollPane scrollPane;
    
    /** Creates new Inspector */
    public Inspector() {
        table = new JTable();
        text = new JTextArea();
        scrollPane = new JScrollPane(text);
    }
    
    public String introspect(Object obj) {
        StringBuilder buf = new StringBuilder();
//        buf.append(obj);
//        buf.append('\n');
        BeanInfo bi = null;
        try {
            bi= Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) { System.err.println(e); }
        PropertyDescriptor[] pd = bi.getPropertyDescriptors();
        for (int i = 0; i < pd.length; i++) {
            String name = pd[i].getName();
            Method getter = pd[i].getReadMethod();
            Object value = null;
            try {
                value = getter.invoke(obj, (Object[]) null);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException t) {System.err.println(t);}
            buf.append(name);
            buf.append('\t');
            buf.append(value);
            if (i < pd.length - 1) { buf.append('\n'); }
        }
        return buf.toString();
    }
    
    @Override
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }
    
    @Override
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 1) {
            Object source = mouseEvent.getSource();
            if (source instanceof SensorIcon) {
                source = ((SensorIcon) source).getSensor();
            } else if (source instanceof MoverIcon) {
                source = ((MoverIcon) source).getMover();
            }
            text.setText(introspect(source));
            text.setEditable(false);
            
            JOptionPane.showMessageDialog((Component) mouseEvent.getSource(),
                    scrollPane, mouseEvent.getSource().toString(),
                    JOptionPane.INFORMATION_MESSAGE
                    );
        }
    }
    
    public Mover getMoverFromObject(Object obj) {
        BeanInfo bi = null;
        try {
            bi= Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (PropertyDescriptor pd1 : pd) {
                Method getter = pd1.getReadMethod();
                if (getter.getReturnType().isAssignableFrom(simkit.smd.Mover.class)) {
                    return (Mover) getter.invoke(obj, (Object[]) null);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { e.printStackTrace(System.err); }
        return null;
    }
    
    @Override
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }
    
}
