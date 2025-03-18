package com.vatsalkhanka.mednames;

import com.opencsv.CSVReader;
import java.util.*;

import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        //Read user input
        System.out.println("Enter the generic name of your medicine: ");
        String medicine = new Scanner(System.in).next();

        MedNames.searchMedicine(medicine);
    }
}