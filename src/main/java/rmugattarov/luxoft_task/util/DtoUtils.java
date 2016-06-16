package rmugattarov.luxoft_task.util;

import com.google.common.base.Strings;
import rmugattarov.luxoft_task.constants.FileSourceConstants;
import rmugattarov.luxoft_task.dto.InstrumentData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class DtoUtils {

    public static InstrumentData convertStringToInstrumentData(String instrumentDataAsString) {
        InstrumentData result = null;
        if (!Strings.isNullOrEmpty(instrumentDataAsString)) {
            String[] instrumentDataFields = instrumentDataAsString.split(FileSourceConstants.FIELD_DELIMITER);
            String instrumentId = instrumentDataFields[0];
            LocalDate localDate = LocalDate.parse(instrumentDataFields[1], DateTimeFormatter.ofPattern(FileSourceConstants.INSTRUMENT_DATE_FORMAT, FileSourceConstants.DATE_LOCALE));
            Double value = Double.valueOf(instrumentDataFields[2]);
            result = new InstrumentData(instrumentId, localDate, new BigDecimal(value));
        }
        return result;
    }
}
