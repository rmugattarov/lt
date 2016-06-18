package rmugattarov.luxoft_task.accumulator;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by rmugattarov on 18.06.2016.
 */
public class Monday {
    public static volatile double instrumentOneSum = -1;
    public static volatile int instrumentOneElementCount;
    public static volatile double instrumentTwoNov2014Sum = -1;
    public static volatile int instrumentTwoNov2014ElementCount;
    public static volatile double instrumentThreeMax = -1;
    public static volatile Map<String, TreeSet<InstrumentData>> genericInstrumentStatistics = new HashMap<>();
}
