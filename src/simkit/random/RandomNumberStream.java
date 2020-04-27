package simkit.random;

/**
 * @since 1096
 * 
 * @author Kirk Stork (The MOVES Institute)
 */
public interface RandomNumberStream extends RandomNumber {
    public void setStreamAndSubstream(int stream, int substream);
    public int getStreamID();
    public int getSubstreamID();
}
