package pano;

import main.BiomeGeneration;
import main.BiomesBase;
import main.GenerateChunk;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        pano("pano4");
    }
    public static void pano(String file) {
        //int[] mapWat = {74, 74, 74};int z = 9; // from x 12 to x14 in chunk at z9
        int[] mapWat = {74, 74, 73};int z = 10; // from x 12 to x14 in chunk at z10
        //int[] mapWat = {74, 74, 72};int z = 11; // from x 12 to x14 in chunk at z11
       // int[] mapWat = {74, 73, 72};int z = 12; // from x 12 to x14 in chunk at z12


        List<Long> worldSeeds = null;
        long id = Thread.currentThread().getId();
        try {
            URI path = Objects.requireNonNull(main.Main.class.getClassLoader().getResource("seeds/" + file)).toURI();
            System.out.println(path + " " + id);
            worldSeeds = Files.lines(Paths.get(path)).map(Long::valueOf).collect(Collectors.toList());
            System.out.println(worldSeeds.size() + " " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long time = System.nanoTime();
        int chunkX = 3;
        int chunkZ = -5;

        long cur = 0;

        long tot = worldSeeds.size();
        for (long seed : worldSeeds) {
            cur++;
            if ((cur % 10000) == 0) {
                System.out.printf("Time %f at %d %f%% on %d\n", (System.nanoTime() - time) / 1e9, cur, (double) cur / tot * 100, id);
            }
            byte[] chunk=new GenChunk(seed).provideChunk(chunkX,chunkZ);
            boolean flag = true;
            for (int x = 12; x < 15; x++) {
                int pos = 128 * x * 16 + 128 * z;
                int y;
                for (y = 80; y >= 70 && chunk[pos + y] == 0; y--) ;

                //System.out.println("x:"+x+" y"+y+" z:"+z+" pos:"+pos);
                if ((y ) != mapWat[x-12]) {
                    flag = false;
                    break;
                }
            }
            if (flag) System.out.println("Seed " + seed + " at x: 61 z: "+(chunkZ*16+z));


        }
        System.out.println("Finished on thread " + id + " at " + (System.nanoTime() - time) / 1e9);

    }
}
