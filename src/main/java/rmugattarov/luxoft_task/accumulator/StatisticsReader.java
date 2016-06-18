package rmugattarov.luxoft_task.accumulator;

import com.google.common.base.Strings;
import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by rmugattarov on 18.06.2016.
 */
public class StatisticsReader {
    public static BigDecimal getInstrumentOneMean() {
        BigDecimal mondayMean = Monday.instrumentOneSum == null ? null : Monday.instrumentOneSum.divide(new BigDecimal(Monday.instrumentOneElementCount), BigDecimal.ROUND_HALF_UP);
        BigDecimal tuesdayMean = Tuesday.instrumentOneSum == null ? null : Tuesday.instrumentOneSum.divide(new BigDecimal(Tuesday.instrumentOneElementCount), BigDecimal.ROUND_HALF_UP);
        BigDecimal wednesdayMean = Wednesday.instrumentOneSum == null ? null : Wednesday.instrumentOneSum.divide(new BigDecimal(Wednesday.instrumentOneElementCount), BigDecimal.ROUND_HALF_UP);
        BigDecimal thursdayMean = Thursday.instrumentOneSum == null ? null : Thursday.instrumentOneSum.divide(new BigDecimal(Thursday.instrumentOneElementCount), BigDecimal.ROUND_HALF_UP);
        BigDecimal fridayMean = Friday.instrumentOneSum == null ? null : Friday.instrumentOneSum.divide(new BigDecimal(Friday.instrumentOneElementCount), BigDecimal.ROUND_HALF_UP);

        BigDecimal[] dailyStats = new BigDecimal[]{mondayMean, tuesdayMean, wednesdayMean, thursdayMean, fridayMean};
        BigDecimal sum = null;
        int elementCount = 0;
        for (BigDecimal dailyStat : dailyStats) {
            if (dailyStat == null) {
                continue;
            }
            elementCount++;
            if (sum == null) {
                sum = BigDecimal.ZERO;
            }
            sum = sum.add(dailyStat);
        }
        return sum == null ? null : sum.divide(new BigDecimal(elementCount), BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getInstrumentTwoMeanNov2014() {
        BigDecimal mondayMean = Monday.instrumentTwoNov2014Sum == null ? null : Monday.instrumentTwoNov2014Sum.divide(new BigDecimal(Monday.instrumentTwoNov2014ElementCount), BigDecimal.ROUND_HALF_UP);
        BigDecimal tuesdayMean = Tuesday.instrumentTwoNov2014Sum == null ? null : Tuesday.instrumentTwoNov2014Sum.divide(new BigDecimal(Tuesday.instrumentTwoNov2014ElementCount), BigDecimal.ROUND_HALF_UP);
        BigDecimal wednesdayMean = Wednesday.instrumentTwoNov2014Sum == null ? null : Wednesday.instrumentTwoNov2014Sum.divide(new BigDecimal(Wednesday.instrumentTwoNov2014ElementCount), BigDecimal.ROUND_HALF_UP);
        BigDecimal thursdayMean = Thursday.instrumentTwoNov2014Sum == null ? null : Thursday.instrumentTwoNov2014Sum.divide(new BigDecimal(Thursday.instrumentTwoNov2014ElementCount), BigDecimal.ROUND_HALF_UP);
        BigDecimal fridayMean = Friday.instrumentTwoNov2014Sum == null ? null : Friday.instrumentTwoNov2014Sum.divide(new BigDecimal(Friday.instrumentTwoNov2014ElementCount), BigDecimal.ROUND_HALF_UP);

        BigDecimal[] dailyStats = new BigDecimal[]{mondayMean, tuesdayMean, wednesdayMean, thursdayMean, fridayMean};
        BigDecimal sum = null;
        int elementCount = 0;
        for (BigDecimal dailyStat : dailyStats) {
            if (dailyStat == null) {
                continue;
            }
            elementCount++;
            if (sum == null) {
                sum = BigDecimal.ZERO;
            }
            sum = sum.add(dailyStat);
        }
        return sum == null ? null : sum.divide(new BigDecimal(elementCount), BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getInstrumentThreeMax() {
        BigDecimal result = null;
        BigDecimal mondayMax = Monday.instrumentThreeMax;
        BigDecimal tuesdayMax = Tuesday.instrumentThreeMax;
        BigDecimal wednesdayMax = Wednesday.instrumentThreeMax;
        BigDecimal thursdayMax = Thursday.instrumentThreeMax;
        BigDecimal fridayMax = Friday.instrumentThreeMax;

        BigDecimal[] dailyStats = new BigDecimal[]{mondayMax, tuesdayMax, wednesdayMax, thursdayMax, fridayMax};
        for (BigDecimal dailyStat : dailyStats) {
            if (dailyStat == null) {
                continue;
            }
            if (result == null) {
                result = BigDecimal.ZERO;
            }
            result = result.max(dailyStat);
        }
        return result;
    }

    public static BigDecimal getGenericInstrumentStatistics(String instrumentId) {
        if (Strings.isNullOrEmpty(instrumentId)) {
            return null;
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
            return null;
        }
        int elementCount = 0;
        BigDecimal result = BigDecimal.ZERO;
        Iterator<InstrumentData> descendingIterator = commonStats.descendingIterator();
        while (descendingIterator.hasNext()) {
            result = result.add(descendingIterator.next().getValue());
            if (++elementCount == 10) {
                break;
            }
        }
        return result;
    }
}
