package rmugattarov.luxoft_task.util;

import org.junit.Assert;
import org.junit.Test;
import rmugattarov.luxoft_task.constants.InstrumentConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class DtoUtilsTest {
    @Test
    public void test_string_to_dto() {
        InstrumentData instrumentData = DtoUtils.convertStringToInstrumentData("INSTRUMENT1,01-Jan-1996,2.4655");
        Assert.assertEquals(InstrumentConstants.INSTRUMENT_ONE, instrumentData.getInstrumentId());
        LocalDate localDate = LocalDate.of(1996, 1, 1);
        Assert.assertEquals(localDate, instrumentData.getLocalDate());
        Assert.assertEquals(new BigDecimal(2.4655), instrumentData.getValue());

        instrumentData = DtoUtils.convertStringToInstrumentData("INSTRUMENT1,12-Mar-1996,2.5795");
        Assert.assertEquals("INSTRUMENT1", instrumentData.getInstrumentId());
        localDate = LocalDate.of(1996, 3, 12);
        Assert.assertEquals(localDate, instrumentData.getLocalDate());
        Assert.assertEquals(new BigDecimal(2.5795), instrumentData.getValue());

        instrumentData = DtoUtils.convertStringToInstrumentData("INSTRUMENT2,01-Aug-1996,9.295001295");
        Assert.assertEquals(InstrumentConstants.INSTRUMENT_TWO, instrumentData.getInstrumentId());
        localDate = LocalDate.of(1996, 8, 1);
        Assert.assertEquals(localDate, instrumentData.getLocalDate());
        Assert.assertEquals(new BigDecimal(9.295001295), instrumentData.getValue());

    }

}