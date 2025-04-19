package services;

import models.Seed;

import java.io.*;
import java.util.Arrays;

public class FileParseService {

    private FileParseService(){}

    public static Seed[] parseSeedLots(String filepath) throws IOException {
        File file = new File(filepath);
        if(!file.exists()){
            throw new FileNotFoundException("The `seed.csv` file is empty");
        }
        Seed[] allSeedLots = new Seed[10];
        int idx = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                allSeedLots[idx++] = Seed.parseSeed(line);
            }
        }
        return Arrays.copyOfRange(allSeedLots, 0, idx);
    }
}
