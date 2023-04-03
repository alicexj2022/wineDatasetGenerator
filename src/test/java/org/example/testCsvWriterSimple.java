package org.example;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class testCsvWriterSimple {
    List<String[]> TotalPerDate;

    {
        try {
            TotalPerDate = CsvWriterSimple.createCsvVisitorsTotalPerDate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void  testCreateCsvVisitorsDownloadedDetailed() throws ParseException {

        List<String[]> lists = CsvWriterSimple.createCsvVisitorsDownloadedDetailed(TotalPerDate);

        assertTrue(lists.get(0).length==7);

    }
    @Test
    void createCsvDataUserTable()throws ParseException {
        List<String[]> userDetaled = CsvWriterSimple.createCsvDataUserTable(TotalPerDate);

        assertTrue(userDetaled.get(0).length==7);
    }
}
