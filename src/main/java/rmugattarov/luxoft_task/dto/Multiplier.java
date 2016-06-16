package rmugattarov.luxoft_task.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by rmugattarov on 16.06.2016.
 */
public class Multiplier implements Serializable {
    private static final long serialVersionUID = 7969781812425555631L;
    public static final Multiplier NO_MULTIPLIER = new Multiplier(null);
    private final BigDecimal multiplier;

    public Multiplier(Double multiplier) {
        if (multiplier == null) {
            this.multiplier = null;
        } else {
            this.multiplier = new BigDecimal(multiplier);
        }
    }

    @Override
    public String toString() {
        return String.format(" Multiplier {multiplier : %s} ", multiplier);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = Boolean.FALSE;
        if (o != null && o.getClass() == Multiplier.class) {
            Multiplier that = (Multiplier) o;
            result = Objects.equals(this.multiplier, that.multiplier);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return multiplier == null ? 0 : multiplier.hashCode();
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }
}
