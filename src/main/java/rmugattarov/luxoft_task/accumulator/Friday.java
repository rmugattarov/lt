package rmugattarov.luxoft_task.accumulator;

import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by rmugattarov on 18.06.2016.
 */
public class Friday {
    public static volatile BigDecimal instrumentOneSum;
    public static volatile BigInteger instrumentOneElementCount = BigInteger.ZERO;
    public static volatile BigDecimal instrumentTwoNov2014Sum;
    public static volatile BigInteger instrumentTwoNov2014ElementCount = BigInteger.ZERO;
    public static volatile BigDecimal instrumentThreeMax;
    public static volatile Map<String, TreeSet<InstrumentData>> genericInstrumentStatistics = new HashMap<>();
}
