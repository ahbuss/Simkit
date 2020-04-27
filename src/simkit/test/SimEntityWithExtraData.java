package simkit.test;

import simkit.SimEntityBase;

/**
 * 
 * @author ahbuss
 */
public class SimEntityWithExtraData extends SimEntityBase {

    protected int state;

    private Object[] extraData;

    public SimEntityWithExtraData() { }
    
    public SimEntityWithExtraData(Object[] extraData) {
        this.setExtraData(extraData);
    }
    
    @Override
    public void reset() {
        super.reset();
        this.state = 0;
    }
    
    public void doRun() {
        firePropertyChange("state", null, this.getState(), this.getExtraData());
    }
    
    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @return the extraData
     */
    public Object[] getExtraData() {
        return extraData.clone();
    }

    /**
     * @param extraData the extraData to set
     */
    public void setExtraData(Object[] extraData) {
        this.extraData = extraData.clone();
    }

}
