package com.vatsalkhanka.mednames;

import com.opencsv.CSVReader;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;

public class MedNames {

    public static String output = "";

    public static String searchMedicine(String query) {
        output = "";
        if(!searchMedCSV(query)) {
            searchMedTata(query);
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
            for (Element link : links) {
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

                output += "\n -------------------------------------------------- \n";
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean searchMedCSV(String medicine) {

        boolean resultsFound = false;

        try {
            FileReader fileReader = new FileReader("src/main/resources/meddb.csv");

            CSVReader csvReader = new CSVReader(fileReader);
            String [] nextEntry;

            output += "-------------------------------------------------- \n";

            //Iterate through medicines
            while ((nextEntry = csvReader.readNext()) != null) {
                //Check name of medicines
                if (nextEntry[1].toLowerCase().contains(medicine.toLowerCase())) {
                    resultsFound = true;
                    output += "NAME: " + nextEntry[1] + "\n"; //Name
                    output += "SALT COMPOSITION: "+ nextEntry[7] + "\n"; //Composition I
                    output += nextEntry[8] + //Composition II
                            "\n -------------------------------------------------- \n";
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resultsFound;
    }
}
