package org.example;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvWriterSimple {

    private static final String COMMA = ",";
    private static final String DEFAULT_SEPARATOR = COMMA;
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
    private static final String NEW_LINE_UNIX = "\n";
    private static final String NEW_LINE_WINDOWS = "\r\n";
    private static List<String[]> visitorsTotalPerDate = new ArrayList<>();
    private static List<String[]> downloadedPerItem = new ArrayList<>();
    private static List<String[]> listPerDate = new ArrayList<>();
    private static List<String[]> listPerUser = new ArrayList<>();
    private static List<LocalDate> dateHiarachy = new ArrayList<>();


    public static void main(String[] args) throws IOException, ParseException {

        CsvWriterSimple writer = new CsvWriterSimple();
        writer.writeToCsvFile(createCsvDataSpecial(), new File("awareness.csv"));

        CsvWriterSimple writer2 = new CsvWriterSimple();
        writer2.writeToCsvFile(createCsvDataUserTable(), new File("referal.csv"));

    }

    public String convertToCsvFormat(final String[] line) {
        return convertToCsvFormat(line, DEFAULT_SEPARATOR);
    }

    public String convertToCsvFormat(final String[] line, final String separator) {
        return convertToCsvFormat(line, separator, true);
    }

    // if quote = true, all fields are enclosed in double quotes
    public String convertToCsvFormat(
            final String[] line,
            final String separator,
            final boolean quote) {

        return Stream.of(line)                              // convert String[] to stream
                .map(l -> formatCsvField(l, quote))         // format CSV field
                .collect(Collectors.joining(separator));    // join with a separator

    }

    // put your extra login here
    private String formatCsvField(final String field, final boolean quote) {

        String result = field;

        if (result.contains(COMMA)
                || result.contains(DOUBLE_QUOTES)
                || result.contains(NEW_LINE_UNIX)
                || result.contains(NEW_LINE_WINDOWS)) {

            // if field contains double quotes, replace it with two double quotes \"\"
            result = result.replace(DOUBLE_QUOTES, EMBEDDED_DOUBLE_QUOTES);

            // must wrap by or enclosed with double quotes
            result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;

        } else {
            // should all fields enclosed in double quotes
            if (quote) {
                result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
            }
        }

        return result;

    }

    // a standard FileWriter, CSV is a normal text file
    private void writeToCsvFile(List<String[]> list, File file) throws IOException {

        List<String> collect = list.stream()
                .map(this::convertToCsvFormat)
                .collect(Collectors.toList());

        // CSV is a normal text file, need a writer
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : collect) {
                bw.write(line);
                bw.newLine();
            }
        }

    }

    private static List<String[]> createCsvVisitorsTotalPerDate() throws ParseException {
        // Used for downloaded per visitors
        // downloaded total should be less than visitors total
        dateHiarachy = dataGenerator.getDates("7-Jun-2020", "8-Jun-2022");
        int numberOfElements = 731;

        // Visitors Per day for app store
        List<Integer> vApp = dataGenerator.generateRandomizedIntegerList("scale3", numberOfElements);
        List<Integer> vGoogle = dataGenerator.generateRandomizedIntegerList("scale3", numberOfElements);

        String[] header = {"VisitorsApp", "VisitorsGoogle", "DownloadsGoogle", "DownloadsApp", "date"};
        visitorsTotalPerDate.add(header);
        for (int i = 0; i < dateHiarachy.size() - 1; i++) {

            String[] record = {vApp.get(i).toString(), vGoogle.get(i).toString(), dateHiarachy.get(i).toString()};
            visitorsTotalPerDate.add(record);
        }
        return visitorsTotalPerDate;
    }

    private static List<String[]> createCsvVisitorsDownloadedDetailed() throws ParseException {
        // Used for downloaded per visitors
        // downloaded total should be less than visitors total
        int numberOfElements = 731;

        for (int i = 1; i < visitorsTotalPerDate.size(); i++) {
            String[] currentRow = visitorsTotalPerDate.get(i);
            String currentDate = currentRow[2];
            System.out.println("current DATE is :" + currentDate);
            int currentVisitorsSumGoogle = Integer.parseInt(currentRow[1]);
            int currentVisitorsSumApp = Integer.parseInt(currentRow[0]);
            List<Integer> dApp = dataGenerator.generateRandomizedIntegerList("scale6", currentVisitorsSumGoogle);
            List<Integer> dGoogle = dataGenerator.generateRandomizedIntegerList("scale6", currentVisitorsSumApp);
            String[] header = {"VisitorsApp", "VisitorsGoogle", "DownloadsGoogle", "DownloadsApp", "RegisteredApp", "RegisteredGoogle", "date"};
            for (int x = 0; x < currentVisitorsSumGoogle; x++) {
                int isDownloadedFromGoogle = dGoogle.get(x);
                int registeredFromGoogle = (int) Math.round(Math.random());
                int currentRegistered = 0;
                if (isDownloadedFromGoogle == 1) {
                    currentRegistered = registeredFromGoogle;
                }
                String[] record = {"0", "1",  Integer.toString(isDownloadedFromGoogle),"0", "0",Integer.toString(currentRegistered), currentDate};
                downloadedPerItem.add(record);
            }

            for (int y = 0; y < currentVisitorsSumApp; y++) {
                int isDownloaded = dApp.get(y);
                int isRegistered = (int) Math.round(Math.random());
                int currentRegistered = 0;
                if (isDownloaded == 1) {
                    currentRegistered = isRegistered;
                }
                String[] record = {"1", "0", "0", dApp.get(i).toString(), Integer.toString(currentRegistered), "0",currentDate};
                downloadedPerItem.add(record);
            }
        }
        return downloadedPerItem;
    }

    private static List<String[]> createCsvDataSpecial() throws ParseException {

        List<LocalDate> dateHiarachy = dataGenerator.getDates("7-Jun-2020", "8-Jun-2022");
        int numberOfElements = 731;

        List<Integer> list1 = dataGenerator.generateRandomizedIntegerList("scale1", numberOfElements);
        List<Integer> list2 = dataGenerator.generateRandomizedIntegerList("scale2", numberOfElements);
        List<Integer> list3 = dataGenerator.generateRandomizedIntegerList("scale1", numberOfElements);
        List<Integer> list4 = dataGenerator.generateRandomizedIntegerList("scale2", numberOfElements);
        List<Integer> list5 = dataGenerator.generateRandomizedIntegerList("scale1", numberOfElements);
        List<Integer> list6 = dataGenerator.generateRandomizedIntegerList("scale2", numberOfElements);
        List<Integer> list7 = dataGenerator.generateRandomizedIntegerList("scale1", numberOfElements);


        String[] header = {"SearchGoogle", "SearchApp", "PromoGoogle", "PromoFacebook", "uniqueApp", "uniqueFacebook", "visitorsFromVintec", "date"};
        listPerDate.add(header);
        for (int i = 0; i < dateHiarachy.size() - 1; i++) {

            String[] record = {list1.get(i).toString(), list2.get(i).toString(),
                    list3.get(i).toString(), list4.get(i).toString(), list5.get(i).toString(),
                    list6.get(i).toString(), list7.get(i).toString(), dateHiarachy.get(i).toString()};
            listPerDate.add(record);
        }
        return listPerDate;
    }

    public static List<String[]> createCsvDataUserTable() throws ParseException {
        // Get users registered count by day
        // generate user id using UUID
        // generate other field values cost, sales, as numbers and cost< sales, appInvitesSentOut, acceptedInvites, ReviewOnAppStore, ReviewOnGoogle(0-9), averageReview(0<x<5), shareWine, shareWineList
        // addNewCellerFirst24Hours, logged5FirstWeek, scanned10FirstWeek, ratedFirstWeek, weeklyAcitve, monthlyActive, drinkFirstMonth, 1DayRetention, 1WeekRetention, 1MonthRetention, winBack, inactive, as 1 or 0,
        // ARPU
        // ER USER, leadToVivino, leadToVintec, leadToElectrolux, leadToAEG, registeredForWarrantyVintec, registeredForWarrantyAEG, registeredForWarrantyElux,becomeVinClub, isVintec
        String[] header = {"id", "cost", "sales", "appInvitesSentOut", "acceptedInvites", "ReviewOnAppStore", "ReviewOnGoogle", "averageReview", "shareWine", "shareWineList", "ARPU",
                "addNewCellerFirst24Hours", "logged5FirstWeek", "scanned10FirstWeek", "ratedFirstWeek", "weeklyAcitve", "monthlyActive", "drinkFirstMonth", "1DayRetention", "1WeekRetention", "1MonthRetention", "winBack", "inactive",
                "leadToVivino", "leadToVintec", "leadToElectrolux", "leadToAEG", "registeredForWarrantyVintec", "registeredForWarrantyAEG", "registeredForWarrantyElux", "becomeVinClub", "isVintec", "date"};
        System.out.println("header length is: " + header.length);
        listPerUser.add(header);


        List<Integer> costList = new ArrayList<>();
        List<Integer> salesList = new ArrayList<>();
        List<Integer> invitesSentOutList = new ArrayList<>();
        List<Integer> acceptedInvitesList = new ArrayList<>();
        List<Integer> reviewOnAppStoreList = new ArrayList<>();
        List<Integer> reviewOnGoogleList = new ArrayList<>();

        List<Integer> averageReviewList = new ArrayList<>();
        List<Integer> shareWineList = new ArrayList<>();
        List<Integer> shareWineListList = new ArrayList<>();
        List<Integer> ARPUList = new ArrayList<>();


        List<Integer> addNewCellerFirst24HoursList = new ArrayList<>();
        List<Integer> logged5FirstWeekList = new ArrayList<>();
        List<Integer> scanned10FirstWeekList = new ArrayList<>();
        List<Integer> ratedFirstWeekList = new ArrayList<>();
        List<Integer> weeklyAcitveList = new ArrayList<>();
        List<Integer> monthlyActiveList = new ArrayList<>();
        List<Integer> drinkFirstMonthList = new ArrayList<>();
        List<Integer> firstDayRetentionList = new ArrayList<>();
        List<Integer> firstWeekRetentionList = new ArrayList<>();
        List<Integer> firstMonthRetentionList = new ArrayList<>();


        List<Integer> winBackList = new ArrayList<>();
        List<Integer> inactiveList = new ArrayList<>();
        List<Integer> leadToVivinoList = new ArrayList<>();
        List<Integer> leadToVintecList = new ArrayList<>();
        List<Integer> leadToElectroluxList = new ArrayList<>();
        List<Integer> leadToAEGList = new ArrayList<>();
        List<Integer> registeredForWarrantyVintecList = new ArrayList<>();
        List<Integer> registeredForWarrantyAEGList = new ArrayList<>();
        List<Integer> registeredForWarrantyEluxList = new ArrayList<>();
        List<Integer> becomeVinClubList = new ArrayList<>();
        List<Integer> isVinteList = new ArrayList<>();


        for (int i = 1; i < listPerDate.size(); i++) {
            String[] currentRow = listPerDate.get(i);
            String currentDate = currentRow[12];
            System.out.println("current DATE is :" + currentDate);
            int currentRegisteredCount = Integer.parseInt(currentRow[4]);

            costList = dataGenerator.generateRandomizedIntegerList("scale1", currentRegisteredCount);
            salesList = dataGenerator.generateRandomizedIntegerList("scale2", currentRegisteredCount);
            invitesSentOutList = dataGenerator.generateRandomizedIntegerList("scale5", currentRegisteredCount);
            acceptedInvitesList = dataGenerator.generateRandomizedIntegerList("scale4", currentRegisteredCount);
            reviewOnAppStoreList = dataGenerator.generateRandomizedIntegerList("scale5", currentRegisteredCount);
            reviewOnGoogleList = dataGenerator.generateRandomizedIntegerList("scale5", currentRegisteredCount);


            averageReviewList = dataGenerator.generateRandomizedIntegerList("scale4", currentRegisteredCount);
            shareWineList = dataGenerator.generateRandomizedIntegerList("scale5", currentRegisteredCount);
            shareWineListList = dataGenerator.generateRandomizedIntegerList("scale5", currentRegisteredCount);
            ARPUList = dataGenerator.generateRandomizedIntegerList("scale3", currentRegisteredCount);


            addNewCellerFirst24HoursList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            logged5FirstWeekList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            scanned10FirstWeekList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            ratedFirstWeekList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            weeklyAcitveList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            monthlyActiveList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            drinkFirstMonthList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            firstDayRetentionList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            firstWeekRetentionList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            firstMonthRetentionList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);


            winBackList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            inactiveList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            leadToVivinoList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            leadToVintecList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            leadToElectroluxList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            leadToAEGList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            registeredForWarrantyVintecList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            registeredForWarrantyAEGList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            registeredForWarrantyEluxList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            becomeVinClubList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);
            isVinteList = dataGenerator.generateRandomizedIntegerList("scale6", currentRegisteredCount);

            System.out.println("isVinteList len is :" + isVinteList.size());

            System.out.println("header length is: " + header.length);

            for (int x = 0; x < currentRegisteredCount; x++) {
                String id = UUID.randomUUID().toString();
                String cost = costList.get(x).toString();
                String sales = salesList.get(x).toString();
                String invitesSentOut = invitesSentOutList.get(x).toString();
                String acceptedInvites = acceptedInvitesList.get(x).toString();
                String reviewOnAppStore = reviewOnAppStoreList.get(x).toString();
                String reviewOnGoogle = reviewOnGoogleList.get(x).toString();
                String averageReview = averageReviewList.get(x).toString();
                String shareWine = shareWineList.get(x).toString();
                String shareWineL = shareWineListList.get(x).toString();
                String ARPU = ARPUList.get(x).toString();
                String addNewCellerFirst24Hours = addNewCellerFirst24HoursList.get(x).toString();
                String logged5FirstWeek = logged5FirstWeekList.get(x).toString();
                String scanned10FirstWeek = scanned10FirstWeekList.get(x).toString();
                String ratedFirstWeek = ratedFirstWeekList.get(x).toString();
                String weeklyAcitve = weeklyAcitveList.get(x).toString();
                String monthlyActive = monthlyActiveList.get(x).toString();
                String drinkFirstMonth = drinkFirstMonthList.get(x).toString();
                String firstDayRetention = firstDayRetentionList.get(x).toString();
                String firstWeekRetention = firstWeekRetentionList.get(x).toString();
                String firstMonthRetention = firstMonthRetentionList.get(x).toString();
                String winBack = winBackList.get(x).toString();
                String inactive = inactiveList.get(x).toString();
                String leadToVivino = leadToVivinoList.get(x).toString();
                String leadToVintec = leadToVintecList.get(x).toString();
                String leadToElectrolux = leadToElectroluxList.get(x).toString();
                String leadToAEG = leadToAEGList.get(x).toString();
                String registeredForWarrantyVintec = registeredForWarrantyVintecList.get(x).toString();
                String registeredForWarrantyAEG = registeredForWarrantyAEGList.get(x).toString();
                String registeredForWarrantyElux = registeredForWarrantyEluxList.get(x).toString();
                String becomeVinClub = becomeVinClubList.get(x).toString();
                String isVinte = isVinteList.get(x).toString();


                String[] record = {id, cost, sales,
                        invitesSentOut, acceptedInvites, reviewOnAppStore, reviewOnGoogle,
                        averageReview, shareWine, shareWineL,
                        ARPU, addNewCellerFirst24Hours, logged5FirstWeek,
                        scanned10FirstWeek,
                        ratedFirstWeek,
                        weeklyAcitve,
                        monthlyActive,
                        drinkFirstMonth,
                        firstDayRetention,
                        firstWeekRetention,
                        firstMonthRetention,
                        winBack,
                        inactive,
                        leadToVivino,
                        leadToVintec,
                        leadToElectrolux,
                        leadToAEG,
                        registeredForWarrantyVintec,
                        registeredForWarrantyAEG,
                        registeredForWarrantyElux,
                        becomeVinClub,
                        isVinte,
                        currentDate
                };

                listPerUser.add(record);
            }
        }
        return listPerUser;
    }
}
