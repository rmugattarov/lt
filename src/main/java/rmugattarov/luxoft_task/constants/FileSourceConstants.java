package rmugattarov.luxoft_task.constants;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public interface FileSourceConstants {
    String INSTRUMENT_DATE_FORMAT = "dd-MMM-yyyy";
    String FIELD_DELIMITER = ",";
    Locale DATE_LOCALE = Locale.US;
    LocalDate TODAY = LocalDate.of(2014, Month.DECEMBER, 19);
}
