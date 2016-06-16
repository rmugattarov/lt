package rmugattarov.luxoft_task.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentData implements Serializable {
    public static final InstrumentData PROVIDER_EXHAUSTED = new InstrumentData("PROVIDER_EXHAUSTED", LocalDate.of(1, 1, 1), BigDecimal.ZERO);
    private static final long serialVersionUID = 3018176913056556176L;
    private final String instrumentId;
    private final LocalDate localDate;
    private final BigDecimal value;
    private int hashCode = -1;

    public InstrumentData(String instrumentId, LocalDate localDate, BigDecimal value) {
        this.instrumentId = instrumentId;
        this.localDate = localDate;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(" InstrumentData {instrumentId : %s, localDate : %s, value : %f} ", instrumentId, localDate, value);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o != null && o.getClass() == InstrumentData.class) {
            InstrumentData that = (InstrumentData) o;
            result = this.instrumentId.equals(that.instrumentId) && this.localDate.equals(that.localDate) && this.value.equals(that.value);
        }
        return result;
    }

    @Override
    public int hashCode() {
        if (hashCode == -1) {
            hashCode = instrumentId.hashCode() * localDate.hashCode() * value.hashCode();
        }
        return hashCode;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public BigDecimal getValue() {
        return value;
    }
}
