package org.example;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class testDateGeneartor {
    @Test
   void  testDateGeneartor() throws ParseException {

        HashMap<String,List<Integer>> dates = dataGenerator.getDaysBetweenDates("7-Jun-2020","8-Jun-2022");
        List<Integer> d = dates.get("months");

         assert(d.get(0).toString().equals("5"));

         assertTrue(dates.get("months").size()==731);
    }

    @Test
    void  testGetDate() throws ParseException {

       List<LocalDate> dates = dataGenerator.getDates("2020-06-07","2022-06-08");
        System.out.println("dates 0: "+ dates.get(0).toString());
        assert(dates.get(0).toString().equals("2020-06-07"));

        assertTrue(dates.size()==731);
    }

    @Test
    void  testGenerateRandomizedIntegerList() {

        List<Integer> listOne = dataGenerator.generateRandomizedIntegerList("scale1",731);
        List<Integer> listTwo = dataGenerator.generateRandomizedIntegerList("scale2",731);

        assertTrue(listOne.size()==731);
        assertTrue(listOne.get(0)!=listTwo.get(0) ||listOne.get(1)!=listTwo.get(1) ||listOne.get(2)!=listTwo.get(2) ||listOne.get(3)!=listTwo.get(3) );
    }

    @Test
    void  testCreateCsvDataUserTable() throws ParseException {

        List<String[]> lists = CsvWriterSimple.createCsvDataUserTable();
        assertTrue(lists.get(0)[32]=="date");
        assertTrue(lists.get(0).length==33);
    }



}
