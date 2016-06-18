package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.accumulator.*;
import rmugattarov.luxoft_task.constants.FileSourceConstants;
import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.impl.DbInstrumentMultiplierProvider;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;

/**
 * Created by rmugattarov on 18.06.2016.
 */
public class InstrumentTask implements Runnable {
    public static final Comparator<InstrumentData> INSTRUMENT_DATA_COMPARATOR = (o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate());
    private final DayOfWeek myDay;
    private final BlockingQueue<InstrumentData> queue;

    public InstrumentTask(DayOfWeek myDay, BlockingQueue<InstrumentData> queue) {
        this.myDay = myDay;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            InstrumentData instrumentData = null;
            try {
                instrumentData = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (instrumentData == InstrumentData.PROVIDER_EXHAUSTED) {
                break;
            }
            if (instrumentData == null) {
                continue;
            }

            LocalDate localDate = instrumentData.getLocalDate();
            if (localDate.isAfter(FileSourceConstants.TODAY)) {
                continue;
            }

            double value = instrumentData.getValue();
            Double multiplier = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
            value *= multiplier;

            String instrumentId = instrumentData.getInstrumentId();
            switch (instrumentId) {
                case InstrumentConstants.INSTRUMENT_ONE:
                    saveInstrumentOneStat(value);
                    break;
                case InstrumentConstants.INSTRUMENT_TWO:
                    if (localDate.getYear() == 2014 && localDate.getMonth() == Month.NOVEMBER) {
                        saveInstrumentTwoStat(value);
                    }
                    break;
                case InstrumentConstants.INSTRUMENT_THREE:
                    saveInstrumentThreeStat(value);
                    break;
                default:
                    saveGenericInstrumentStat(instrumentData, value, instrumentId);
                    break;
            }
        }
        System.out.printf("\r\n>> %s task complete\r\n", myDay);
    }

    private void saveGenericInstrumentStat(InstrumentData instrumentData, double value, String instrumentId) {
        instrumentData = new InstrumentData(instrumentId, instrumentData.getLocalDate(), value);
        TreeSet<InstrumentData> treeSet = null;
        switch (myDay) {
            case MONDAY:
                treeSet = Monday.genericInstrumentStatistics.get(instrumentId);
                break;
            case TUESDAY:
                treeSet = Tuesday.genericInstrumentStatistics.get(instrumentId);
                break;
            case WEDNESDAY:
                treeSet = Wednesday.genericInstrumentStatistics.get(instrumentId);
                break;
            case THURSDAY:
                treeSet = Thursday.genericInstrumentStatistics.get(instrumentId);
                break;
            case FRIDAY:
                treeSet = Friday.genericInstrumentStatistics.get(instrumentId);
                break;
        }
        if (treeSet == null) {
            treeSet = new TreeSet<>(INSTRUMENT_DATA_COMPARATOR);
            treeSet.add(instrumentData);
            switch (myDay) {
                case MONDAY:
                    Monday.genericInstrumentStatistics.put(instrumentId, treeSet);
                    break;
                case TUESDAY:
                    Tuesday.genericInstrumentStatistics.put(instrumentId, treeSet);
                    break;
                case WEDNESDAY:
                    Wednesday.genericInstrumentStatistics.put(instrumentId, treeSet);
                    break;
                case THURSDAY:
                    Thursday.genericInstrumentStatistics.put(instrumentId, treeSet);
                    break;
                case FRIDAY:
                    Friday.genericInstrumentStatistics.put(instrumentId, treeSet);
                    break;
            }
        } else if (treeSet.size() < 10) {
            treeSet.add(instrumentData);
        } else {
            LocalDate earliestDate = treeSet.first().getLocalDate();
            if (instrumentData.getLocalDate().isAfter(earliestDate)) {
                treeSet.pollFirst();
                treeSet.add(instrumentData);
            }
        }
    }

    private void saveInstrumentThreeStat(double value) {
        switch (myDay) {
            case MONDAY:
                if (Monday.instrumentThreeMax == -1) {
                    Monday.instrumentThreeMax = value;
                } else {
                    Monday.instrumentThreeMax = Math.max(Monday.instrumentThreeMax, value);
                }
                break;
            case TUESDAY:
                if (Tuesday.instrumentThreeMax == -1) {
                    Tuesday.instrumentThreeMax = value;
                } else {
                    Tuesday.instrumentThreeMax = Math.max(Monday.instrumentThreeMax, value);
                }
                break;
            case WEDNESDAY:
                if (Wednesday.instrumentThreeMax == -1) {
                    Wednesday.instrumentThreeMax = value;
                } else {
                    Wednesday.instrumentThreeMax = Math.max(Monday.instrumentThreeMax, value);
                }
                break;
            case THURSDAY:
                if (Thursday.instrumentThreeMax == -1) {
                    Thursday.instrumentThreeMax = value;
                } else {
                    Thursday.instrumentThreeMax = Math.max(Monday.instrumentThreeMax, value);
                }
                break;
            case FRIDAY:
                if (Friday.instrumentThreeMax == -1) {
                    Friday.instrumentThreeMax = value;
                } else {
                    Friday.instrumentThreeMax = Math.max(Monday.instrumentThreeMax, value);
                }
                break;
        }
    }

    private void saveInstrumentTwoStat(double value) {
        switch (myDay) {
            case MONDAY:
                if (Monday.instrumentTwoNov2014Sum == -1) {
                    Monday.instrumentTwoNov2014Sum = value;
                } else {
                    Monday.instrumentTwoNov2014Sum += value;
                }
                Monday.instrumentTwoNov2014ElementCount++;
                break;
            case TUESDAY:
                if (Tuesday.instrumentTwoNov2014Sum == -1) {
                    Tuesday.instrumentTwoNov2014Sum = value;
                } else {
                    Tuesday.instrumentTwoNov2014Sum += value;
                }
                Tuesday.instrumentTwoNov2014ElementCount++;
                break;
            case WEDNESDAY:
                if (Wednesday.instrumentTwoNov2014Sum == -1) {
                    Wednesday.instrumentTwoNov2014Sum = value;
                } else {
                    Wednesday.instrumentTwoNov2014Sum += value;
                }
                Wednesday.instrumentTwoNov2014ElementCount++;
                break;
            case THURSDAY:
                if (Thursday.instrumentTwoNov2014Sum == -1) {
                    Thursday.instrumentTwoNov2014Sum = value;
                } else {
                    Thursday.instrumentTwoNov2014Sum += value;
                }
                Thursday.instrumentTwoNov2014ElementCount++;
                break;
            case FRIDAY:
                if (Friday.instrumentTwoNov2014Sum == -1) {
                    Friday.instrumentTwoNov2014Sum = value;
                } else {
                    Friday.instrumentTwoNov2014Sum += value;
                }
                Friday.instrumentTwoNov2014ElementCount++;
                break;
        }
    }

    private void saveInstrumentOneStat(double value) {
        switch (myDay) {
            case MONDAY:
                if (Monday.instrumentOneSum == -1) {
                    Monday.instrumentOneSum = value;
                } else {
                    Monday.instrumentOneSum += value;
                }
                Monday.instrumentOneElementCount++;
                break;
            case TUESDAY:
                if (Tuesday.instrumentOneSum == -1) {
                    Tuesday.instrumentOneSum = value;
                } else {
                    Tuesday.instrumentOneSum += value;
                }
                Tuesday.instrumentOneElementCount++;
                break;
            case WEDNESDAY:
                if (Wednesday.instrumentOneSum == -1) {
                    Wednesday.instrumentOneSum = value;
                } else {
                    Wednesday.instrumentOneSum += value;
                }
                Wednesday.instrumentOneElementCount++;
                break;
            case THURSDAY:
                if (Thursday.instrumentOneSum == -1) {
                    Thursday.instrumentOneSum = value;
                } else {
                    Thursday.instrumentOneSum += value;
                }
                Thursday.instrumentOneElementCount++;
                break;
            case FRIDAY:
                if (Friday.instrumentOneSum == -1) {
                    Friday.instrumentOneSum = value;
                } else {
                    Friday.instrumentOneSum += value;
                }
                Friday.instrumentOneElementCount++;
                break;
        }
    }
}
