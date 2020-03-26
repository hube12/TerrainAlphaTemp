

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
