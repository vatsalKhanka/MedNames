package com.vatsalkhanka.mednames;

import com.opencsv.CSVReader;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URLEncoder;
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

    public static void searchMedTata(String brandName) {
        //Search Tata 1mg
        String searchPageLink = "https://www.1mg.com/search/all?name=" + URLEncoder.encode(brandName);

        try {
            Document searchPage = Jsoup.connect(searchPageLink).get();

            //Find links to all results
            Elements links = searchPage.select("a[href^='/drugs/']");

            //Iterate through all links
            for (int i = 0; i < Math.clamp(links.size(), 0, MAX_SEARCHES); i++) {
                Element link = links.get(i);
                String relativeUrl = link.attr("href");
                String fullUrl = "https://www.1mg.com" + relativeUrl;

                //Connect to link
                Document medPage = Jsoup.connect(fullUrl).get();

                //Find medicine data
                Elements salts = medPage.select("div.saltInfo.DrugHeader__meta-value___vqYM0 a[href]");
                Element drugNameElement = medPage.selectFirst("h1.DrugHeader__title-content___2ZaPo");

                if (drugNameElement != null) {
                    String drugName = drugNameElement.text();
                    output += "NAME: " + drugName + "\n";
                } else {
                    System.out.println("Drug name not found!");
                }

                for (Element salt : salts) {
                    String saltName = salt.text();

                    output += "SALT COMPOSITION: " + saltName + "\n";
                }

                output += "\n --------------------------------------------------------------------------- \n";
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
