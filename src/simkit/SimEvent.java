package simkit;
/////////////////////////// Copyright Notice //////////////////////////
//                                                                   //
// This simkit package or sub-package and this file is Copyright (c) //
// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss.             //
//                                                                   //
// Please forward any changes, comments or suggestions to:           //
//   abuss@nps.navy.mil                                              //
//                                                                   //
///////////////////////////////////////////////////////////////////////

/**
  * <P>Changes
  * <UL>
  * <LI> 9/30/98 - Made <CODE>SimEvent</CODE> an interface (again).
  * </UL>
  *
  * @version 1.1.0
  * @author Kirk A. Stork
  * @author Arnold H. Buss
  *
**/

public interface SimEvent {

   public static final double   DEFAULT_PRIORITY = 0.0;

   public SimEntity getSource();

   public String getFullMethodName();
   public Object[] getParameters();

    public void setSource(SimEntity source);
    public boolean interruptParametersMatch(Object[] parameters);
    public void interrupt();
    public void setSerial(int num);
    public void setID(int num);
    public void setWaitState(SimEventState ws);
    public void setParameters(Object[] params);
    public void setEventName(String eventName);
    public void setScheduledTime(double time);
    public void setPriority(double priority);
    public void setCreationTime(double time);

    public String getMethodName();
    public SimEventState getWaitState();
    public double getScheduledTime();
    public double getEventPriority();
    public double getCreationTime();
    public double getOwnerPriority();
    public int getSerial();
    public String paramString();
    public String getEventName();
    public int getID();
    public boolean isPending();
    public void reset();

} 
