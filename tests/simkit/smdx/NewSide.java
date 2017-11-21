package simkit.smdx;

class NewSide extends Side {

    public static final Side CHARTRUESE = new NewSide("Chartruese");
    public static final Side MAGENTA = new NewSide("Magenta");

    /** Creates new NewSides */
    protected NewSide(String name) {
        super(name);
    }
}
