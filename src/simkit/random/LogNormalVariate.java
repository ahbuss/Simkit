package simkit.random;

/**
 * @version $Id$
 * @author ahbuss
 */
public class LogNormalVariate extends RandomVariateBase {
    
    private RandomVariate normalVariate;
    
    public LogNormalVariate() {
    }

    public Object[] getParameters() {
        Object[] params = new Object[2];
        
        return params;
    }
    
    public RandomVariate getNormalVariate() {
        return normalVariate;
    }

    public void setNormalVariate(RandomVariate normalVariate) {
        this.normalVariate = normalVariate;
    }

    public double generate() {
        return 0.0;
    }

    public void setParameters(Object... params) {
    }
    
}
