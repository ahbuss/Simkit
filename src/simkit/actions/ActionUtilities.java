package simkit.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

/**
 * @version $Id$
 * @author ahbuss
 */
public class ActionUtilities {
    
    public static String[] DECORATION_KEYS;
    
    static {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(Action.ACCELERATOR_KEY);
        keys.add(Action.ACTION_COMMAND_KEY);
        keys.add(Action.DEFAULT);
        keys.add(Action.LONG_DESCRIPTION);
        keys.add(Action.MNEMONIC_KEY);
        keys.add(Action.NAME);
        keys.add(Action.SHORT_DESCRIPTION);
        keys.add(Action.SMALL_ICON);
        DECORATION_KEYS =  keys.toArray(new String[0]);
    }
    
    public static JMenu createMenu(String title, Object usingObject) {
        if (usingObject == null) { return new JMenu(); }
        title = title == null ? "" : title;
        ActionInfo ai = ActionIntrospector.getActionInfo(usingObject.getClass());
        Action[] actions = ai.getActions(usingObject);
        return createMenu(title, actions);
    }
    
    public static JMenu createMenu(String title, Object usingObject, String[] justThese) {
        Action[] actions = ActionIntrospector.getActions(usingObject, justThese);
        return createMenu(title, actions);
    }
    
    public static JMenu createMenu(String title, Object[] usingObject) {
        if (usingObject == null) { return null; }
        title = title != null ? title : "";
        JMenu menu = new JMenu(title);
        for (int i = 0; i < usingObject.length; i++) {
            if (usingObject[i] == null) { continue; }
            Action[] actions = ActionIntrospector.getActions(usingObject[i]);
            for (Action action : actions) {
                menu.add(createMenuItem(action));
            }
            if (i < usingObject.length - 1) {
                menu.addSeparator();
            }
        }
        return menu;
    }
    
    public static JMenu[] createMenus(String[] title, Action[][] actions) {
        if (title == null || actions == null) {
            throw new NullPointerException();
        }
        else if (title.length != actions.length) {
            throw new IllegalArgumentException("title and action arrays not equal length: " +
            title.length + ", " + actions.length);
        }
        JMenu[] menu = new JMenu[title.length];
        for (int i = 0; i < title.length; i++) {
            menu[i] = createMenu(title[i], actions[i]);
        }
        return menu;
    }
    
    public static JMenu createMenu(String title, Action[] actions) {
        if (actions == null) { return null; }
        title = title != null ? title : "";
        JMenu menu = new JMenu(title);
        for (Action action : actions) {
            menu.add(createMenuItem(action));
        }
        return menu;
    }
    
    public static JToolBar createToolBar(String label, Action[] actions) {
        if (actions == null) { return null; }
        if (label == null || label.length() == 0) {
            return createToolBar(actions);
        }
        JLabel labelLabel = new JLabel(label);
        JToolBar toolBar = createToolBar(actions);
        toolBar.add(labelLabel, 0);
        return toolBar;
    }
    
    public static JToolBar createToolBar(String label, Object usingObject) {
        return usingObject != null ? createToolBar(label, ActionIntrospector.getActions(usingObject)) : null;
    }
    
    public static JToolBar createToolBar(Object usingObject, String[] justThese) {
        Action[] actions = ActionIntrospector.getActions(usingObject, justThese);
        return createToolBar(actions);
    }
    
    public static JToolBar createToolBar(Action[] actions ) {
        if (actions == null) { return null; }
        JToolBar toolBar = new JToolBar();
        JButton[] buttons = createButtons(actions);
        for (int i = 0; i < actions.length; i++) {
            if (actions[i].getValue(Action.SMALL_ICON) != null) {
                buttons[i].setText(null);
            }
            toolBar.add(buttons[i]);
        }
        return toolBar;
    }
    
    public static JToolBar createToolBar(String label, Object[] usingObject) {
        if (usingObject == null) { return null; }
        JToolBar toolBar = new JToolBar();
        if (label != null && label.length() > 0) {
            toolBar.add(new JLabel(label));
        }
        for (int i = 0; i < usingObject.length; i++) {
            if (usingObject[i] == null) { continue; }
            Action[] actions = ActionIntrospector.getActions(usingObject[i]);
            for (Action action : actions) {
                toolBar.add(createButton(action));
            }
            if (i < usingObject.length - 1) {
                toolBar.addSeparator();
            }
        }
        return toolBar;
    }
    
    public static JToolBar createToolBar(Object[] usingObject) {
        return createToolBar(null, usingObject);
    }
    
    public static JButton[] createButtons(Action[] actions) {
        if (actions == null) { return null; }
        JButton[] button = new JButton[actions.length];
        for (int i = 0; i < button.length; i++) {
            button[i] = createButton(actions[i]);
        }
        return button;
    }
    
    public static JButton createButton(Action action) {
        JButton button = new JButton(action);
        JButtonListener listener = new JButtonListener(button);
        action.addPropertyChangeListener(listener);
        configureButton(action, listener);
        return button;
    }
    
    public static JButton createButton(Object source, String method) {
        Action action = ActionIntrospector.getAction(source, method);
        return createButton(action);
    }
    
    public static JMenuItem createMenuItem(Action action) {
        JMenuItem menuItem = new JMenuItem(action);
        JButtonListener listener = new JButtonListener(menuItem);
        action.addPropertyChangeListener(listener);
        configureButton(action, listener);
        return menuItem;
    }
    
    public static Action decorateAction(Action action, Map decorations) {
        for (Iterator i = decorations.keySet().iterator(); i.hasNext(); ) {
            String key = i.next().toString();
            action.putValue(key, decorations.get(key));
        }
        return action;
    }
    
    public static Action[] decorateActions(Action[] actions, Map[] decorations) {
        if (actions == null || decorations == null) {
            throw new NullPointerException();
        }
        else if (actions.length != decorations.length) {
            throw new IllegalArgumentException("Number of actions not the same as the "
            + " number of decorations: " + actions.length + ", " + decorations.length);
        }
        for (int i = 0; i < actions.length; i++) {
            decorateAction(actions[i], decorations[i]);
        }
        return actions;
    }
    
    public static void configureButton(Action action, PropertyChangeListener listener) {
        if (action == null || listener == null) { return; }
        action.addPropertyChangeListener(listener);
        action.putValue("listener", listener);
        if (action instanceof AbstractAction) {
            AbstractAction abstractAction = (AbstractAction)action;
            Object[] key = abstractAction.getKeys();
            for (Object key1 : key) {
                listener.propertyChange(new PropertyChangeEvent(action, key1.toString(), null, abstractAction.getValue(key1.toString())));
            }
        }
    }
    
    public static void bindActionToProperties(Action action, Object object) {
        if (object == null) { return; }
        JButtonListener listener = new JButtonListener(object);
        configureButton(action, listener);
    }
    
    public static void unbindActionTFromListener(Action action, Object object) {
        Object listener = action.getValue("listener");
        if (listener instanceof PropertyChangeListener) {
            action.removePropertyChangeListener((PropertyChangeListener) listener);
            action.putValue("listener", null);
        }
    }
    
    public static Map getCurrentDecorationsFor(Action action) {
        Map<String, Object> decorations = new WeakHashMap<>();
        decorations.put("action", action);
        for (String DECORATION_KEYS1 : DECORATION_KEYS) {
            Object value = action.getValue(DECORATION_KEYS1);
            if (value != null) {
                decorations.put(DECORATION_KEYS1, value);
            }
        }
        return decorations;
    }
    
    public static Map[] getCurrentDecorationsFor(Action[] actions) {
        Map[] decorations = new Map[actions.length];
        for (int i = 0; i < actions.length; i++) {
            decorations[i] = getCurrentDecorationsFor(actions[i]);
        }
        return decorations;
    }
}
