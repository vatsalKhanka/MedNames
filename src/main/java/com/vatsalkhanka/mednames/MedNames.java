package com.vatsalkhanka.mednames;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.*;
import java.util.Comparator;
import java.util.List;

public class MedNames {

    public static String output = "";
    public static final int MAX_SEARCHES = 30;
    public static LevenshteinDistance distance = new LevenshteinDistance();


    public static String searchMedicine(String query) {
        output = "";
        if(!searchMedCSV(query)) {
            output = "No medicines were found by the name \"" + query + "\". Did you make a typo?";
        }

        return output;
    }

    public static boolean searchMedCSV(String medicine) {
        boolean resultsFound = false;
        int resultsCount = 0;

        try {
            InputStream inputStream = MedNames.class.getClassLoader().getResourceAsStream("meddb.csv");
            if (inputStream == null) {
                throw new FileNotFoundException("meddb.csv not found in resources!");
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReader(reader);


            List<String[]> results = csvReader.readAll();
        results.sort(Comparator.comparingInt(entry -> distance.apply(medicine.toLowerCase(), entry[1].toLowerCase().substring(0,Math.min(entry[1].length()-1, medicine.length()))) + 2*distance.apply(String.valueOf(medicine.toLowerCase().charAt(0)), String.valueOf(entry[1].toLowerCase().charAt(0)))));

            output += "---------------------------------------------------------------------------------------------------- \n";

            for(String [] result : results) {
                if(result[1].contains("name")) continue;

                int endLength = medicine.length();
                if(endLength >= result[1].length()) endLength = result[1].length()-1;

                if (isValidLevenshtein(result[1].substring(0,endLength).toLowerCase(), medicine.toLowerCase())) {
                    resultsFound = true;
                    resultsCount++;
                    output += "NAME: " + result[1] +"\n"; //Name
                    output += "SALT COMPOSITION: "+ result[7]; //Composition I
                    output += "," + result[8]+//Composition II
                            "\n \n ---------------------------------------------------------------------------------------------------- \n";

                    if(resultsCount == MAX_SEARCHES) return true;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resultsFound;
    }

    public static boolean isValidLevenshtein(String str1, String str2) {
        return distance.apply(str1, str2) <= Math.max(1, str2.length()/3);
    }

    public static void addMed(String med, String salt) {
        try {
            CSVReader reader2 = new CSVReader(new InputStreamReader(MedNames.class.getClassLoader().getResourceAsStream("meddb.csv")));
            List<String[]> allElements = reader2.readAll();
            allElements.add(new String[]{"", med, "", "", "", "", "", salt, ""});
            CSVWriter writr = new CSVWriter(new FileWriter("src/main/resources/meddb.csv"));
            writr.writeAll(allElements);
            writr.flush();
            writr.close();
        } catch (IOException e) {

        }
    }
}
