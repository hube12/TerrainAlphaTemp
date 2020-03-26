

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        boolean debug = true;
        if (!debug) {
            int[] mapWat = {78, 77, 76, 76, 77, 78, 77, 75, 73};
            URI path = Objects.requireNonNull(Main.class.getClassLoader().getResource("seeds14.txt")).toURI();
            System.out.println(path);
            long time = System.nanoTime();
            List<Long> worldSeeds = Files.lines(Paths.get(path)).map(Long::valueOf).collect(Collectors.toList());
            System.out.println(worldSeeds.size());
            System.out.println((System.nanoTime() - time) / 1e9);
            int chunkX = 0;
            int chunkZ = -3;
            long cur = 0;
            long tot = worldSeeds.size();
            BiomesBase[] biomesForGeneration= new BiomesBase[256];
            for (long seed : worldSeeds) {
                BiomeGeneration biomeGenerationInstance = new BiomeGeneration(seed);
                biomeGenerationInstance.loadBiomes(biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);

                for (BiomesBase biome: biomesForGeneration){
                    ;
                }

                GenerateChunk generateChunk = new GenerateChunk(seed);
                byte[] chunk = generateChunk.provideChunk(chunkX, chunkZ, Boolean.FALSE,biomeGenerationInstance,biomesForGeneration);
                boolean flag = true;
                for (int z = 0; z < mapWat.length; z++) {
                    if (chunk[14 * 16 + z] != mapWat[z]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    System.out.println(seed);
                }
                cur++;
                if ((cur % 10000) == 0) {
                    System.out.printf("at %d %f%%\n", cur, (double) cur / tot * 100);
                    System.out.println((System.nanoTime() - time) / 1e9);
                }
                if ((cur % 100000) == 0) {
                    System.out.println((System.nanoTime() - time) / 1e9);
                    break;
                }
            }
            System.out.println((System.nanoTime() - time) / 1e9);
        }
        if (debug) {
            Boolean fast = Boolean.FALSE;
            int chunkX=0;
            int chunkZ=-3;
            long seed=10;
            BiomesBase[] biomesForGeneration= new BiomesBase[256];
            BiomeGeneration biomeGenerationInstance = new BiomeGeneration(seed);
            biomeGenerationInstance.loadBiomes(biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
            GenerateChunk generateChunk = new GenerateChunk(seed);

            byte[] chunk = generateChunk.provideChunk(chunkX, chunkZ, fast,biomeGenerationInstance,biomesForGeneration);
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


}
