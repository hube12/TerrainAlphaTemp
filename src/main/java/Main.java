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

    public Main(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static void main(String[] args) {
        boolean debug = false;

        ArrayList<List<Object>> files = new ArrayList<>();

        files.add(Arrays.asList("seeds165.txt00",165));
        files.add(Arrays.asList("seeds165.txt01",165));
        files.add(Arrays.asList("seeds165.txt02",165));
        files.add(Arrays.asList("seeds165.txt03",165));
        files.add(Arrays.asList("seeds165.txt04",165));
        files.add(Arrays.asList("seeds165.txt05",165));
        files.add(Arrays.asList("seeds165.txt06",165));
        files.add(Arrays.asList("seeds20.txt00",20));
        files.add(Arrays.asList("seeds20.txt01",20));
        files.add(Arrays.asList("seeds20.txt02",20));
        files.add(Arrays.asList("seeds20.txt03",20));
        files.add(Arrays.asList("seeds20.txt04",20));
        files.add(Arrays.asList("seeds20.txt05",20));
        files.add(Arrays.asList("seeds20.txt06",20));
        files.add(Arrays.asList("seeds23.txt00",23));
        files.add(Arrays.asList("seeds23.txt01",23));
        files.add(Arrays.asList("seeds23.txt02",23));
        files.add(Arrays.asList("seeds23.txt03",23));
        files.add(Arrays.asList("seeds23.txt04",23));
        files.add(Arrays.asList("seeds23.txt05",23));
        files.add(Arrays.asList("seeds23.txt06",23));
        files.add(Arrays.asList("seeds3.txt00",3));
        files.add(Arrays.asList("seeds3.txt01",3));
        files.add(Arrays.asList("seeds3.txt02",3));
        files.add(Arrays.asList("seeds3.txt03",3));
        files.add(Arrays.asList("seeds3.txt04",3));
        files.add(Arrays.asList("seeds3.txt05",3));
        files.add(Arrays.asList("seeds3.txt06",3));
        files.add(Arrays.asList("seeds35.txt00",35));
        files.add(Arrays.asList("seeds35.txt01",35));
        files.add(Arrays.asList("seeds35.txt02",35));
        files.add(Arrays.asList("seeds35.txt03",35));
        files.add(Arrays.asList("seeds35.txt04",35));
        files.add(Arrays.asList("seeds35.txt05",35));
        files.add(Arrays.asList("seeds35.txt06",35));
        files.add(Arrays.asList("seeds36.txt00",36));
        files.add(Arrays.asList("seeds36.txt01",36));
        files.add(Arrays.asList("seeds36.txt02",36));
        files.add(Arrays.asList("seeds36.txt03",36));
        files.add(Arrays.asList("seeds36.txt04",36));
        files.add(Arrays.asList("seeds36.txt05",36));
        files.add(Arrays.asList("seeds36.txt06",36));
        files.add(Arrays.asList("seeds38.txt00",38));
        files.add(Arrays.asList("seeds38.txt01",38));
        files.add(Arrays.asList("seeds38.txt02",38));
        files.add(Arrays.asList("seeds38.txt03",38));
        files.add(Arrays.asList("seeds38.txt04",38));
        files.add(Arrays.asList("seeds38.txt05",38));
        files.add(Arrays.asList("seeds38.txt06",38));
        files.add(Arrays.asList("seeds4.txt00",4));
        files.add(Arrays.asList("seeds4.txt01",4));
        files.add(Arrays.asList("seeds4.txt02",4));
        files.add(Arrays.asList("seeds4.txt03",4));
        files.add(Arrays.asList("seeds4.txt04",4));
        files.add(Arrays.asList("seeds4.txt05",4));
        files.add(Arrays.asList("seeds4.txt06",4));
        files.add(Arrays.asList("seeds52.txt00",52));
        files.add(Arrays.asList("seeds52.txt01",52));
        files.add(Arrays.asList("seeds52.txt02",52));
        files.add(Arrays.asList("seeds52.txt03",52));
        files.add(Arrays.asList("seeds52.txt04",52));
        files.add(Arrays.asList("seeds52.txt05",52));
        files.add(Arrays.asList("seeds52.txt06",52));
        files.add(Arrays.asList("seeds53.txt00",53));
        files.add(Arrays.asList("seeds53.txt01",53));
        files.add(Arrays.asList("seeds53.txt02",53));
        files.add(Arrays.asList("seeds53.txt03",53));
        files.add(Arrays.asList("seeds53.txt04",53));
        files.add(Arrays.asList("seeds53.txt05",53));
        files.add(Arrays.asList("seeds53.txt06",53));
        files.add(Arrays.asList("seeds55.txt00",55));
        files.add(Arrays.asList("seeds55.txt01",55));
        files.add(Arrays.asList("seeds55.txt02",55));
        files.add(Arrays.asList("seeds55.txt03",55));
        files.add(Arrays.asList("seeds55.txt04",55));
        files.add(Arrays.asList("seeds55.txt05",55));
        files.add(Arrays.asList("seeds55.txt06",55));
        files.add(Arrays.asList("seeds6.txt00",6));
        files.add(Arrays.asList("seeds6.txt01",6));
        files.add(Arrays.asList("seeds6.txt02",6));
        files.add(Arrays.asList("seeds6.txt03",6));
        files.add(Arrays.asList("seeds6.txt04",6));
        files.add(Arrays.asList("seeds6.txt05",6));
        files.add(Arrays.asList("seeds6.txt06",6));
        files.add(Arrays.asList("seeds7.txt00",7));
        files.add(Arrays.asList("seeds7.txt01",7));
        files.add(Arrays.asList("seeds7.txt02",7));
        files.add(Arrays.asList("seeds7.txt03",7));
        files.add(Arrays.asList("seeds7.txt04",7));
        files.add(Arrays.asList("seeds7.txt05",7));
        files.add(Arrays.asList("seeds7.txt06",7));
        files.add(Arrays.asList("seeds70.txt00",70));
        files.add(Arrays.asList("seeds70.txt01",70));
        files.add(Arrays.asList("seeds70.txt02",70));
        files.add(Arrays.asList("seeds70.txt03",70));
        files.add(Arrays.asList("seeds70.txt04",70));
        files.add(Arrays.asList("seeds70.txt05",70));
        files.add(Arrays.asList("seeds70.txt06",70));
        files.add(Arrays.asList("seeds84.txt00",84));
        files.add(Arrays.asList("seeds84.txt01",84));
        files.add(Arrays.asList("seeds84.txt02",84));
        files.add(Arrays.asList("seeds84.txt03",84));
        files.add(Arrays.asList("seeds84.txt04",84));
        files.add(Arrays.asList("seeds84.txt05",84));
        files.add(Arrays.asList("seeds84.txt06",84));



        for (int i = 0; i < 105; i++) {
            Thread thread = new Thread(new Main((String)files.get(i).get(0), ((int)files.get(i).get(1))-1));
            thread.start();
        }
        if (debug) {
            new Main("blob_seeds0.txt", -1).run();
        }
    }


    public void run() {
        int[] mapWat = {77,78,77,75}; // from z 12 to z15 in chunk
        int OFFSET=12;
        List<Long> worldSeeds = null;
        long id=Thread.currentThread().getId();
        try {
            URI path = Objects.requireNonNull(Main.class.getClassLoader().getResource(name)).toURI();
            System.out.println(path + " "+ id+" "+index);
            worldSeeds = Files.lines(Paths.get(path)).map(Long::valueOf).collect(Collectors.toList());
            System.out.println(worldSeeds.size()+ " "+id );
        } catch (Exception e) {
            e.printStackTrace();
        }

        long time = System.nanoTime();
        int chunkX = ((index+16) >> 4)-1;
        int chunkZ = -3;
        long cur = 0;
        long tot = worldSeeds.size();
        BiomesBase[] biomesForGeneration = new BiomesBase[256];
        BiomesBase[] validsBiomes = {BiomesBase.Forest, BiomesBase.Seasonal_forest, BiomesBase.Plains, BiomesBase.Shrubland};
        int ind = (index % 16 +16)%16;
        for (long seed : worldSeeds) {
            cur++;
            if ((cur % 100000) == 0) {
                System.out.printf("Time %f at %d %f%% on %d\n",(System.nanoTime() - time) / 1e9, cur, (double) cur / tot * 100,id);
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
                int pos = 128* ind * 16 + 128*(z+OFFSET);
                int y;
                for (y = 127; y >= 0 && chunk[pos + y] == 0; y--) ;
                if ((y+1) != mapWat[z]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                System.out.println("Seed " + seed + " at x:"+index+" z:-30");
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
