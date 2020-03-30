import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main implements Runnable {
    private String name;
    private int index;
    private int tid;


    public Main(String name, int index, int i) {
        this.name = name;
        this.index = index;
        this.tid = i;
    }

    public static void main(String[] args) {
        boolean debug = false;

        ArrayList<List<Object>> files = new ArrayList<>();
        //done locally

        //files.add(Arrays.asList("blob_seeds20.txt", 20));
        //files.add(Arrays.asList("blob_seeds35.txt", 35));
        //files.add(Arrays.asList("blob_seeds36.txt", 36));
        //files.add(Arrays.asList("blob_seeds52.txt", 52));
        //files.add(Arrays.asList("blob_seeds84.txt", 84));
        //files.add(Arrays.asList("blob_seeds93.txt", 93));
        //files.add(Arrays.asList("blob_seeds99.txt", 99));
        //files.add(Arrays.asList("blob_seeds100.txt", 100));
        //files.add(Arrays.asList("blob_seeds115.txt", 115));
        //files.add(Arrays.asList("blob_seeds116.txt", 116));
        //files.add(Arrays.asList("blob_seeds147.txt", 147));
        //files.add(Arrays.asList("blob_seeds164.txt", 164));
//
       files.add(Arrays.asList("blob_seeds112.txt", 112));
       files.add(Arrays.asList("blob_seeds114.txt", 114));
       files.add(Arrays.asList("blob_seeds128.txt", 128));
       files.add(Arrays.asList("blob_seeds146.txt", 146));
       files.add(Arrays.asList("blob_seeds16.txt", 16));
       files.add(Arrays.asList("blob_seeds160.txt", 160));
       files.add(Arrays.asList("blob_seeds177.txt", 177));
       files.add(Arrays.asList("blob_seeds18.txt", 18));
       files.add(Arrays.asList("blob_seeds34.txt", 34));
       files.add(Arrays.asList("blob_seeds49.txt", 49));
       files.add(Arrays.asList("blob_seeds50.txt", 50));
       files.add(Arrays.asList("blob_seeds66.txt", 66));
       files.add(Arrays.asList("blob_seeds80.txt", 80));
       files.add(Arrays.asList("blob_seeds96.txt00", 96));
       files.add(Arrays.asList("blob_seeds98.txt00", 98));

       // files.add(Arrays.asList("seeds100.txt00", 100));
       // files.add(Arrays.asList("seeds100.txt01", 100));
       // files.add(Arrays.asList("seeds100.txt02", 100));
       // files.add(Arrays.asList("seeds100.txt03", 100));
       // files.add(Arrays.asList("seeds100.txt04", 100));
//
       // files.add(Arrays.asList("seeds101.txt00",101));
       // files.add(Arrays.asList("seeds101.txt01",101));
       // files.add(Arrays.asList("seeds101.txt02",101));
       // files.add(Arrays.asList("seeds101.txt03",101));
       // files.add(Arrays.asList("seeds101.txt04",101));
//
       // files.add(Arrays.asList("seeds102.txt00", 102));
       // files.add(Arrays.asList("seeds102.txt01", 102));
       // files.add(Arrays.asList("seeds102.txt02", 102));
       // files.add(Arrays.asList("seeds102.txt03", 102));
       // files.add(Arrays.asList("seeds102.txt04", 102));
//
       // files.add(Arrays.asList("seeds103.txt00", 103));
       // files.add(Arrays.asList("seeds103.txt01", 103));
       // files.add(Arrays.asList("seeds103.txt02", 103));
       // files.add(Arrays.asList("seeds103.txt03", 103));
       // files.add(Arrays.asList("seeds103.txt04", 103));
//
       // files.add(Arrays.asList("seeds115.txt00",115));
       // files.add(Arrays.asList("seeds115.txt01",115));
       // files.add(Arrays.asList("seeds115.txt02",115));
       // files.add(Arrays.asList("seeds115.txt03",115));
       // files.add(Arrays.asList("seeds115.txt04",115));
//
       // files.add(Arrays.asList("seeds116.txt00",116));
       // files.add(Arrays.asList("seeds116.txt01",116));
       // files.add(Arrays.asList("seeds116.txt02",116));
       // files.add(Arrays.asList("seeds116.txt03",116));
       // files.add(Arrays.asList("seeds116.txt04",116));
//
       // files.add(Arrays.asList("seeds119.txt00", 119));
       // files.add(Arrays.asList("seeds119.txt01", 119));
       // files.add(Arrays.asList("seeds119.txt02", 119));
       // files.add(Arrays.asList("seeds119.txt03", 119));
       // files.add(Arrays.asList("seeds119.txt04", 119));
//
       // files.add(Arrays.asList("seeds133.txt00", 133));
       // files.add(Arrays.asList("seeds133.txt01", 133));
       // files.add(Arrays.asList("seeds133.txt02", 133));
       // files.add(Arrays.asList("seeds133.txt03", 133));
       // files.add(Arrays.asList("seeds133.txt04", 133));
//
       // files.add(Arrays.asList("seeds135.txt00", 135));
       // files.add(Arrays.asList("seeds135.txt01", 135));
       // files.add(Arrays.asList("seeds135.txt02", 135));
       // files.add(Arrays.asList("seeds135.txt03", 135));
       // files.add(Arrays.asList("seeds135.txt04", 135));
//
       // files.add(Arrays.asList("seeds147.txt00",147));
       // files.add(Arrays.asList("seeds147.txt01",147));
       // files.add(Arrays.asList("seeds147.txt02",147));
       // files.add(Arrays.asList("seeds147.txt03",147));
       // files.add(Arrays.asList("seeds147.txt04",147));
//
       // files.add(Arrays.asList("seeds149.txt00", 149));
       // files.add(Arrays.asList("seeds149.txt01", 149));
       // files.add(Arrays.asList("seeds149.txt02", 149));
       // files.add(Arrays.asList("seeds149.txt03", 149));
       // files.add(Arrays.asList("seeds149.txt04", 149));
//
       // files.add(Arrays.asList("seeds150.txt00",150));
       // files.add(Arrays.asList("seeds150.txt01",150));
       // files.add(Arrays.asList("seeds150.txt02",150));
       // files.add(Arrays.asList("seeds150.txt03",150));
       // files.add(Arrays.asList("seeds150.txt04",150));
//
       // //done locally
       // //files.add(Arrays.asList("seeds151.txt00", 151));
       // //files.add(Arrays.asList("seeds151.txt01", 151));
       // //files.add(Arrays.asList("seeds151.txt02", 151));
       // //files.add(Arrays.asList("seeds151.txt03", 151));
       // //files.add(Arrays.asList("seeds151.txt04", 151));
//
       // files.add(Arrays.asList("seeds164.txt00", 164));
       // files.add(Arrays.asList("seeds164.txt01", 164));
       // files.add(Arrays.asList("seeds164.txt02", 164));
       // files.add(Arrays.asList("seeds164.txt03", 164));
       // files.add(Arrays.asList("seeds164.txt04", 164));
//
       // files.add(Arrays.asList("seeds165.txt00", 165));
       // files.add(Arrays.asList("seeds165.txt01", 165));
       // files.add(Arrays.asList("seeds165.txt02", 165));
       // files.add(Arrays.asList("seeds165.txt03", 165));
       // files.add(Arrays.asList("seeds165.txt04", 165));
//
       // files.add(Arrays.asList("seeds20.txt00", 20));
       // files.add(Arrays.asList("seeds20.txt01", 20));
       // files.add(Arrays.asList("seeds20.txt02", 20));
       // files.add(Arrays.asList("seeds20.txt03", 20));
       // files.add(Arrays.asList("seeds20.txt04", 20));
//
       // files.add(Arrays.asList("seeds23.txt00", 23));
       // files.add(Arrays.asList("seeds23.txt01", 23));
       // files.add(Arrays.asList("seeds23.txt02", 23));
       // files.add(Arrays.asList("seeds23.txt03", 23));
       // files.add(Arrays.asList("seeds23.txt04", 23));
//
       // files.add(Arrays.asList("seeds35.txt00", 35));
       // files.add(Arrays.asList("seeds35.txt01", 35));
       // files.add(Arrays.asList("seeds35.txt02", 35));
       // files.add(Arrays.asList("seeds35.txt03", 35));
       // files.add(Arrays.asList("seeds35.txt04", 35));
//
       // files.add(Arrays.asList("seeds36.txt00", 36));
       // files.add(Arrays.asList("seeds36.txt01", 36));
       // files.add(Arrays.asList("seeds36.txt02", 36));
       // files.add(Arrays.asList("seeds36.txt03", 36));
       // files.add(Arrays.asList("seeds36.txt04", 36));
//
       // files.add(Arrays.asList("seeds38.txt00", 38));
       // files.add(Arrays.asList("seeds38.txt01", 38));
       // files.add(Arrays.asList("seeds38.txt02", 38));
       // files.add(Arrays.asList("seeds38.txt03", 38));
       // files.add(Arrays.asList("seeds38.txt04", 38));

       // files.add(Arrays.asList("seeds52.txt00",52));
        //not done
        files.add(Arrays.asList("seeds52.txt01",52));
        files.add(Arrays.asList("seeds52.txt02",52));
        files.add(Arrays.asList("seeds52.txt03",52));
        files.add(Arrays.asList("seeds52.txt04",52));

        files.add(Arrays.asList("seeds53.txt00", 53));
        files.add(Arrays.asList("seeds53.txt01", 53));
        files.add(Arrays.asList("seeds53.txt02", 53));
        files.add(Arrays.asList("seeds53.txt03", 53));
        files.add(Arrays.asList("seeds53.txt04", 53));

        files.add(Arrays.asList("seeds55.txt00", 55));
        files.add(Arrays.asList("seeds55.txt01", 55));
        files.add(Arrays.asList("seeds55.txt02", 55));
        files.add(Arrays.asList("seeds55.txt03", 55));
        files.add(Arrays.asList("seeds55.txt04", 55));

        files.add(Arrays.asList("seeds70.txt00", 70));
        files.add(Arrays.asList("seeds70.txt01", 70));
        files.add(Arrays.asList("seeds70.txt02", 70));
        files.add(Arrays.asList("seeds70.txt03", 70));
        files.add(Arrays.asList("seeds70.txt04", 70));

        files.add(Arrays.asList("seeds84.txt00", 84));
        files.add(Arrays.asList("seeds84.txt01", 84));
        files.add(Arrays.asList("seeds84.txt02", 84));
        files.add(Arrays.asList("seeds84.txt03", 84));
        files.add(Arrays.asList("seeds84.txt04", 84));

        files.add(Arrays.asList("seeds87.txt00",87));
        files.add(Arrays.asList("seeds87.txt01",87));
        files.add(Arrays.asList("seeds87.txt02",87));
        files.add(Arrays.asList("seeds87.txt03",87));
        files.add(Arrays.asList("seeds87.txt04",87));

        files.add(Arrays.asList("seeds99.txt00", 99));
        files.add(Arrays.asList("seeds99.txt01", 99));
        files.add(Arrays.asList("seeds99.txt02", 99));
        files.add(Arrays.asList("seeds99.txt03", 99));
        files.add(Arrays.asList("seeds99.txt04", 99));


        for (int i = 0; i < 15; i++) {
            Thread thread = new Thread(new Main((String) files.get(i).get(0), ((int) files.get(i).get(1)) - 1, i));
            thread.start();
        }
        if (debug) {
            new Main("blob_seeds0.txt", -1, 0).run();
        }
    }


    public void run() {
        int[] mapWat = {77, 78, 77, 75}; // from z 12 to z15 in chunk
        int OFFSET = 12;
        List<Long> worldSeeds = null;
        long id = Thread.currentThread().getId();
        try {
            URI path = Objects.requireNonNull(Main.class.getClassLoader().getResource(name)).toURI();
            System.out.println(path + " " + id + " " + index);
            worldSeeds = Files.lines(Paths.get(path)).map(Long::valueOf).collect(Collectors.toList());
            System.out.println(worldSeeds.size() + " " + id + " on thread " + tid + " " + index);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long time = System.nanoTime();
        int chunkX = ((index + 16) >> 4) - 1;
        int chunkZ = -3;
        long cur = 0;
        long tot = worldSeeds.size();
        BiomesBase[] biomesForGeneration = new BiomesBase[256];
        BiomesBase[] validsBiomes = {BiomesBase.Forest, BiomesBase.Seasonal_forest, BiomesBase.Plains, BiomesBase.Shrubland};
        int ind = (index % 16 + 16) % 16;
        for (long seed : worldSeeds) {
            cur++;
            if ((cur % 100000) == 0) {
                System.out.printf("Time %f at %d %f%% on %d\n", (System.nanoTime() - time) / 1e9, cur, (double) cur / tot * 100, id);
            }
            BiomeGeneration biomeGenerationInstance = new BiomeGeneration(seed);
            biomeGenerationInstance.loadBiomes(biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
            boolean skipIt = false;
            for (BiomesBase biome : biomesForGeneration) {
                if (biome == BiomesBase.Rainforest || biome == BiomesBase.Swampland || biome == BiomesBase.Savanna || biome == BiomesBase.Taiga || biome == BiomesBase.Desert || biome == BiomesBase.IceDesert || biome == BiomesBase.Tundra) {
                    skipIt = true;
                    break;
                }
            }
            if (skipIt) {
                continue;
            }
            GenerateChunk generateChunk = new GenerateChunk(seed);
            byte[] chunk = generateChunk.provideChunk(chunkX, chunkZ, Boolean.FALSE, biomeGenerationInstance, biomesForGeneration);
            boolean flag = true;
            for (int z = 0; z < mapWat.length; z++) {
                int pos = 128 * ind * 16 + 128 * (z + OFFSET);
                int y;
                for (y = 127; y >= 0 && chunk[pos + y] == 0; y--) ;
                if ((y + 1) != mapWat[z]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                System.out.println("Found seed " + seed + " at x:" + index + " z:-30 on thread" + id);
            }

        }
        System.out.println("Finished on thread " + id + " at " + (System.nanoTime() - time) / 1e9 + " index: " + index);
    }

    public void debug() {
        Boolean fast = Boolean.FALSE;
        int chunkX = 0;
        int chunkZ = -3;
        long seed = 10;

        BiomesBase[] biomesForGeneration = null;
        BiomeGeneration biomeGenerationInstance = new BiomeGeneration(seed);
        biomesForGeneration = biomeGenerationInstance.loadBiomes(biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        for (BiomesBase biomesBase : biomesForGeneration) {
            System.out.print(biomesBase.name + " ");
        }
        System.out.println();
        GenerateChunk generateChunk = new GenerateChunk(seed);
        byte[] chunk = generateChunk.provideChunk(chunkX, chunkZ, fast, biomeGenerationInstance, biomesForGeneration);
        if (fast) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    System.out.print(chunk[16 * x + z] + " ");
                }
                System.out.println();
            }
        } else {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int pos = 128 * 16 * x + 128 * z;
                    int y;
                    for (y = 127; y >= 0 && chunk[pos + y] == 0; y--) ;
                    System.out.print((y + 1) + " ");
                }
                System.out.println();
            }
        }
    }

}
