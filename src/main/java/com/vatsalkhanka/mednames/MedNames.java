package com.vatsalkhanka.mednames;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.*;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;

public class MedNames {

    public static String output = "";

    public static final int MAX_SEARCHES = 30;
    public static final int BRAND_TO_GEN = 0;
    public static final int GEN_TO_BRAND = 1;


    public static int SEARCH_MODE = BRAND_TO_GEN;
    public static LevenshteinDistance distance = new LevenshteinDistance();

    //Method to search medicines
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

            CSVReader csvReader = new CSVReader(new FileReader(getOrCreateMedDB()));

            List<String[]> results = csvReader.readAll();

            if(SEARCH_MODE == BRAND_TO_GEN) {
                results.sort(Comparator.comparingInt(entry -> distance.apply(medicine.toLowerCase(), entry[1].toLowerCase().substring(0, Math.min(entry[1].length() - 1, medicine.length()))) + 2 * distance.apply(String.valueOf(medicine.toLowerCase().charAt(0)), String.valueOf(entry[1].toLowerCase().charAt(0)))));

                output += "---------------------------------------------------------------------------------------------------- \n";

                for (String[] result : results) {
                    if (result[1].contains("name")) continue;

                    int endLength = medicine.length();
                    if (endLength >= result[1].length()) endLength = result[1].length() - 1;

                    if (isValidLevenshtein(result[1].substring(0, endLength).toLowerCase(), medicine.toLowerCase())) {
                        resultsFound = true;
                        resultsCount++;
                        output += "NAME: " + result[1] + "\n"; //Name
                        output += "SALT COMPOSITION: " + result[7]; //Composition I
                        output += "," + result[8] +//Composition II
                                "\n \n ---------------------------------------------------------------------------------------------------- \n";

                        if (resultsCount == MAX_SEARCHES) return true;
                    }
                }
            } else {

                output += "---------------------------------------------------------------------------------------------------- \n";

                for (String[] result : results) {
                    if (result[1].contains("name")) continue;

                    String[] medicinesToCompare = (result[7] + "," + result[8]).split(",");

                    for(String salt : medicinesToCompare) {
                        int endLength = medicine.length();
                        if (endLength >= salt.length()) endLength = salt.length() - 1;

                        if (isValidLevenshtein(salt.substring(0, endLength).toLowerCase(), medicine.toLowerCase())) {
                            resultsFound = true;
                            resultsCount++;
                            output += "NAME: " + result[1] + "\n"; //Name
                            output += "SALT COMPOSITION: " + result[7]; //Composition I
                            output += "," + result[8] +//Composition II
                                    "\n \n ---------------------------------------------------------------------------------------------------- \n";

                            if (resultsCount == MAX_SEARCHES) return true;
                            break;
                        }
                    }
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
            CSVReader reader2 = new CSVReader(new FileReader(getOrCreateMedDB()));
            List<String[]> allElements = reader2.readAll();
            allElements.add(new String[]{"", med, "", "", "", "", "", salt, ""});
            CSVWriter writr = new CSVWriter(new FileWriter(getOrCreateMedDB()));
            writr.writeAll(allElements);
            writr.flush();
            writr.close();
        } catch (IOException e) {
            System.out.println("What the fuck bro");
        }
    }

    public static File getOrCreateMedDB() throws IOException {
        String userHome = System.getProperty("user.home");
        File dataFolder = new File(userHome, "MedNames/database");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File medFile = new File(dataFolder, "meddb.csv");

        if (!medFile.exists()) {
            try (InputStream in = MedNames.class.getClassLoader().getResourceAsStream("meddb.csv")) {
                if (in == null) throw new FileNotFoundException("Default meddb.csv not found in JAR!");
                Files.copy(in, medFile.toPath());
            }
        }

        return medFile;
    }


}
