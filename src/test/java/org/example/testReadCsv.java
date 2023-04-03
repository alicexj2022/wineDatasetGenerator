package org.example;

import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testReadCsv {

    @Test
    void  testReadCsv() throws ParseException, IOException {

        List<Country> dates = readCsv.getDataFromCsv();
        assert(dates.size() == 750);

        // before filter out the first list, now it is no longer needed
        //assertEquals(dates.get(0)[1].replaceAll("\uFEFF", ""),"continent_name");
    }
}
