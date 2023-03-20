package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class dataGenerator {
    public static HashMap<String, List<Integer>> getDaysBetweenDates(String s, String e) throws ParseException {
        HashMap<String, List<Integer>> dateHiarachy  = new HashMap<String, List<Integer>>();
        List<Integer> months = new ArrayList<>();
        List<Integer> weeks = new ArrayList<>();
        List<Integer> years = new ArrayList<>();
        List<Integer> datesOfmonth = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date startdate = formatter.parse(s);
        Date enddate = formatter.parse(e);

        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            months.add(calendar.get(Calendar.MONTH));
            years.add(calendar.get(Calendar.YEAR));
            weeks.add(calendar.get(Calendar.WEEK_OF_YEAR));
            datesOfmonth .add(calendar.get(Calendar.DATE));
            calendar.add(Calendar.DATE, 1);
        }

        dateHiarachy.put("months",months);
        dateHiarachy.put("years",years);
        dateHiarachy.put("weeks",weeks);
        dateHiarachy.put("dates",datesOfmonth );
        return dateHiarachy;
    }
    public static  List<LocalDate> getDates(String s, String e) throws ParseException {

        LocalDate localDateStart = LocalDate.of(2020, Month.JUNE, 7); // today's date
        System.out.println("Course Start date is :- \n" + localDateStart);


        // 2. Course end date -> Calendar
        LocalDate localDateEnd = LocalDate.of(2022, Month.JUNE, 8); // future date
        System.out.println("\nCourse End date is :- \n" + localDateEnd);


        // 3. get number of Days between start/end LocalDate
        long days = ChronoUnit.DAYS.between(localDateStart, localDateEnd);
        System.out.println("\nNumber of Days between (" + localDateStart
                + ") & (" +  localDateEnd + ") is :- \n" + days);


        // 4. get all Dates in List
        List<LocalDate> dateList = Stream
                .iterate(localDateStart, localDate -> localDate.plusDays(1))
                .limit(days)
                .collect(Collectors.toList());


        // 5. print all dates to console
        System.out.println("\nDates between (" + localDateStart + ") & (" + localDateEnd + ") is :- ");
        dateList.forEach(System.out::println);
        return dateList;
    }



    public static List<Integer> generateRandomizedIntegerList(String scale, int numberOfElements) {

        List<Integer> givenList = new ArrayList<Integer>();
        switch (scale)
        {
            //comparing value of variable against each case
            case "scale1":
                givenList = Arrays.asList(20,30,40,50);
                break;
            case "scale2":
                givenList = Arrays.asList(60,70,80,90);
                break;
            case "scale3":
                givenList = Arrays.asList(200,400,100);
                break;
            case "scale4":
                givenList = Arrays.asList(1,2,3,4,5);
                break;
            case "scale5":
                givenList = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
                break;
            case "scale6":
                givenList = Arrays.asList(0,1);
                break;
            //optional
            default:
                System.out.println("Invalid Input!");
        }

        Random rand = new Random();
        List<Integer> results = new ArrayList<>();;

        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(givenList.size());
            int randomElement = givenList.get(randomIndex);
            results.add(randomElement);
        }

        return results;
    }
}
