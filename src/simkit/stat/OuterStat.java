package simkit.stat;

import static simkit.stat.OuterStatType.MEAN;

/**
 *
 * @author ahbuss
 */
public class OuterStat extends SimpleStatsTally {

    private final OuterStatType type;

    public OuterStat(String name, OuterStatType type) {
       setName(name);
       this.type = type;
    }

    public OuterStat(String name) {
        this(name, MEAN);
    }

    public OuterStat() {
        this(DEFAULT_NAME);
    }
    
    public void newObservation(SampleStatistics stat) {
        switch (type) {
            case MEAN:
                newObservation(stat.getMean());
                break;
            case MIN:
                newObservation(stat.getMinObs());
                break;
            case MAX:
                newObservation(stat.getMaxObs());
                break;
            case STD:
                newObservation(stat.getStandardDeviation());
                break;
            case VARIANCE:
                newObservation(stat.getVariance());
                break;
            case COUNT:
                newObservation(stat.getCount());
                break;
            default:
                System.err.println("Invalid OuterStatType: " + type);
        }
    }

}
