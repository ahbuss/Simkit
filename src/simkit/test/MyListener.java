package simkit.test;
import simkit.*;

    public class MyListener extends SimEntityBase {
        protected int thisCount;
        protected int thatCount;
        protected int theOtherCount;
        
        public MyListener() {}
        
        public void reset() {
            thisCount = 0;
            thatCount = 0;
            theOtherCount = 0;
        }
        
        public void doRun() {
            firePropertyChange("thisCount", thisCount);
            firePropertyChange("thatCount", thatCount);
            firePropertyChange("theOtherCount", theOtherCount);
        }
        
        public void doThis() {
            firePropertyChange("thisCount", thisCount, ++thisCount);
        }
        
        public void doThat() {
            firePropertyChange("thatCount", thatCount, ++thatCount);
        }
        
        public void doTheOther() {
            firePropertyChange("theOtherCount", theOtherCount, ++theOtherCount);
        }
    }
    