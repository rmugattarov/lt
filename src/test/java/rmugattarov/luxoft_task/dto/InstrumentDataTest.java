package rmugattarov.luxoft_task.dto;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rmugattarov on 15.06.2016.
 */
public class InstrumentDataTest {
    @Test
    public void test_equals() {
        InstrumentData instrumentData = new InstrumentData("qwerty", LocalDate.of(2000, 1, 1), 1.01);
        InstrumentData instrumentDataCopy = new InstrumentData("qwerty", LocalDate.of(2000, 1, 1), 1.01);
        List<InstrumentData> list = new ArrayList<>();
        Assert.assertTrue(list.add(instrumentData));
        Assert.assertTrue(list.add(instrumentData));
        Assert.assertEquals(0, list.indexOf(instrumentDataCopy));
        Assert.assertEquals(1, list.lastIndexOf(instrumentDataCopy));
    }

    @Test
    public void test_hashCode() {
        InstrumentData instrumentData = new InstrumentData("qwerty", LocalDate.of(2000, 1, 1), 1.01);
        InstrumentData instrumentDataCopy = new InstrumentData("qwerty", LocalDate.of(2000, 1, 1), 1.01);
        Set<InstrumentData> set = new HashSet<>();
        Assert.assertTrue(set.add(instrumentData));
        Assert.assertFalse(set.add(instrumentData));
        Assert.assertEquals(true, set.contains(instrumentDataCopy));
    }
}