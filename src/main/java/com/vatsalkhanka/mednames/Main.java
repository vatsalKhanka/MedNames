package com.vatsalkhanka.mednames;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        //Read user input
        System.out.println("Enter the generic name of your medicine: ");
        String medicine = new Scanner(System.in).next();

        MedNames.searchMedicine(medicine);
    }
}