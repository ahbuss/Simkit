package simkit.actions;

import javax.swing.Action;

public interface ActionInfo {

    public String[] getActionNames();

    public Action[] getActions(Object instance);
    
    public Action[] getActions(Object instance, String[] justThese);
    
    public Action getAction(Object instance, String actionName);
    
    public Class<?> getActionClass();
    
    public boolean isActionMethod(String methodName);
    
}
