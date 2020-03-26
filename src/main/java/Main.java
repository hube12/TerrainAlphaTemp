

import javafx.util.Pair;
import sun.nio.ch.Net;
import sun.swing.StringUIClientPropertyKey;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main implements Runnable {
    private String name;
    private int index;

    public Main(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        boolean debug = false;

        ArrayList<Pair<String, Integer>> files = new ArrayList<>();
        files.add(new Pair<>("seeds14.txt", 14));
        files.add(new Pair<>("seeds15.txt", 15));
        files.add(new Pair<>("seeds27.txt", 27));
        files.add(new Pair<>("seeds29.txt", 29));
        files.add(new Pair<>("seeds43.txt", 43));
        files.add(new Pair<>("seeds45.txt", 45));
        files.add(new Pair<>("seeds46.txt", 46));
        files.add(new Pair<>("seeds47.txt", 47));
        files.add(new Pair<>("seeds59.txt", 59));
        files.add(new Pair<>("seeds60.txt", 60));
        files.add(new Pair<>("seeds62.txt", 62));
        files.add(new Pair<>("seeds63.txt", 63));
        // ALL above are already have been runned
        // ALL below are on GCP instance
        files.add(new Pair<>("blob_seeds101.txt", 101));
        files.add(new Pair<>("blob_seeds102.txt", 102));
        files.add(new Pair<>("blob_seeds103.txt", 103));
        files.add(new Pair<>("blob_seeds104.txt", 104));
        files.add(new Pair<>("blob_seeds105.txt", 105));
        files.add(new Pair<>("blob_seeds108.txt", 108));
        files.add(new Pair<>("blob_seeds119.txt", 119));
        files.add(new Pair<>("blob_seeds124.txt", 124));
        files.add(new Pair<>("blob_seeds133.txt", 133));
        files.add(new Pair<>("blob_seeds135.txt", 135));
        files.add(new Pair<>("blob_seeds149.txt", 149));
        files.add(new Pair<>("blob_seeds150.txt", 150));
        files.add(new Pair<>("blob_seeds151.txt", 151));
        files.add(new Pair<>("blob_seeds152.txt", 152));
        files.add(new Pair<>("blob_seeds153.txt", 153));
        files.add(new Pair<>("blob_seeds154.txt", 154));
        files.add(new Pair<>("blob_seeds156.txt", 156));
        files.add(new Pair<>("blob_seeds165.txt", 165));
        files.add(new Pair<>("blob_seeds172.txt", 172));
        files.add(new Pair<>("blob_seeds23.txt", 23));
        files.add(new Pair<>("blob_seeds24.txt", 24));
        files.add(new Pair<>("blob_seeds26.txt", 26));
        files.add(new Pair<>("blob_seeds27.txt", 27));
        files.add(new Pair<>("blob_seeds38.txt", 38));
        files.add(new Pair<>("blob_seeds40.txt", 40));
        files.add(new Pair<>("blob_seeds41.txt", 41));
        files.add(new Pair<>("blob_seeds42.txt", 42));
        files.add(new Pair<>("blob_seeds43.txt", 43));
        files.add(new Pair<>("blob_seeds53.txt", 53));
        files.add(new Pair<>("blob_seeds55.txt", 55));
        files.add(new Pair<>("blob_seeds59.txt", 59));
        files.add(new Pair<>("blob_seeds6.txt", 6));
        files.add(new Pair<>("blob_seeds60.txt", 60));
        files.add(new Pair<>("blob_seeds7.txt", 7));
        files.add(new Pair<>("blob_seeds70.txt", 70));
        files.add(new Pair<>("blob_seeds73.txt", 73));
        files.add(new Pair<>("blob_seeds74.txt", 74));
        files.add(new Pair<>("blob_seeds76.txt", 76));
        files.add(new Pair<>("blob_seeds87.txt", 87));
        files.add(new Pair<>("blob_seeds88.txt", 88));
        files.add(new Pair<>("blob_seeds89.txt", 89));
        files.add(new Pair<>("blob_seeds9.txt", 9));
        files.add(new Pair<>("blob_seeds90.txt", 90));
        files.add(new Pair<>("blob_seeds92.txt", 92));

        files.add(new Pair<>("seeds108.txt", 108));
        files.add(new Pair<>("seeds110.txt", 110));
        files.add(new Pair<>("seeds124.txt", 124));
        files.add(new Pair<>("seeds126.txt", 126));
        files.add(new Pair<>("seeds141.txt", 141));
        files.add(new Pair<>("seeds143.txt", 143));
        files.add(new Pair<>("seeds156.txt", 156));
        files.add(new Pair<>("seeds157.txt", 157));
        files.add(new Pair<>("seeds158.txt", 158));
        files.add(new Pair<>("seeds172.txt", 172));
        files.add(new Pair<>("seeds76.txt", 76));
        files.add(new Pair<>("seeds78.txt", 78));
        files.add(new Pair<>("seeds79.txt", 79));
        files.add(new Pair<>("seeds92.txt", 92));
        files.add(new Pair<>("seeds93.txt", 93));




        for (int i = 0; i < 12; i++) {
            Thread thread = new Thread(new Main(files.get(i).getKey(), files.get(i).getValue()));
            thread.start();
        }
        if (!debug) {
            new Main("seeds14.txt", 14).run();
        }
    }

    public void run() {
        int[] mapWat = {78, 77, 76, 76, 77, 78, 77, 75, 73};
        List<Long> worldSeeds = null;
        long id=Thread.currentThread().getId();
        try {
            URI path = Objects.requireNonNull(Main.class.getClassLoader().getResource("worldSeed/"+name)).toURI();
            System.out.println(path + " "+ id);
            worldSeeds = Files.lines(Paths.get(path)).map(Long::valueOf).collect(Collectors.toList());
            System.out.println(worldSeeds.size()+ " "+id );
        } catch (Exception e) {
            e.printStackTrace();
        }

        long time = System.nanoTime();
        int chunkX = index >> 4;
        int chunkZ = -3;
        long cur = 0;
        long tot = worldSeeds.size();
        BiomesBase[] biomesForGeneration = new BiomesBase[256];
        BiomesBase[] validsBiomes = {BiomesBase.Forest, BiomesBase.Seasonal_forest, BiomesBase.Plains, BiomesBase.Shrubland};
        int ind = index % 16;
        for (long seed : worldSeeds) {
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
                if (chunk[ind * 16 + z] != mapWat[z]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                System.out.println(seed + " at x:"+index+" z:-30");
            }
            cur++;
            if ((cur % 100000) == 0) {
                System.out.printf("Time %f at %d %f%% on %d\n",(System.nanoTime() - time) / 1e9, cur, (double) cur / tot * 100,id);
            }
        }
        System.out.println("Finished on thread " + id + " at " +(System.nanoTime() - time) / 1e9);
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
