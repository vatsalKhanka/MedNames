package org.example;

import com.opencsv.CSVReader;
import java.util.*;

import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String medicine = new Scanner(System.in).next();

        readFile("C:/Users/cyno/IdeaProjects/MedNames/src/main/resources/meddb.csv", medicine);
    }

    public static void readFile(String file, String medicine) {
        try {
            FileReader fileReader = new FileReader(file);

            CSVReader csvReader = new CSVReader(fileReader);
            String [] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                for (String field: nextLine) {
                    if (field.toLowerCase().contains(medicine.toLowerCase())) {
                        System.out.println(nextLine[1]);
                        System.out.println(nextLine[2]);
                        System.out.println(nextLine[7]);
                        System.out.println(nextLine[8]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}