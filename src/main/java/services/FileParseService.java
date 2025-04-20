package services;

import models.SeedsLot;

import java.io.*;
import java.util.Arrays;

public class FileParseService {

    private FileParseService(){}

    public static SeedsLot[] parseSeedLots(String filepath) throws IOException {
        File file = new File(filepath);
        if(!file.exists()){
            throw new FileNotFoundException("The `seeds.csv` file is empty");
        }
        SeedsLot[] allSeedLots = new SeedsLot[10];
        int idx = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                allSeedLots[idx++] = SeedsLot.parseSeed(line);
            }
        }
        return Arrays.copyOfRange(allSeedLots, 0, idx);
    }
}
