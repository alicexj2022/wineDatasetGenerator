package org.example;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class readCsv {
    public static  List<String[]>  getDataFromCsv() throws ParseException, IOException {
    List<String[]> collect =
            Files.lines(Paths.get("csvSources/countryAndContinent.csv"))
                    // skip the header
                    .skip(1)
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
    return collect;
    }
}
