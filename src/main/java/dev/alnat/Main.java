package dev.alnat;

import dev.alnat.ipcounter.BitSetIpCounter;
import dev.alnat.ipcounter.IPCounter;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        File file = new File(args[0]);
        IPCounter ipCounter = new BitSetIpCounter();

        long value;
        try {
            value = ipCounter.countUniqueIP(file, args[0].endsWith(".zip"));
        } catch (IOException e) {
            System.out.println("Exception " + e.getMessage());
            return;
        }

        System.out.println("Found " + value);
    }

}