package simkit;

import java.util.*;
class ListenerNotifier extends Thread {

    private SimEventSource simEntity;
    private SimEvent event;

    ListenerNotifier(SimEvent e) {
       event = e;
       simEntity = (SimEventSource) e.getSource();
    }

    public void run() {
        simEntity.notifyListeners(event);
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {}
    }
}