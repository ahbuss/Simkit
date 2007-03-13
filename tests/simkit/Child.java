
package simkit;

public class Child extends Parent {

    public void schedule() {
        waitDelay("Something", 1.0, new Object[]{this});
    }

    //public void doSomething(Child c) {
     //   super.doSomething(c);
    //}
}
