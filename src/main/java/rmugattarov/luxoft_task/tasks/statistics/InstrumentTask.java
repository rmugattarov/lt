package rmugattarov.luxoft_task.tasks.statistics;

import rmugattarov.luxoft_task.accumulator.*;
import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;
import rmugattarov.luxoft_task.dto.Multiplier;
import rmugattarov.luxoft_task.impl.DbInstrumentMultiplierProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
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

            BigDecimal value = instrumentData.getValue();
            Multiplier multiplierDto = DbInstrumentMultiplierProvider.getInstrumentMultiplier(instrumentData.getInstrumentId());
            BigDecimal multiplier = multiplierDto.getMultiplier();
            if (multiplier != null) {
                value = value.multiply(multiplier);
            }

            String instrumentId = instrumentData.getInstrumentId();
            switch (instrumentId) {
                case InstrumentConstants.INSTRUMENT_ONE:
                    saveInstrumentOneStat(value);
                    break;
                case InstrumentConstants.INSTRUMENT_TWO:
                    LocalDate localDate = instrumentData.getLocalDate();
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

    private void saveGenericInstrumentStat(InstrumentData instrumentData, BigDecimal value, String instrumentId) {
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
            treeSet = new TreeSet<>((Comparator<InstrumentData>) (o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()));
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

    private void saveInstrumentThreeStat(BigDecimal value) {
        switch (myDay) {
            case MONDAY:
                if (Monday.instrumentThreeMax == null) {
                    Monday.instrumentThreeMax = value;
                } else {
                    Monday.instrumentThreeMax = Monday.instrumentThreeMax.max(value);
                }
                break;
            case TUESDAY:
                if (Tuesday.instrumentThreeMax == null) {
                    Tuesday.instrumentThreeMax = value;
                } else {
                    Tuesday.instrumentThreeMax = Tuesday.instrumentThreeMax.max(value);
                }
                break;
            case WEDNESDAY:
                if (Wednesday.instrumentThreeMax == null) {
                    Wednesday.instrumentThreeMax = value;
                } else {
                    Wednesday.instrumentThreeMax = Wednesday.instrumentThreeMax.max(value);
                }
                break;
            case THURSDAY:
                if (Thursday.instrumentThreeMax == null) {
                    Thursday.instrumentThreeMax = value;
                } else {
                    Thursday.instrumentThreeMax = Thursday.instrumentThreeMax.max(value);
                }
                break;
            case FRIDAY:
                if (Friday.instrumentThreeMax == null) {
                    Friday.instrumentThreeMax = value;
                } else {
                    Friday.instrumentThreeMax = Friday.instrumentThreeMax.max(value);
                }
                break;
        }
    }

    private void saveInstrumentTwoStat(BigDecimal value) {
        switch (myDay) {
            case MONDAY:
                if (Monday.instrumentTwoNov2014Sum == null) {
                    Monday.instrumentTwoNov2014Sum = value;
                } else {
                    Monday.instrumentTwoNov2014Sum = Monday.instrumentTwoNov2014Sum.add(value);
                }
                Monday.instrumentTwoNov2014ElementCount++;
                break;
            case TUESDAY:
                if (Tuesday.instrumentTwoNov2014Sum == null) {
                    Tuesday.instrumentTwoNov2014Sum = value;
                } else {
                    Tuesday.instrumentTwoNov2014Sum = Tuesday.instrumentTwoNov2014Sum.add(value);
                }
                Tuesday.instrumentTwoNov2014ElementCount = Tuesday.instrumentTwoNov2014ElementCount.add(BigInteger.ONE);
                break;
            case WEDNESDAY:
                if (Wednesday.instrumentTwoNov2014Sum == null) {
                    Wednesday.instrumentTwoNov2014Sum = value;
                } else {
                    Wednesday.instrumentTwoNov2014Sum = Wednesday.instrumentTwoNov2014Sum.add(value);
                }
                Wednesday.instrumentTwoNov2014ElementCount = Wednesday.instrumentTwoNov2014ElementCount.add(BigInteger.ONE);
                break;
            case THURSDAY:
                if (Thursday.instrumentTwoNov2014Sum == null) {
                    Thursday.instrumentTwoNov2014Sum = value;
                } else {
                    Thursday.instrumentTwoNov2014Sum = Thursday.instrumentTwoNov2014Sum.add(value);
                }
                Thursday.instrumentTwoNov2014ElementCount = Thursday.instrumentTwoNov2014ElementCount.add(BigInteger.ONE);
                break;
            case FRIDAY:
                if (Friday.instrumentTwoNov2014Sum == null) {
                    Friday.instrumentTwoNov2014Sum = value;
                } else {
                    Friday.instrumentTwoNov2014Sum = Friday.instrumentTwoNov2014Sum.add(value);
                }
                Friday.instrumentTwoNov2014ElementCount = Friday.instrumentTwoNov2014ElementCount.add(BigInteger.ONE);
                break;
        }
    }

    private void saveInstrumentOneStat(BigDecimal value) {
        switch (myDay) {
            case MONDAY:
                if (Monday.instrumentOneSum == null) {
                    Monday.instrumentOneSum = value;
                } else {
                    Monday.instrumentOneSum = Monday.instrumentOneSum.add(value);
                }
                Monday.instrumentOneElementCount++;
                break;
            case TUESDAY:
                if (Tuesday.instrumentOneSum == null) {
                    Tuesday.instrumentOneSum = value;
                } else {
                    Tuesday.instrumentOneSum = Tuesday.instrumentOneSum.add(value);
                }
                Tuesday.instrumentOneElementCount = Tuesday.instrumentOneElementCount.add(BigInteger.ONE);
                break;
            case WEDNESDAY:
                if (Wednesday.instrumentOneSum == null) {
                    Wednesday.instrumentOneSum = value;
                } else {
                    Wednesday.instrumentOneSum = Wednesday.instrumentOneSum.add(value);
                }
                Wednesday.instrumentOneElementCount = Wednesday.instrumentOneElementCount.add(BigInteger.ONE);
                break;
            case THURSDAY:
                if (Thursday.instrumentOneSum == null) {
                    Thursday.instrumentOneSum = value;
                } else {
                    Thursday.instrumentOneSum = Thursday.instrumentOneSum.add(value);
                }
                Thursday.instrumentOneElementCount = Thursday.instrumentOneElementCount.add(BigInteger.ONE);
                break;
            case FRIDAY:
                if (Friday.instrumentOneSum == null) {
                    Friday.instrumentOneSum = value;
                } else {
                    Friday.instrumentOneSum = Friday.instrumentOneSum.add(value);
                }
                Friday.instrumentOneElementCount = Friday.instrumentOneElementCount.add(BigInteger.ONE);
                break;
        }
    }
}
