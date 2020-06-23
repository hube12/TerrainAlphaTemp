package pano;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {

    public static List<Long> genSeedList(File... files) throws Exception {
        List<Long> result = new ArrayList<>();
        for (File f : files) {
            DataInputStream dis = new DataInputStream(new FileInputStream(f));
            System.out.println("Using seed output file: " + f.getName());
            if (dis.available() % 8 != 0) System.err.println("Warning, file input did not have correct number of bytes, discarding invalid last bytes");
            System.out.println("Seeds in file: " + (dis.available() / 8));
            while (dis.available() > 7) {
                result.add(Long.reverseBytes(dis.readLong()));
                //result.add(dis.readLong());
            }
            dis.close();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        pano("pano4");
    }

    public static void pano(String file) throws Exception {
        //int[] mapWat = {74, 74, 74};int z = 9; // from x 12 to x14 in chunk at z9
        // int[] mapWat = {74, 74, 73};int z = 10; // from x 12 to x14 in chunk at z10
        //int[] mapWat = {74, 74, 72};int z = 11; // from x 12 to x14 in chunk at z11
        // int[] mapWat = {74, 73, 72};int z = 12; // from x 12 to x14 in chunk at z12
        int size=2;
        int OFFSET=13;
        //int[] mapWat = {0, 0};int z = 9; // from x 12 to x14 in chunk at z9
        //int[] mapWat = {0, -1};int z = 10; // from x 12 to x14 in chunk at z10
        //int[] mapWat = {0, -2};int z = 11; // from x 12 to x14 in chunk at z11
         int[] mapWat = {-1, -1};int z = 12; // from x 12 to x14 in chunk at z12


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
        //worldSeeds = genSeedList(new File(Objects.requireNonNull(main.Main.class.getClassLoader().getResource("seeds/" + "seed_output.dat")).toURI()),
        //        new File(Objects.requireNonNull(main.Main.class.getClassLoader().getResource("seeds/" + "seed_output_first_4999_blocks.dat")).toURI()),
        //        new File(Objects.requireNonNull(main.Main.class.getClassLoader().getResource("seeds/" + "seed_output_next_big_block.dat")).toURI()));
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
            byte[] chunk = new GenChunk(seed).provideChunk(chunkX, chunkZ);
            boolean flag = true;
            int y,pos;
            List<Integer> heights=new ArrayList<>();
            pos = 128 * (OFFSET-1) * 16 + 128 * z;
            for (y = 80; y >= 70 && chunk[pos + y] == 0; y--) ;
            int last=y;
            heights.add(last);
            for (int x = OFFSET; x < OFFSET+size; x++) {
                pos = 128 * x * 16 + 128 * z;
                for (y = 80; y >= 70 && chunk[pos + y] == 0; y--) ;
                //System.out.println("x:"+x+" y"+y+" z:"+z+" pos:"+pos);
                if ((y-last) != mapWat[x - OFFSET]) {
                    flag = false;
                    break;
                }
                last=y;
                heights.add(last);
            }
            if (flag) System.out.println("Seed " + seed + " at x: 61 z: " + (chunkZ * 16 + z)+" "+heights.toString());


        }
        System.out.println("Finished on thread " + id + " at " + (System.nanoTime() - time) / 1e9);

    }
}
