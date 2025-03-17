package com.vatsalkhanka.mednames;

import com.opencsv.CSVReader;
import java.util.*;

import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        //Read user input
        String medicine = new Scanner(System.in).next();

        //Read medicine database
        searchMedicine("C:/Users/cyno/IdeaProjects/MedNames/src/main/resources/meddb.csv", medicine);
    }

    public static void searchMedicine(String file, String medicine) {
        try {
            FileReader fileReader = new FileReader(file);

            CSVReader csvReader = new CSVReader(fileReader);
            String [] nextEntry;

            System.out.println("Searching medicines! \n ---------------------------------------");

            //Iterate through medicines
            while ((nextEntry = csvReader.readNext()) != null) {
                //Check name of medicine
                if (nextEntry[1].toLowerCase().contains(medicine.toLowerCase())) {
                    System.out.println("NAME: " + nextEntry[1]); //Name
                    System.out.println("PRICE: Rs." + nextEntry[2]); //Price
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