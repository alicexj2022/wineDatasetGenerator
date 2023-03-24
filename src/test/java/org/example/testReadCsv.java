package org.example;

import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testReadCsv {

    @Test
    void  testReadCsv() throws ParseException, IOException {

        List<String[]> dates = readCsv.getDataFromCsv();
        assert(dates.get(0).length == 2);

        // before filter out the first list, now it is no longer needed
        //assertEquals(dates.get(0)[1].replaceAll("\uFEFF", ""),"continent_name");

        assert(dates.get(0)[0].equals("Africa"));
        assert(dates.get(0)[1].equals("Algeria"));
        assert(dates.get(1)[0].equals("Africa"));
        assert(dates.get(1)[1].equals("Angola"));
    }
}
