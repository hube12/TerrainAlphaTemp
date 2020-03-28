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

        files.add(Arrays.asList("seeds15000",150));
        files.add(Arrays.asList("seeds15001",150));
        files.add(Arrays.asList("seeds15002",150));
        files.add(Arrays.asList("seeds15003",150));
        files.add(Arrays.asList("seeds15004",150));
        files.add(Arrays.asList("seeds15005",150));
        files.add(Arrays.asList("seeds15006",150));
        files.add(Arrays.asList("seeds15007",150));
        files.add(Arrays.asList("seeds15008",150));
        files.add(Arrays.asList("seeds15009",150));
        files.add(Arrays.asList("seeds15010",150));
        files.add(Arrays.asList("seeds15011",150));
        files.add(Arrays.asList("seeds15012",150));
        files.add(Arrays.asList("seeds15013",150));
        files.add(Arrays.asList("seeds15014",150));
        files.add(Arrays.asList("seeds15015",150));
        files.add(Arrays.asList("seeds15016",150));
        files.add(Arrays.asList("seeds15017",150));
        files.add(Arrays.asList("seeds15018",150));
        files.add(Arrays.asList("seeds15019",150));
        files.add(Arrays.asList("seeds15020",150));
        files.add(Arrays.asList("seeds15021",150));
        files.add(Arrays.asList("seeds15022",150));
        files.add(Arrays.asList("seeds15023",150));
        files.add(Arrays.asList("seeds15024",150));
        files.add(Arrays.asList("seeds15025",150));
        files.add(Arrays.asList("seeds15026",150));
        files.add(Arrays.asList("seeds15027",150));
        files.add(Arrays.asList("seeds15028",150));
        files.add(Arrays.asList("seeds15029",150));
        files.add(Arrays.asList("seeds15030",150));
        files.add(Arrays.asList("seeds15031",150));
        files.add(Arrays.asList("seeds15032",150));
        files.add(Arrays.asList("seeds15033",150));
        files.add(Arrays.asList("seeds15034",150));
        files.add(Arrays.asList("seeds15035",150));
        files.add(Arrays.asList("seeds15036",150));
        files.add(Arrays.asList("seeds15037",150));
        files.add(Arrays.asList("seeds15038",150));
        files.add(Arrays.asList("seeds15039",150));
        files.add(Arrays.asList("seeds15040",150));
        files.add(Arrays.asList("seeds15041",150));
        files.add(Arrays.asList("seeds15042",150));
        files.add(Arrays.asList("seeds9900",99));
        files.add(Arrays.asList("seeds8700",87));

        //files.add(Arrays.asList("seeds87.txt",87));
        //files.add(Arrays.asList("seeds99.txt",99));



        for (int i = 0; i < 45; i++) {
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
