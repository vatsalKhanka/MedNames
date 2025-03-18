package com.vatsalkhanka.mednames;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

public class Scraper {

    public Scraper() {

    }

    public String[] getMedicineNames(String brandName) {
        String searchPageLink = "https://www.1mg.com/search/all?name=" + URLEncoder.encode(brandName);
        try {
            Document searchPage = Jsoup.connect(searchPageLink).get();
            Elements links = searchPage.select("a[href^='/drugs/']"); // Selects only links that start with "/drugs/"

            // Print each link found
            for (Element link : links) {
                String relativeUrl = link.attr("href"); // Get the href value
                String fullUrl = "https://www.1mg.com" + relativeUrl; // Append the base URL

                Document medPage = Jsoup.connect(fullUrl).get();

                Elements salts = medPage.select("div.saltInfo.DrugHeader__meta-value___vqYM0 a[href]");
                Element drugNameElement = medPage.selectFirst("h1.DrugHeader__title-content___2ZaPo");

                if (drugNameElement != null) {
                    String drugName = drugNameElement.text(); // Get the text inside <h1>
                    System.out.println("NAME: " + drugName);
                } else {
                    System.out.println("Drug name not found!");
                }

                for (Element salt : salts) {
                    String saltName = salt.text(); // The name inside <a>

                    System.out.println("SALT COMPOSITION: " + saltName);
                }

                System.out.println("\n -------------------------------------------------- \n");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return null;
    }
}
