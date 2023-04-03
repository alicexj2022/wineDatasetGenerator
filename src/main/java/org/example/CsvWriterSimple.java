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

import static org.example.readCsv.getRandomizedCountryList;

public class CsvWriterSimple {

    private static final String COMMA = ",";
    private static final String DEFAULT_SEPARATOR = COMMA;
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
    private static final String NEW_LINE_UNIX = "\n";
    private static final String NEW_LINE_WINDOWS = "\r\n";
    private static List<String[]> TotalPerDate = new ArrayList<>();
    private static List<String[]> downloadedPerItem = new ArrayList<>();
    private static List<String[]> listPerDate = new ArrayList<>();
    private static List<LocalDate> dateHiarachy = new ArrayList<>();


    public static void main(String[] args) throws IOException, ParseException {


        CsvWriterSimple writer1 = new CsvWriterSimple();
        writer1.writeToCsvFile(createCsvAwarenessDetailed(), new File("awarenessDetailed.csv"));

        List<String[]> TotalPerDate = createCsvVisitorsTotalPerDate();
        List<String[]> registeredPerDateAndPerSource = createCsvVisitorsDownloadedDetailed( TotalPerDate);

        CsvWriterSimple writer = new CsvWriterSimple();
        writer.writeToCsvFile(registeredPerDateAndPerSource, new File("downloaded.csv"));

        CsvWriterSimple writer2 = new CsvWriterSimple();
        writer2.writeToCsvFile(createCsvDataUserTable(TotalPerDate), new File("users.csv"));

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

    public static List<String[]> createCsvVisitorsTotalPerDate() throws ParseException {
        // Used for downloaded per visitors
        // downloaded total should be less than visitors total
        dateHiarachy = dataGenerator.getDates("7-Jun-2020", "8-Jun-2022");
        int numberOfElements = 731;

        // Visitors Per day for app store
        List<Integer> vApp = dataGenerator.generateRandomizedIntegerList("scale3", numberOfElements);
        List<Integer> vGoogle = dataGenerator.generateRandomizedIntegerList("scale3", numberOfElements);
        List<Integer> rApp = dataGenerator.generateRandomizedIntegerList("scale3", numberOfElements);
        List<Integer> rGoogle = dataGenerator.generateRandomizedIntegerList("scale3", numberOfElements);
        // User for generating users table, radomized total of how many visitors per day
        List<Integer> visitorsPerDay = dataGenerator.generateRandomizedIntegerList("scale3", numberOfElements);

        // fake the registeredApp and RegisteredGoogle, TODO, The two values should be calcualted through group by from getRegisteredBySourceAndDate
        String[] header = {"VisitorsApp", "VisitorsGoogle", "RegisteredApp","","RegisteredGoogle","date"};
        TotalPerDate.add(header);
        for (int i = 0; i < dateHiarachy.size() - 1; i++) {

            String[] record = {
                    vApp.get(i).toString(), vGoogle.get(i).toString(), rApp.get(i).toString(), rGoogle.get(i).toString(), dateHiarachy.get(i).toString()};
            TotalPerDate.add(record);
        }
        return TotalPerDate;
    }

    public static List<String[]> createCsvVisitorsDownloadedDetailed(List<String[]> TotalPerDate) throws ParseException, IOException {
        // Used for downloaded per visitors
        // downloaded total should be less than visitors total
        // int numberOfElements = 731;
        String[] header = {"VisitorsApp", "VisitorsGoogle", "DownloadsGoogle", "DownloadsApp", "RegisteredApp", "RegisteredGoogle", "country","continent","date"};
        downloadedPerItem.add(header);
        List<Country> countryList  = readCsv.getDataFromCsv();

        for (int i = 1; i < TotalPerDate.size(); i++) {
            List<Country> randomizedCountryList = getRandomizedCountryList(countryList);
            String[] currentRow = TotalPerDate.get(i);
            String currentDate = currentRow[4];
            System.out.println("current DATE is :" + currentDate);
            int currentVisitorsSumGoogle = Integer.parseInt(currentRow[1]);
            int currentVisitorsSumApp = Integer.parseInt(currentRow[0]);
            List<Integer> dApp = dataGenerator.generateRandomizedIntegerList("scale6", currentVisitorsSumApp);
            List<Integer> dGoogle = dataGenerator.generateRandomizedIntegerList("scale6", currentVisitorsSumGoogle);
            System.out.println("dApp size"+ dApp.size());
            System.out.println("dGoogle size"+ dGoogle.size());


            // TODO: add the country per row with random value here
            for (int j = 0; j <= dGoogle.size()-1; j++) {
                int isDownloadedFromGoogle = dGoogle.get(j);
                int registeredFromGoogle = (int) Math.round(Math.random());
                int currentRegistered = 0;
                if (isDownloadedFromGoogle == 1) {
                    currentRegistered = registeredFromGoogle;
                }
                String[] record = {"0", "1", Integer.toString(isDownloadedFromGoogle), "0", "0", Integer.toString(currentRegistered),randomizedCountryList.get(j).getName(),
                randomizedCountryList.get(j).getContinent(), currentDate};
                downloadedPerItem.add(record);
            }

            for (int y = 0; y <= dApp.size()-1; y++) {
                int isDownloaded = dApp.get(y);
                int isRegistered = (int) Math.round(Math.random());
                int currentRegistered = 0;
                if (isDownloaded == 1) {
                    currentRegistered = isRegistered;
                }
                String[] record = {"1", "0", "0", Integer.toString(isDownloaded), Integer.toString(currentRegistered), "0", randomizedCountryList.get(y).getName(),
                        randomizedCountryList.get(y).getContinent(),currentDate};
                downloadedPerItem.add(record);
            }
        }
        return downloadedPerItem;
    }

    private static List<String[]> createCsvAwarenessDetailed() throws ParseException, IOException {
        List<String[]> awarenessDetailed =  new ArrayList<>();
        // For total daily awareness, for simplicity, create the one total for all the awareness entries, the randomness of total number is implemented in a broader scope.
        List<Integer> awarenessTotalByDate = dataGenerator.generateRandomizedIntegerList("scale1", 731);
        String[] header = {"SearchApp", "SearchGoogle", "PromoGoogle", "PromoApp", "UniqueApp", "UniqueFacebook", "VisitorsFromVintec","date", "country","continent"};
        List<Country> countryList  = readCsv.getDataFromCsv();
        dateHiarachy = dataGenerator.getDates("7-Jun-2020", "8-Jun-2022");
        for (int i = 1; i < 731; i++) {
            int todayTotalItems = awarenessTotalByDate.get(i);
            String currentDate = dateHiarachy.get(i).toString();
            List<Integer> radomizedValue1 = dataGenerator.generateRandomizedIntegerList("scale6", todayTotalItems);
            List<Integer> radomizedValue2 = dataGenerator.generateRandomizedIntegerList("scale6", todayTotalItems);
            List<Integer> radomizedValue3 = dataGenerator.generateRandomizedIntegerList("scale6", todayTotalItems);
            List<Integer> radomizedValue4 = dataGenerator.generateRandomizedIntegerList("scale6", todayTotalItems);
            List<Integer> radomizedValue5 = dataGenerator.generateRandomizedIntegerList("scale6", todayTotalItems);
            List<Integer> radomizedValue6 = dataGenerator.generateRandomizedIntegerList("scale6", todayTotalItems);
            List<Integer> radomizedValue7 = dataGenerator.generateRandomizedIntegerList("scale6", todayTotalItems);
            List<Country> randomizedCountryList = getRandomizedCountryList(countryList);
            for (int x = 1; x < todayTotalItems; x++) {
                String[] record ={radomizedValue1.get(x).toString(),
                        radomizedValue1.get(x).toString(),
                        radomizedValue2.get(x).toString(),
                        radomizedValue3.get(x).toString(),
                        radomizedValue4.get(x).toString(),
                        radomizedValue5.get(x).toString(),
                        radomizedValue6.get(x).toString(),
                        radomizedValue7.get(x).toString(),
                        randomizedCountryList.get(x).getName(),
                        randomizedCountryList.get(x).getContinent(),
                        currentDate};
                awarenessDetailed.add(record );
            }
        }
          return awarenessDetailed;
        }


    public static List<String[]> createCsvDataUserTable( List<String[]> listPerDate) throws ParseException, IOException {
        List<Country> countryList  = readCsv.getDataFromCsv();
        // Get users registered count by day
        // generate user id using UUID
        // generate other field values cost, sales, as numbers and cost< sales, appInvitesSentOut, acceptedInvites, ReviewOnAppStore, ReviewOnGoogle(0-9), averageReview(0<x<5), shareWine, shareWineList
        // addNewCellerFirst24Hours, logged5FirstWeek, scanned10FirstWeek, ratedFirstWeek, weeklyAcitve, monthlyActive, drinkFirstMonth, 1DayRetention, 1WeekRetention, 1MonthRetention, winBack, inactive, as 1 or 0,
        // ARPU
        // ER USER, leadToVivino, leadToVintec, leadToElectrolux, leadToAEG, registeredForWarrantyVintec, registeredForWarrantyAEG, registeredForWarrantyElux,becomeVinClub, isVintec
        String[] header = {"id", "cost", "sales", "appInvitesSentOut", "acceptedInvites", "ReviewOnAppStore", "ReviewOnGoogle", "averageReview", "shareWine", "shareWineList", "ARPU",
                "addNewCellerFirst24Hours", "logged5FirstWeek", "scanned10FirstWeek", "ratedFirstWeek", "weeklyAcitve", "monthlyActive", "drinkFirstMonth", "1DayRetention", "1WeekRetention", "1MonthRetention", "winBack", "inactive",
                "leadToVivino", "leadToVintec", "leadToElectrolux", "leadToAEG", "registeredForWarrantyVintec", "registeredForWarrantyAEG", "registeredForWarrantyElux", "becomeVinClub", "isVintec", "country","continent","date"};
        System.out.println("888888  header length is: " + header.length);
        List<String[]> listPerUser = new ArrayList<>();
        listPerUser.add(header);
        for (int i = 1; i < listPerDate.size(); i++) {
            List<Country> randomizedCountryList = getRandomizedCountryList(countryList);
            System.out.println("listPerDate.get(i) :" + listPerDate.get(i).length);
            String[] currentRow = listPerDate.get(i);
            String currentDate = currentRow[4];
            System.out.println("current DATE is :" + currentDate);
            int currentRegisteredCount = Integer.parseInt(currentRow[3]);

            List<Integer> costList = dataGenerator.generateRandomizedIntegerList("scale1", currentRegisteredCount);
            List<Integer> salesList = dataGenerator.generateRandomizedIntegerList("scale2", currentRegisteredCount);
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
                        randomizedCountryList.get(x).getName(),
                        randomizedCountryList.get(x).getContinent(),
                        currentDate
                };

                listPerUser.add(record);
            }
        }
        return listPerUser;
    }
}

