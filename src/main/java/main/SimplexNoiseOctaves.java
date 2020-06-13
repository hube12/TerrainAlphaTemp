package main;

import java.util.Arrays;

public class SimplexNoiseOctaves extends NoiseGenerator {

    public SimplexNoiseOctaves(Random random, int nbOctaves) {
        this.nbOctaves = nbOctaves;
        noise = new SimplexNoise[nbOctaves];
        for (int octave = 0; octave < nbOctaves; octave++) {
            noise[octave] = new SimplexNoise(random);
        }

    }

    public double[] generateNoise(double[] cache, double chunkX, double chunkZ, int x, int z, double offsetX, double offsetZ, double ampFactor) {
        return generateOctaves(cache, chunkX, chunkZ, x, z, offsetX, offsetZ, ampFactor, 0.5D);
    }

    public double[] generateOctaves(double[] cache, double chunkX, double chunkZ, int x, int z, double offsetX, double offsetZ, double ampFactor, double octaveFactor) {
        offsetX /= 1.5D;
        offsetZ /= 1.5D;
        if (cache == null || cache.length < x * z) {
            cache = new double[x * z];
        } else {
            Arrays.fill(cache, 0.0D);

        }
        double octaveDiminution = 1.0D;
        double octaveAmplification = 1.0D;
        for (int l = 0; l < nbOctaves; l++) {
            noise[l].noise(cache, chunkX, chunkZ, x, z, offsetX * octaveAmplification, offsetZ * octaveAmplification, 0.55000000000000004D / octaveDiminution);
            octaveAmplification *= ampFactor;
            octaveDiminution *= octaveFactor;
        }
        return cache;
    }

    private final SimplexNoise[] noise;
    private final int nbOctaves;
}
