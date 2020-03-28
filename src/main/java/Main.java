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
        //files.add(Arrays.asList("blob_seeds0.txt",0));
        //files.add(Arrays.asList("blob_seeds100.txt",100));
        //files.add(Arrays.asList("blob_seeds110.txt",110));
        //files.add(Arrays.asList("blob_seeds112.txt",112));
        //files.add(Arrays.asList("blob_seeds114.txt",114));
        //files.add(Arrays.asList("blob_seeds115.txt",115));
        //files.add(Arrays.asList("blob_seeds116.txt",116));
        //files.add(Arrays.asList("blob_seeds126.txt",126));
        //files.add(Arrays.asList("blob_seeds128.txt",128));
        //files.add(Arrays.asList("blob_seeds141.txt",141));
        //files.add(Arrays.asList("blob_seeds143.txt",143));
        //files.add(Arrays.asList("blob_seeds146.txt",146));
        //files.add(Arrays.asList("blob_seeds147.txt",147));
        //files.add(Arrays.asList("blob_seeds14.txt",14));
        //files.add(Arrays.asList("blob_seeds157.txt",157));
        //files.add(Arrays.asList("blob_seeds158.txt",158));
        //files.add(Arrays.asList("blob_seeds15.txt",15));
        //files.add(Arrays.asList("blob_seeds160.txt",160));
        //files.add(Arrays.asList("blob_seeds164.txt",164));
        //files.add(Arrays.asList("blob_seeds16.txt",16));
        //files.add(Arrays.asList("blob_seeds177.txt",177));
        //files.add(Arrays.asList("blob_seeds18.txt",18));
        //files.add(Arrays.asList("blob_seeds20.txt",20));
        //files.add(Arrays.asList("blob_seeds29.txt",29));


        //GCP

        files.add(Arrays.asList("seeds115aaa",115));
        files.add(Arrays.asList("seeds115aab",115));
        files.add(Arrays.asList("seeds115aac",115));
        files.add(Arrays.asList("seeds115aad",115));
        files.add(Arrays.asList("seeds115aae",115));
        files.add(Arrays.asList("seeds115aaf",115));
        files.add(Arrays.asList("seeds115aag",115));
        files.add(Arrays.asList("seeds115aah",115));
        files.add(Arrays.asList("seeds115aai",115));
        files.add(Arrays.asList("seeds115aaj",115));
        files.add(Arrays.asList("seeds115aak",115));
        files.add(Arrays.asList("seeds115aal",115));
        files.add(Arrays.asList("seeds115aam",115));
        files.add(Arrays.asList("seeds116aaa",116));
        files.add(Arrays.asList("seeds116aab",116));
        files.add(Arrays.asList("seeds116aac",116));
        files.add(Arrays.asList("seeds116aad",116));
        files.add(Arrays.asList("seeds116aae",116));
        files.add(Arrays.asList("seeds116aaf",116));
        files.add(Arrays.asList("seeds116aag",116));
        files.add(Arrays.asList("seeds116aah",116));
        files.add(Arrays.asList("seeds116aai",116));
        files.add(Arrays.asList("seeds116aaj",116));
        files.add(Arrays.asList("seeds116aak",116));
        files.add(Arrays.asList("seeds116aal",116));
        files.add(Arrays.asList("seeds116aam",116));
        files.add(Arrays.asList("seeds119aaa",119));
        files.add(Arrays.asList("seeds119aab",119));
        files.add(Arrays.asList("seeds119aac",119));
        files.add(Arrays.asList("seeds119aad",119));
        files.add(Arrays.asList("seeds119aae",119));
        files.add(Arrays.asList("seeds119aaf",119));
        files.add(Arrays.asList("seeds119aag",119));
        files.add(Arrays.asList("seeds119aah",119));
        files.add(Arrays.asList("seeds119aai",119));
        files.add(Arrays.asList("seeds119aaj",119));
        files.add(Arrays.asList("seeds119aak",119));
        files.add(Arrays.asList("seeds119aal",119));
        files.add(Arrays.asList("seeds119aam",119));
        files.add(Arrays.asList("seeds133aaa",133));
        files.add(Arrays.asList("seeds133aab",133));
        files.add(Arrays.asList("seeds133aac",133));
        files.add(Arrays.asList("seeds133aad",133));
        files.add(Arrays.asList("seeds133aae",133));
        files.add(Arrays.asList("seeds133aaf",133));
        files.add(Arrays.asList("seeds133aag",133));
        files.add(Arrays.asList("seeds133aah",133));
        files.add(Arrays.asList("seeds133aai",133));
        files.add(Arrays.asList("seeds133aaj",133));
        files.add(Arrays.asList("seeds133aak",133));
        files.add(Arrays.asList("seeds133aal",133));
        files.add(Arrays.asList("seeds133aam",133));
        files.add(Arrays.asList("seeds135aaa",135));
        files.add(Arrays.asList("seeds135aab",135));
        files.add(Arrays.asList("seeds135aac",135));
        files.add(Arrays.asList("seeds135aad",135));
        files.add(Arrays.asList("seeds135aae",135));
        files.add(Arrays.asList("seeds135aaf",135));
        files.add(Arrays.asList("seeds135aag",135));
        files.add(Arrays.asList("seeds135aah",135));
        files.add(Arrays.asList("seeds135aai",135));
        files.add(Arrays.asList("seeds135aaj",135));
        files.add(Arrays.asList("seeds135aak",135));
        files.add(Arrays.asList("seeds135aal",135));
        files.add(Arrays.asList("seeds135aam",135));
        files.add(Arrays.asList("seeds147aaa",147));
        files.add(Arrays.asList("seeds147aab",147));
        files.add(Arrays.asList("seeds147aac",147));
        files.add(Arrays.asList("seeds147aad",147));
        files.add(Arrays.asList("seeds147aae",147));
        files.add(Arrays.asList("seeds147aaf",147));
        files.add(Arrays.asList("seeds147aag",147));
        files.add(Arrays.asList("seeds147aah",147));
        files.add(Arrays.asList("seeds147aai",147));
        files.add(Arrays.asList("seeds147aaj",147));
        files.add(Arrays.asList("seeds147aak",147));
        files.add(Arrays.asList("seeds147aal",147));
        files.add(Arrays.asList("seeds147aam",147));
        files.add(Arrays.asList("seeds151aaa",151));
        files.add(Arrays.asList("seeds151aab",151));
        files.add(Arrays.asList("seeds151aac",151));
        files.add(Arrays.asList("seeds151aad",151));
        files.add(Arrays.asList("seeds151aae",151));
        files.add(Arrays.asList("seeds151aaf",151));
        files.add(Arrays.asList("seeds151aag",151));
        files.add(Arrays.asList("seeds151aah",151));
        files.add(Arrays.asList("seeds151aai",151));
        files.add(Arrays.asList("seeds151aaj",151));
        files.add(Arrays.asList("seeds151aak",151));
        files.add(Arrays.asList("seeds151aal",151));
        files.add(Arrays.asList("seeds151aam",151));
        files.add(Arrays.asList("seeds164aaa",164));
        files.add(Arrays.asList("seeds164aab",164));
        files.add(Arrays.asList("seeds164aac",164));
        files.add(Arrays.asList("seeds164aad",164));
        files.add(Arrays.asList("seeds164aae",164));
        files.add(Arrays.asList("seeds164aaf",164));
        files.add(Arrays.asList("seeds164aag",164));
        files.add(Arrays.asList("seeds164aah",164));
        files.add(Arrays.asList("seeds164aai",164));
        files.add(Arrays.asList("seeds164aaj",164));
        files.add(Arrays.asList("seeds164aak",164));
        files.add(Arrays.asList("seeds164aal",164));
        files.add(Arrays.asList("seeds164aam",164));

        //files.add(Arrays.asList("seeds87.txt",87));
        //files.add(Arrays.asList("seeds99.txt",99));



        for (int i = 0; i < 104; i++) {
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
