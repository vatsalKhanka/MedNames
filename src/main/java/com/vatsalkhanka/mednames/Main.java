package com.vatsalkhanka.mednames;

import com.opencsv.CSVReader;

import java.sql.SQLOutput;
import java.util.*;

import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        //Window window = new Window();
        //Read user input
        System.out.println("Enter the generic name of your medicine: ");
        String medicine = new Scanner(System.in).next();
        //String medicine = window.getMedicineInput();

        //Read medicine database
        readFile("src/main/resources/meddb.csv", medicine);
        new Scraper().getMedicineNames(medicine);
    }

    public static void readFile(String file, String medicine) {
        try {
            FileReader fileReader = new FileReader(file);

            CSVReader csvReader = new CSVReader(fileReader);
            String [] nextEntry;

            System.out.println("Searching medicines! \n --------------------------------------------------");

            //Iterate through medicines
            while ((nextEntry = csvReader.readNext()) != null) {
                //Check name of medicine
                if (nextEntry[1].toLowerCase().contains(medicine.toLowerCase())) {
                    System.out.println("NAME: " + nextEntry[1]); //Name
                    System.out.println("SALT COMPOSITION: "+ nextEntry[7]); //Composition I
                    System.out.println(nextEntry[8] + //Composition II
                            "\n -------------------------------------------------- \n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}