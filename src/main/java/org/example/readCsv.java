package org.example;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class readCsv {
    public static List<Country> getDataFromCsv() throws ParseException, IOException {
        List<String[]> collects =
                Files.lines(Paths.get("csvSources/countryAndContinent.csv"))
                        // skip the header
                        .skip(1)
                        .map(line -> line.split(","))
                        .collect(Collectors.toList());
        List<Country> countryList = new ArrayList<>();
        for (int i = 0; i < collects.size(); i++) {
            countryList.add(new Country(collects.get(i)[1],collects.get(i)[0]));
        }

        return countryList;
    }

    public static List<Country> getRandomizedCountryList(List<Country> countryList){

        Collections.shuffle(countryList);
        // 250*3 = 750 and 750 is larger than 731
        List <Country> randomizedMergedList = Stream.of(countryList.stream(),countryList.stream(),countryList.stream())
                .flatMap(Function.identity())
                .collect(Collectors.toList());

        return randomizedMergedList;
    }
}
