package rmugattarov.luxoft_task.accumulator;

import com.google.common.base.Strings;
import rmugattarov.luxoft_task.dto.InstrumentData;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by rmugattarov on 18.06.2016.
 */
public class StatisticsReader {
    public static double getInstrumentOneMean() {
        double mondayMean = Monday.instrumentOneElementCount == 0 ? -1 : Monday.instrumentOneSum / Monday.instrumentOneElementCount;
        double tuesdayMean = Tuesday.instrumentOneElementCount == 0 ? -1 : Tuesday.instrumentOneSum / Tuesday.instrumentOneElementCount;
        double wednesdayMean = Wednesday.instrumentOneElementCount == 0 ? -1 : Wednesday.instrumentOneSum / Wednesday.instrumentOneElementCount;
        double thursdayMean = Thursday.instrumentOneElementCount == 0 ? -1 : Thursday.instrumentOneSum / Thursday.instrumentOneElementCount;
        double fridayMean = Friday.instrumentOneElementCount == 0 ? -1 : Friday.instrumentOneSum / Friday.instrumentOneElementCount;

        double[] dailyStats = new double[]{mondayMean, tuesdayMean, wednesdayMean, thursdayMean, fridayMean};
        double sum = -1;
        int elementCount = 0;
        for (double dailyStat : dailyStats) {
            if (dailyStat == -1) {
                continue;
            }
            elementCount++;
            if (sum == -1) {
                sum = 0;
            }
            sum += dailyStat;
        }
        return sum == -1 ? -1 : sum / elementCount;
    }

    public static double getInstrumentTwoMeanNov2014() {
        double mondayMean = Monday.instrumentTwoNov2014ElementCount == 0 ? -1 : Monday.instrumentTwoNov2014Sum / Monday.instrumentTwoNov2014ElementCount;
        double tuesdayMean = Tuesday.instrumentTwoNov2014ElementCount == 0 ? -1 : Tuesday.instrumentTwoNov2014Sum / Tuesday.instrumentTwoNov2014ElementCount;
        double wednesdayMean = Wednesday.instrumentTwoNov2014ElementCount == 0 ? -1 : Wednesday.instrumentTwoNov2014Sum / Wednesday.instrumentTwoNov2014ElementCount;
        double thursdayMean = Thursday.instrumentTwoNov2014ElementCount == 0 ? -1 : Thursday.instrumentTwoNov2014Sum / Thursday.instrumentTwoNov2014ElementCount;
        double fridayMean = Friday.instrumentTwoNov2014ElementCount == 0 ? -1 : Friday.instrumentTwoNov2014Sum / Friday.instrumentTwoNov2014ElementCount;

        double[] dailyStats = new double[]{mondayMean, tuesdayMean, wednesdayMean, thursdayMean, fridayMean};
        double sum = -1;
        int elementCount = 0;
        for (double dailyStat : dailyStats) {
            if (dailyStat == -1) {
                continue;
            }
            elementCount++;
            if (sum == -1) {
                sum = 0;
            }
            sum += dailyStat;
        }
        return sum == -1 ? -1 : sum / elementCount;
    }

    public static double getInstrumentThreeMax() {
        double result = -1;
        double mondayMax = Monday.instrumentThreeMax;
        double tuesdayMax = Tuesday.instrumentThreeMax;
        double wednesdayMax = Wednesday.instrumentThreeMax;
        double thursdayMax = Thursday.instrumentThreeMax;
        double fridayMax = Friday.instrumentThreeMax;

        double[] dailyStats = new double[]{mondayMax, tuesdayMax, wednesdayMax, thursdayMax, fridayMax};
        for (double dailyStat : dailyStats) {
            if (dailyStat == -1) {
                continue;
            }
            if (result == -1) {
                result = 0;
            }
            result = Math.max(result, dailyStat);
        }
        return result;
    }

    public static double getGenericInstrumentStatistics(String instrumentId) {
        if (Strings.isNullOrEmpty(instrumentId)) {
            return -1;
        }

        TreeSet<InstrumentData> mondayStats = Monday.genericInstrumentStatistics.get(instrumentId);
        TreeSet<InstrumentData> tuesdayStats = Tuesday.genericInstrumentStatistics.get(instrumentId);
        TreeSet<InstrumentData> wednesdayStats = Wednesday.genericInstrumentStatistics.get(instrumentId);
        TreeSet<InstrumentData> thursdayStats = Thursday.genericInstrumentStatistics.get(instrumentId);
        TreeSet<InstrumentData> fridayStats = Friday.genericInstrumentStatistics.get(instrumentId);

        TreeSet<InstrumentData>[] dailyStats = new TreeSet[]{mondayStats, tuesdayStats, wednesdayStats, thursdayStats, fridayStats};
        TreeSet<InstrumentData> commonStats = null;
        for (TreeSet<InstrumentData> dailyStat : dailyStats) {
            if (dailyStat == null) {
                continue;
            }
            if (commonStats == null) {
                commonStats = new TreeSet<>((Comparator<InstrumentData>) (o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));
            }
            commonStats.addAll(dailyStat);
        }
        if (commonStats == null) {
            return -1;
        }
        int elementCount = 0;
        double result = 0;
        Iterator<InstrumentData> descendingIterator = commonStats.descendingIterator();
        while (descendingIterator.hasNext()) {
            result += (descendingIterator.next().getValue());
            if (++elementCount == 10) {
                break;
            }
        }
        return result;
    }
}
