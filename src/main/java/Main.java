import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class Main implements Runnable {
    private String name;
    private int index;

    public Main(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 256; i++) {
            long seed=random.nextLong();
            testEquality(seed);
            //evaluateSpeed(seed);
        }
       // evaluateSpeed(2518515);
    }


    public static void evaluateSpeed(long seed) {
        Main main = new Main("test.txt", -1);
        long time1 = 0;
        long time2 = 0;
        long counter = System.nanoTime();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                main.original(seed, x, z);

            }
        }
        time1 += System.nanoTime() - counter;
        counter = System.nanoTime();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                main.newOne(seed, x, z);

            }
        }
        time2 += System.nanoTime() - counter;
        counter = System.nanoTime();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                main.newOne(seed, x, z);

            }
        }
        time2 += System.nanoTime() - counter;
        counter = System.nanoTime();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                main.original(seed, x, z);
            }
        }
        time1 += System.nanoTime() - counter;
        System.out.println("Time 1 : " + time1 / 1e9D + " time 2: " + time2 / 1e9D + " difference: " + (float)Math.abs(time2 - time1) / time1*100+" %");
    }

    public static void testEquality(long seed) {
        Main main = new Main("test.txt", -1);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                List<Integer> res1 = main.original(seed, x, z);
                List<Integer> res2 = main.newOne(seed, x, z);
                for (int i = 0; i < res1.size(); i++) {
                    if (!res1.get(i).equals(res2.get(i))){// && res1.get(i)>=73 && res1.get(i)<=79) {
                        System.out.printf("Different for x:%d z:%d at index %d with original value %d and new value %d\n", x, z, i, res1.get(i), res2.get(i));
                    }
                }
            }
        }
    }

    public void run() {
        int[] mapWat = {77, 78, 77, 75}; // from z 12 to z15 in chunk
        int OFFSET = 12;
        List<Long> worldSeeds = null;
        long id = Thread.currentThread().getId();
        try {
            URI path = Objects.requireNonNull(Main.class.getClassLoader().getResource("seeds/" + name)).toURI();
            System.out.println(path + " " + id + " " + index);
            worldSeeds = Files.lines(Paths.get(path)).map(Long::valueOf).collect(Collectors.toList());
            System.out.println(worldSeeds.size() + " " + id);
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
                System.out.println("Seed " + seed + " at x:" + index + " z:-30");
            }

        }
        System.out.println("Finished on thread " + id + " at " + (System.nanoTime() - time) / 1e9);
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

    public List<Integer> original(long seed, int chunkX, int chunkZ) {
        List<Integer> res = new ArrayList<>();
        BiomeGeneration biomeGenerationInstance = new BiomeGeneration(seed);
        BiomesBase[] biomesForGeneration = biomeGenerationInstance.loadBiomes(null, chunkX * 16, chunkZ * 16, 16, 16);
        GenerateChunk generateChunk = new GenerateChunk(seed);
        byte[] chunk = generateChunk.provideChunk(chunkX, chunkZ, false, biomeGenerationInstance, biomesForGeneration);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int pos = 128 * 16 * x + 128 * z;
                int y;
                for (y = 127; y >= 0 && chunk[pos + y] == 0; y--) ;
                res.add(y + 1);
            }
        }
        return res;
    }

    public List<Integer> newOne(long seed, int chunkX, int chunkZ) {
        List<Integer> res = new ArrayList<>();
        BiomeGeneration biomeGenerationInstance = new BiomeGeneration(seed);
        BiomesBase[] biomesForGeneration = biomeGenerationInstance.loadBiomes(null, chunkX * 16, chunkZ * 16, 16, 16);
        GenerateChunkBisPerlin generateChunkbis = new GenerateChunkBisPerlin(seed);
        byte[] chunkbis = generateChunkbis.provideChunk(chunkX, chunkZ, false, biomeGenerationInstance, biomesForGeneration);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int pos = 128 * 16 * x + 128 * z;
                int y;
                for (y = 127; y >= 0 && chunkbis[pos + y] == 0; y--) ;
                res.add(y + 1);
            }
        }
        return res;
    }


    public void compare() {
        int chunkX = 0;
        int chunkZ = -3;
        long seed = 10;
        BiomeGeneration biomeGenerationInstance = new BiomeGeneration(seed);
        BiomesBase[] biomesForGeneration = biomeGenerationInstance.loadBiomes(null, chunkX * 16, chunkZ * 16, 16, 16);
        for (BiomesBase biomesBase : biomesForGeneration) {
            System.out.print(biomesBase.name + " ");
        }
        GenerateChunk generateChunk = new GenerateChunk(seed);
        byte[] chunk = generateChunk.provideChunk(chunkX, chunkZ, false, biomeGenerationInstance, biomesForGeneration);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int pos = 128 * 16 * x + 128 * z;
                int y;
                for (y = 127; y >= 0 && chunk[pos + y] == 0; y--) ;
                System.out.print((y + 1) + " ");
            }
            System.out.println();
        }
        System.out.println("--------");
        GenerateChunkBis generateChunkbis = new GenerateChunkBis(seed);
        byte[] chunkbis = generateChunkbis.provideChunk(chunkX, chunkZ, false, biomeGenerationInstance, biomesForGeneration);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int pos = 128 * 16 * x + 128 * z;
                int y;
                for (y = 127; y >= 0 && chunkbis[pos + y] == 0; y--) ;
                System.out.print((y + 1) + " ");
            }
            System.out.println();
        }


    }

}
