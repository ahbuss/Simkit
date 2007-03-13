
package simkit;

public class Parent extends GrandParent {

    protected int counter = 2;

    public void doRun() {
        schedule();
    }

    protected void schedule() {
    }
    public void doSomething(Parent p) {
        firePropertyChange("doSomething", counter, --counter);
        if (counter > 0) {
            schedule();
        }
    }
}
        
